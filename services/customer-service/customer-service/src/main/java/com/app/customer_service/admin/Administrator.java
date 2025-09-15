package com.app.customer_service.admin;

import com.app.customer_service.refresh_token.AdminRefreshToken;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    @OneToOne(mappedBy = "administrator",cascade = CascadeType.ALL)
    private AdminRefreshToken refreshToken;


}
