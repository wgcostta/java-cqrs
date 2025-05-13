package com.example.cqrsdemo.event;

import com.example.cqrsdemo.domain.model.Product;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProductCreatedEvent extends ApplicationEvent {
    private final Product product;

    public ProductCreatedEvent(Product product) {
        super(product);
        this.product = product;
    }
}
