package com.example.cqrsdemo.adapter.rest;

import com.example.cqrsdemo.command.api.CreateProductCommand;
import com.example.cqrsdemo.command.api.DeleteProductCommand;
import com.example.cqrsdemo.command.api.UpdateProductCommand;
import com.example.cqrsdemo.domain.model.Product;
import com.example.cqrsdemo.gateway.CqrsApiGateway;
import com.example.cqrsdemo.query.api.GetAllProductsQuery;
import com.example.cqrsdemo.query.api.GetProductQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Adaptador REST opcional que pode ser ativado via propriedade de configuração
 * Apenas para demonstração/testes - em produção os microsserviços se comunicariam
 * diretamente via CqrsApiGateway
 */
@RestController
@RequestMapping("/api/products")
@ConditionalOnProperty(name = "cqrs.rest.enabled", havingValue = "true", matchIfMissing = false)
public class ProductRestAdapter {

    private final CqrsApiGateway apiGateway;

    @Autowired
    public ProductRestAdapter(CqrsApiGateway apiGateway) {
        this.apiGateway = apiGateway;
    }

    // Endpoints de comando
    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody CreateProductCommand command) {
        String productId = apiGateway.sendCommand(command);
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable String id, @RequestBody UpdateProductCommand command) {
        command.setId(id);
        apiGateway.sendCommand(command);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        DeleteProductCommand command = new DeleteProductCommand();
        command.setId(id);
        apiGateway.sendCommand(command);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoints de consulta
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable String id) {
        GetProductQuery query = new GetProductQuery(id);
        Product product = apiGateway.sendQuery(query);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        GetAllProductsQuery query = new GetAllProductsQuery();
        List<Product> products = apiGateway.sendQuery(query);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
