package com.example.cqrsdemo.gateway;

import com.example.cqrsdemo.command.api.CreateProductCommand;
import com.example.cqrsdemo.command.api.DeleteProductCommand;
import com.example.cqrsdemo.command.api.UpdateProductCommand;
import com.example.cqrsdemo.command.handler.ProductCommandHandler;
import com.example.cqrsdemo.domain.model.Product;
import com.example.cqrsdemo.query.api.GetAllProductsQuery;
import com.example.cqrsdemo.query.api.GetProductQuery;
import com.example.cqrsdemo.query.handler.ProductQueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Gateway central para processamento de comandos e consultas CQRS
 * Permite comunicação direta entre microsserviços sem exposição REST
 */
@Component
public class CqrsApiGateway {

    private final ProductCommandHandler commandHandler;
    private final ProductQueryHandler queryHandler;

    @Autowired
    public CqrsApiGateway(ProductCommandHandler commandHandler, ProductQueryHandler queryHandler) {
        this.commandHandler = commandHandler;
        this.queryHandler = queryHandler;
    }

    // Métodos de comando
    public String sendCommand(CreateProductCommand command) {
        return commandHandler.handleCreateProductCommand(command);
    }

    public void sendCommand(UpdateProductCommand command) {
        commandHandler.handleUpdateProductCommand(command);
    }

    public void sendCommand(DeleteProductCommand command) {
        commandHandler.handleDeleteProductCommand(command);
    }

    // Métodos de consulta
    public Product sendQuery(GetProductQuery query) {
        return queryHandler.handle(query);
    }

    public List<Product> sendQuery(GetAllProductsQuery query) {
        return queryHandler.handle(query);
    }
}