package com.example.cqrsdemo.event;

import com.example.cqrsdemo.domain.model.Product;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProductUpdatedEvent extends ApplicationEvent {
    private final Product product;

    public ProductUpdatedEvent(Product product) {
        super(product);
        this.product = product;
    }
}
