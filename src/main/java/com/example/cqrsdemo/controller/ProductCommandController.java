package com.example.cqrsdemo.controller;

import com.example.cqrsdemo.command.api.CreateProductCommand;
import com.example.cqrsdemo.command.api.DeleteProductCommand;
import com.example.cqrsdemo.command.api.UpdateProductCommand;
import com.example.cqrsdemo.command.handler.ProductCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/commands")
public class ProductCommandController {

    private final ProductCommandHandler commandHandler;

    @Autowired
    public ProductCommandController(ProductCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody CreateProductCommand command) {
        String productId = commandHandler.handleCreateProductCommand(command);
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable String id, @RequestBody UpdateProductCommand command) {
        command.setId(id);
        commandHandler.handleUpdateProductCommand(command);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        DeleteProductCommand command = new DeleteProductCommand();
        command.setId(id);
        commandHandler.handleDeleteProductCommand(command);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}