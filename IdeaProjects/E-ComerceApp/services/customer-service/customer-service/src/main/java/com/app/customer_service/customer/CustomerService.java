package com.app.customer_service.customer;

import com.app.customer_service.address.AddressRequest;
import com.app.customer_service.address.AddressService;

import com.app.customer_service.admin.AdminRepo;
import com.app.customer_service.admin.AdminService;
import com.app.customer_service.admin.Administrator;
import com.app.customer_service.exceptions.AdminAlreadyExistsException;
import com.app.customer_service.exceptions.CustomerAlreadyExistsException;
import com.app.customer_service.exceptions.CustomerNotFoundException;
import com.app.customer_service.keycloak.CodeRequest;
import com.app.customer_service.keycloak.KeyCloakAdminClient;
import com.app.customer_service.keycloak.Roles;
import com.app.customer_service.producer.KafkaProducer;
import com.app.customer_service.refresh_token.AdminRefreshToken;
import com.app.customer_service.refresh_token.AdminRefreshTokenRepo;
import com.app.customer_service.refresh_token.UserRefreshToken;
import com.app.customer_service.refresh_token.UserRefreshTokenRepo;
import com.app.customer_service.security.Aes.AesEncryptor;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.app.customer_service.superadmin.TokenValidator;
import com.ecom.EmailRequest;
import com.ecom.RegisterAdminRequest;
import com.ecom.SuperAdminRequest;
import com.app.customer_service.superadmin.TokenGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepo repo;
    private final CustomerMapper mapper;
    private final AddressService addressService;
    private final KeyCloakAdminClient keyCloakAdminClient;
    private final TokenGenerator tokenGenerator;
    private final KafkaProducer kafkaProducer;
    private final AdminRepo adminRepo;
    private final TokenValidator tokenValidator;
    private final UserRefreshTokenRepo userRefreshTokenRepo;
    private final AdminRefreshTokenRepo adminRefreshTokenRepo;
    private final AesEncryptor aesEncryptor;
    private final AdminService adminService;


    public Integer save(CustomerDto customerRequest) {
        Integer id=repo.save(mapper.toCustomer(customerRequest)).getId();
        keyCloakAdminClient.createUserAndSendActionsEmail(
                customerRequest.email(),
                customerRequest.email(),
                customerRequest.firstname(),
                customerRequest.lastname(),
                Roles.user,
                id

        ).block();
        return id;


    }
    public void updateCustomer(@Valid @RequestBody CustomerUpdateDto customerResponse,Integer Id) {
        Customer customer=repo.findById(Id).orElseThrow(()->
            new CustomerNotFoundException("No Customer with the given "+Id)
        );
        mergeCustomer(customerResponse,customer);
    }

    private void mergeCustomer(CustomerUpdateDto customerRequest, Customer customer) {
        if(StringUtils.isNotBlank(customerRequest.firstname())){
            customer.setFirstname(customerRequest.firstname());
        }
        if(StringUtils.isNotBlank(customerRequest.lastname())){
            customer.setLastname(customerRequest.lastname());
        }
        repo.save(customer);
    }
    public List<CustomerResponse> getAllCustomer() {
        return repo.findAll().stream().map(mapper:: toCustomerResponse).collect(Collectors.toList());
    }
    public boolean isExisting(int id){
        return repo.findById(id).isPresent();
    }

    public CustomerResponse getById(int cusId) {
       return repo.findById(cusId).map(mapper:: toCustomerResponse)
               .orElseThrow(()-> new CustomerNotFoundException("No Customer with the given "+cusId));
    }

    public void deleteCustomer(int cusId) {
        repo.deleteById(cusId);
    }

    public JsonNode exchangeCodeForTokens(CodeRequest codeRequest) {
        return keyCloakAdminClient.exchangeCodeForTokens(codeRequest.code(), codeRequest.codeVerifier(), codeRequest.redirectUri()).block();
    }

    public Mono<Void> logout(String authHeader) {
        DecodedJWT jwt = extractAndDecodeToken(authHeader);
        String email = jwt.getClaim("email").asString();
        List<String> roles = (List<String>) jwt.getClaim("realm_access").asMap().get("roles");
        String decryptedRefreshToken = getDecryptedRefreshToken(email, roles);
        return keyCloakAdminClient.logout(decryptedRefreshToken);
    }

    public void generateAndSendToken(SuperAdminRequest request) {
        String token = tokenGenerator.generateToken(request.Email(),request.firstName(), request.lastName());
        System.out.println(token);
        String url = "http://localhost:5173/register-admin?token=" + token;
        kafkaProducer.sendEmailRequest(new EmailRequest(request,url));

    }

    public void registerAdmin(RegisterAdminRequest adminRequest){
        if(tokenValidator.isValid(adminRequest.token())){
            String email=tokenValidator.extractEmail(adminRequest.token());
            Integer id=adminService.save(Administrator.builder().firstName(adminRequest.firstName())
                    .lastName(adminRequest.lastName())
                    .email(email)
                    .build());

            keyCloakAdminClient.createUserAndSendActionsEmail(email,email,adminRequest.firstName(),adminRequest.lastName(),Roles.admin,null).block();


        }



    }

    public void saveAndValidateTokens(String accessToken, String refreshToken, long refreshTokenExpiresIn) {
        DecodedJWT jwt = com.auth0.jwt.JWT.decode(accessToken);
        String email = jwt.getClaim("email").asString();
        List<String> roles = (List<String>) jwt.getClaim("realm_access").asMap().get("roles");
        String encrypted =aesEncryptor.aesEncrypt(refreshToken);

        if (roles.contains("admin")) {
            Administrator admin = adminRepo.findByEmail(email).orElseThrow(()->new EntityNotFoundException("Error Cannot Fetch Details"));
            AdminRefreshToken record=adminRefreshTokenRepo.findByAdministrator_Id(admin.getId());
            if(record!=null){
                record.setEncryptedRefreshToken(encrypted);
                record.setExpiresAt(Instant.now().plus(Duration.ofSeconds(refreshTokenExpiresIn)));
                adminRefreshTokenRepo.save(record);
            }
            else{
                AdminRefreshToken token = AdminRefreshToken.builder()
                        .encryptedRefreshToken(encrypted)
                        .administrator(admin)
                        .expiresAt((Instant.now().plus(Duration.ofSeconds(refreshTokenExpiresIn)))).build();
                adminRefreshTokenRepo.save(token);
            }

        } else if (roles.contains("user")) {

            Customer customer = repo.findByEmail(email).orElseThrow(()->new EntityNotFoundException("No customer found"));
            UserRefreshToken record=userRefreshTokenRepo.findByCustomer_Id(customer.getId());
            if(record!=null){
                record.setEncryptedRefreshToken(encrypted);
                System.out.println(Instant.now());
                record.setExpiresAt(Instant.now().plus(Duration.ofSeconds(refreshTokenExpiresIn)));
                userRefreshTokenRepo.save(record);
            }
            else{
                userRefreshTokenRepo.save(UserRefreshToken.builder()
                        .customer(customer)
                        .encryptedRefreshToken(encrypted)
                        .expiresAt(Instant.now().plus(Duration.ofSeconds(refreshTokenExpiresIn)))
                        .build());
            }

        }
        if (roles.contains("super_admin")) {
            return;
        }

    }

    private DecodedJWT extractAndDecodeToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        String withoutBearer= authHeader.substring(7);
        return com.auth0.jwt.JWT.decode(withoutBearer);// remove "Bearer "
    }
    public Map<String, Object> refreshAccessToken(String authHeader) {
        DecodedJWT jwt = extractAndDecodeToken(authHeader);
        String email = jwt.getClaim("email").asString();
        List<String> roles = (List<String>) jwt.getClaim("realm_access").asMap().get("roles");
        String decryptedRefreshToken=getDecryptedRefreshToken(email,roles);

        JsonNode response = keyCloakAdminClient.refreshAccessToken(decryptedRefreshToken);

        String newAccessToken = response.get("access_token").asText();
        int expiresIn = response.get("expires_in").asInt();

        // 🔁 (Optional) Update new refresh token if rotation is enabled
        if (response.has("refresh_token")) {
            String newRefreshToken = response.get("refresh_token").asText();
            String newEncrypted = aesEncryptor.aesEncrypt(newRefreshToken);

            if (roles.contains("admin")) {
                Administrator admin = adminRepo.findByEmail(email).orElseThrow();
                admin.getRefreshToken().setEncryptedRefreshToken(newEncrypted);
            } else {
                Customer customer = repo.findByEmail(email).orElseThrow();
                customer.getRefreshToken().setEncryptedRefreshToken(newEncrypted);
            }
        }

        return Map.of(
                "access_token", newAccessToken,
                "expires_in", expiresIn
        );
    }

    private String getDecryptedRefreshToken(String email, List<String> roles) {
        String encrypted;
        if (roles.contains("admin")) {
            Administrator admin = adminRepo.findByEmail(email).orElseThrow();
            encrypted = admin.getRefreshToken().getEncryptedRefreshToken();
        } else {
            Customer customer = repo.findByEmail(email).orElseThrow();
            encrypted = customer.getRefreshToken().getEncryptedRefreshToken();
        }

        return aesEncryptor.aesDecrypt(encrypted);

    }

    public String  fetchDashBoard(String authHeader) {
        DecodedJWT jwt = extractAndDecodeToken(authHeader);
        String email = jwt.getClaim("email").asString();
        List<String> roles = (List<String>) jwt.getClaim("realm_access").asMap().get("roles");
        if (roles.contains("user")) {
           return fetchUserDashBoard(email);
        } else if (roles.contains("admin")) {
            return fetchAdminDashBoard(email);

        }
        return null;
    }

    private String fetchAdminDashBoard(String email) {
        Administrator admin = adminRepo.findByEmail(email).orElseThrow(()->new EntityNotFoundException("No admin found"));
        return "Welcome Admin "+ admin.getFirstName() + " " + admin.getLastName();
    }

    private String fetchUserDashBoard(String email) {
        Customer customer = repo.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("No customer found"));
        return "Welcome User " +customer.getFirstname()+" "+customer.getLastname();

    }
    private String fetchSuperAdminDashBoard(String email) {
        return null;
    }
}
