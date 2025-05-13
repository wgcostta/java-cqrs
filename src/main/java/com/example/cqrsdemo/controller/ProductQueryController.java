package com.example.cqrsdemo.controller;

import com.example.cqrsdemo.domain.model.Product;
import com.example.cqrsdemo.query.api.GetAllProductsQuery;
import com.example.cqrsdemo.query.api.GetProductQuery;
import com.example.cqrsdemo.query.handler.ProductQueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products/queries")
public class ProductQueryController {

    private final ProductQueryHandler queryHandler;

    @Autowired
    public ProductQueryController(ProductQueryHandler queryHandler) {
        this.queryHandler = queryHandler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {
        GetProductQuery query = new GetProductQuery(id);
        Product product = queryHandler.handle(query);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        GetAllProductsQuery query = new GetAllProductsQuery();
        List<Product> products = queryHandler.handle(query);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
