package com.example.cqrsdemo.domain.repository;

import com.example.cqrsdemo.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
