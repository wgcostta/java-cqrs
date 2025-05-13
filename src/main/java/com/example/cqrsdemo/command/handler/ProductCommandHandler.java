package com.example.cqrsdemo.command.handler;

import com.example.cqrsdemo.command.api.CreateProductCommand;
import com.example.cqrsdemo.command.api.DeleteProductCommand;
import com.example.cqrsdemo.command.api.UpdateProductCommand;
import com.example.cqrsdemo.domain.model.Product;
import com.example.cqrsdemo.domain.repository.ProductRepository;
import com.example.cqrsdemo.event.ProductCreatedEvent;
import com.example.cqrsdemo.event.ProductDeletedEvent;
import com.example.cqrsdemo.event.ProductUpdatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductCommandHandler {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ProductCommandHandler(ProductRepository productRepository, ApplicationEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    public String handleCreateProductCommand(CreateProductCommand command) {
        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName(command.getName());
        product.setDescription(command.getDescription());
        product.setPrice(command.getPrice());
        product.setQuantity(command.getQuantity());

        productRepository.save(product);

        // Publicar evento
        eventPublisher.publishEvent(new ProductCreatedEvent(product));

        return product.getId();
    }

    public void handleUpdateProductCommand(UpdateProductCommand command) {
        Product product = productRepository.findById(command.getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + command.getId()));

        product.setName(command.getName());
        product.setDescription(command.getDescription());
        product.setPrice(command.getPrice());
        product.setQuantity(command.getQuantity());

        productRepository.save(product);

        // Publicar evento
        eventPublisher.publishEvent(new ProductUpdatedEvent(product));
    }

    public void handleDeleteProductCommand(DeleteProductCommand command) {
        Product product = productRepository.findById(command.getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + command.getId()));

        productRepository.delete(product);

        // Publicar evento
        eventPublisher.publishEvent(new ProductDeletedEvent(command.getId()));
    }
}