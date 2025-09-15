package com.app.rewards_service.service_gift_cards;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GiftCardRepository extends JpaRepository<GiftCard, Integer> {
    GiftCard findByCode(String giftCardCode);
}
