package com.app.rewards_service.kafka_config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {
    @Bean
    public NewTopic createTopic() {
        return TopicBuilder.name("points_topic").build();
    }
    @Bean
    public NewTopic ordersTopic() {
        return TopicBuilder.name("order_topic").build();
    }
    @Bean
    public NewTopic cartTopic() {
        return TopicBuilder.name("cart_topic").build();
    }
    @Bean
    public NewTopic ordersConfirmationTopic() {
        return TopicBuilder.name("order_topic_conf").build();
    }
    @Bean
    public NewTopic paymentConfirmationTopic() {
        return TopicBuilder.name("payment_topic").build();
    }
}
