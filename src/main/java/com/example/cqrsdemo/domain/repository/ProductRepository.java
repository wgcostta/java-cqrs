package com.example.cqrsdemo.domain.repository;

import com.example.cqrsdemo.domain.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}