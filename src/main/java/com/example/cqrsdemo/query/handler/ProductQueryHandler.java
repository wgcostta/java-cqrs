package com.example.cqrsdemo.query.handler;

import com.example.cqrsdemo.domain.model.Product;
import com.example.cqrsdemo.domain.repository.ProductRepository;
import com.example.cqrsdemo.query.api.GetAllProductsQuery;
import com.example.cqrsdemo.query.api.GetProductQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductQueryHandler {

    private final ProductRepository productRepository;

    @Autowired
    public ProductQueryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product handle(GetProductQuery query) {
        return productRepository.findById(query.getId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + query.getId()));
    }

    public List<Product> handle(GetAllProductsQuery query) {
        return productRepository.findAll();
    }
}
