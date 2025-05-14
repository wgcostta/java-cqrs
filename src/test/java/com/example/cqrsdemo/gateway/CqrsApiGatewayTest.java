// TEST - 1
package com.example.cqrsdemo.gateway;

import com.example.cqrsdemo.command.api.CreateProductCommand;
import com.example.cqrsdemo.command.api.DeleteProductCommand;
import com.example.cqrsdemo.command.api.UpdateProductCommand;
import com.example.cqrsdemo.command.handler.ProductCommandHandler;
import com.example.cqrsdemo.domain.model.Product;
import com.example.cqrsdemo.query.api.GetAllProductsQuery;
import com.example.cqrsdemo.query.api.GetProductQuery;
import com.example.cqrsdemo.query.handler.ProductQueryHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CqrsApiGatewayTest {

    @Mock
    private ProductCommandHandler commandHandler;

    @Mock
    private ProductQueryHandler queryHandler;

    @InjectMocks
    private CqrsApiGateway apiGateway;

    private CreateProductCommand createCommand;
    private UpdateProductCommand updateCommand;
    private DeleteProductCommand deleteCommand;
    private GetProductQuery getProductQuery;
    private GetAllProductsQuery getAllProductsQuery;
    private Product product;
    private List<Product> productList;

    @BeforeEach
    void setUp() {
        // Setup create command
        createCommand = new CreateProductCommand();
        createCommand.setName("Test Product");
        createCommand.setDescription("Test Description");
        createCommand.setPrice(100.0);
        createCommand.setQuantity(10);

        // Setup update command
        updateCommand = new UpdateProductCommand();
        updateCommand.setId("123");
        updateCommand.setName("Updated Product");
        updateCommand.setDescription("Updated Description");
        updateCommand.setPrice(150.0);
        updateCommand.setQuantity(15);

        // Setup delete command
        deleteCommand = new DeleteProductCommand();
        deleteCommand.setId("123");

        // Setup getProduct query
        getProductQuery = new GetProductQuery("123");

        // Setup getAllProducts query
        getAllProductsQuery = new GetAllProductsQuery();

        // Setup product
        product = new Product();
        product.setId("123");
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(100.0);
        product.setQuantity(10);

        // Setup product list
        Product product2 = new Product();
        product2.setId("456");
        product2.setName("Another Product");
        productList = Arrays.asList(product, product2);
    }

    @Test
    void sendCommand_CreateProductCommand_ShouldDelegateToCommandHandler() {
        // Arrange
        when(commandHandler.handleCreateProductCommand(any(CreateProductCommand.class))).thenReturn("123");

        // Act
        String productId = apiGateway.sendCommand(createCommand);

        // Assert
        assertEquals("123", productId);
        verify(commandHandler, times(1)).handleCreateProductCommand(createCommand);
    }

    @Test
    void sendCommand_UpdateProductCommand_ShouldDelegateToCommandHandler() {
        // Arrange
        doNothing().when(commandHandler).handleUpdateProductCommand(any(UpdateProductCommand.class));

        // Act
        apiGateway.sendCommand(updateCommand);

        // Assert
        verify(commandHandler, times(1)).handleUpdateProductCommand(updateCommand);
    }

    @Test
    void sendCommand_DeleteProductCommand_ShouldDelegateToCommandHandler() {
        // Arrange
        doNothing().when(commandHandler).handleDeleteProductCommand(any(DeleteProductCommand.class));

        // Act
        apiGateway.sendCommand(deleteCommand);

        // Assert
        verify(commandHandler, times(1)).handleDeleteProductCommand(deleteCommand);
    }

    @Test
    void sendQuery_GetProductQuery_ShouldDelegateToQueryHandler() {
        // Arrange
        when(queryHandler.handle(any(GetProductQuery.class))).thenReturn(product);

        // Act
        Product result = apiGateway.sendQuery(getProductQuery);

        // Assert
        assertNotNull(result);
        assertEquals("123", result.getId());
        verify(queryHandler, times(1)).handle(getProductQuery);
    }

    @Test
    void sendQuery_GetAllProductsQuery_ShouldDelegateToQueryHandler() {
        // Arrange
        when(queryHandler.handle(any(GetAllProductsQuery.class))).thenReturn(productList);

        // Act
        List<Product> result = apiGateway.sendQuery(getAllProductsQuery);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(queryHandler, times(1)).handle(getAllProductsQuery);
    }
}
