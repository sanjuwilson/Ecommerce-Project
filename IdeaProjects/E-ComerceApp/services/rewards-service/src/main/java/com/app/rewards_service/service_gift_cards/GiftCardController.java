package com.app.rewards_service.service_gift_cards;

import lombok.RequiredArgsConstructor;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/gift")
@RequiredArgsConstructor
public class GiftCardController {
    private final GiftCardService giftCardService;
    @PostMapping
    public ResponseEntity<Integer> purchaseGiftCards(@RequestBody GiftCardPurchaseRequest request) {
        return ResponseEntity.ok(giftCardService.saveGiftCard(request));
    }
}
