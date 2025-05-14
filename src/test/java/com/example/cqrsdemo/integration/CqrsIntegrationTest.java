package com.example.cqrsdemo.integration;

import com.example.cqrsdemo.command.api.CreateProductCommand;
import com.example.cqrsdemo.command.api.UpdateProductCommand;
import com.example.cqrsdemo.domain.model.Product;
import com.example.cqrsdemo.domain.repository.ProductRepository;
import com.example.cqrsdemo.gateway.CqrsApiGateway;
import com.example.cqrsdemo.query.api.GetAllProductsQuery;
import com.example.cqrsdemo.query.api.GetProductQuery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CqrsIntegrationTest {

    @Autowired
    private CqrsApiGateway apiGateway;

    @Autowired
    private ProductRepository productRepository;

    private String productId;

    @BeforeEach
    void setUp() {
        // Limpar o banco de dados antes de cada teste
        productRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        // Limpar o banco de dados após cada teste
        productRepository.deleteAll();
    }

    @Test
    void testFullCqrsFlow() {
        // 1. Criar um produto via comando
        CreateProductCommand createCommand = new CreateProductCommand();
        createCommand.setName("Integration Test Product");
        createCommand.setDescription("Product created during integration test");
        createCommand.setPrice(99.99);
        createCommand.setQuantity(5);

        productId = apiGateway.sendCommand(createCommand);
        assertNotNull(productId);
        assertTrue(productId.length() > 0);

        // 2. Verificar se o produto foi criado corretamente via consulta
        GetProductQuery getQuery = new GetProductQuery(productId);
        Product product = apiGateway.sendQuery(getQuery);

        assertNotNull(product);
        assertEquals("Integration Test Product", product.getName());
        assertEquals("Product created during integration test", product.getDescription());
        assertEquals(99.99, product.getPrice());
        assertEquals(5, product.getQuantity());

        // 3. Atualizar o produto via comando
        UpdateProductCommand updateCommand = new UpdateProductCommand();
        updateCommand.setId(productId);
        updateCommand.setName("Updated Integration Test Product");
        updateCommand.setDescription("Updated product description");
        updateCommand.setPrice(149.99);
        updateCommand.setQuantity(10);

        apiGateway.sendCommand(updateCommand);

        // 4. Verificar se o produto foi atualizado corretamente via consulta
        product = apiGateway.sendQuery(getQuery);

        assertNotNull(product);
        assertEquals("Updated Integration Test Product", product.getName());
        assertEquals("Updated product description", product.getDescription());
        assertEquals(149.99, product.getPrice());
        assertEquals(10, product.getQuantity());

        // 5. Verificar listagem de todos os produtos
        GetAllProductsQuery getAllQuery = new GetAllProductsQuery();
        List<Product> products = apiGateway.sendQuery(getAllQuery);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(productId, products.get(0).getId());
    }
}
