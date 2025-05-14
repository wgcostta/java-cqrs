package com.example.cqrsdemo.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProductDeletedEvent extends ApplicationEvent {
    private final String productId;

    public ProductDeletedEvent(String productId) {
        super(productId);
        this.productId = productId;
    }
}
