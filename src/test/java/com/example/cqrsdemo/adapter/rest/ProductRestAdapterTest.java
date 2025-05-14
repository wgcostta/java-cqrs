
package com.example.cqrsdemo.adapter.rest;

import com.example.cqrsdemo.command.api.CreateProductCommand;
import com.example.cqrsdemo.command.api.UpdateProductCommand;
import com.example.cqrsdemo.domain.model.Product;
import com.example.cqrsdemo.gateway.CqrsApiGateway;
import com.example.cqrsdemo.query.api.GetAllProductsQuery;
import com.example.cqrsdemo.query.api.GetProductQuery;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductRestAdapterTest {

    private MockMvc mockMvc;

    @Mock
    private CqrsApiGateway apiGateway;

    @InjectMocks
    private ProductRestAdapter restAdapter;

    private ObjectMapper objectMapper;
    private Product product;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restAdapter).build();
        objectMapper = new ObjectMapper();

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
        products = Arrays.asList(product, product2);
    }

    @Test
    void createProduct_ShouldReturnCreatedStatusAndProductId() throws Exception {
        // Arrange
        CreateProductCommand command = new CreateProductCommand();
        command.setName("Test Product");
        command.setDescription("Test Description");
        command.setPrice(100.0);
        command.setQuantity(10);

        when(apiGateway.sendCommand(any(CreateProductCommand.class))).thenReturn("123");

        // Act & Assert
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(content().string("123"));

        verify(apiGateway, times(1)).sendCommand(any(CreateProductCommand.class));
    }

    @Test
    void updateProduct_ShouldReturnOkStatus() throws Exception {
        // Arrange
        UpdateProductCommand command = new UpdateProductCommand();
        command.setName("Updated Product");
        command.setDescription("Updated Description");
        command.setPrice(150.0);
        command.setQuantity(15);

        doNothing().when(apiGateway).sendCommand(any(UpdateProductCommand.class));

        // Act & Assert
        mockMvc.perform(put("/api/products/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk());

        verify(apiGateway, times(1)).sendCommand(any(UpdateProductCommand.class));
    }

    @Test
    void deleteProduct_ShouldReturnNoContentStatus() throws Exception {
        // Arrange
        doNothing().when(apiGateway).sendCommand(any());

        // Act & Assert
        mockMvc.perform(delete("/api/products/123"))
                .andExpect(status().isNoContent());

        verify(apiGateway, times(1)).sendCommand(any());
    }

    @Test
    void getProduct_ShouldReturnProductAndOkStatus() throws Exception {
        // Arrange
        when(apiGateway.sendQuery(any(GetProductQuery.class))).thenReturn(product);

        // Act & Assert
        mockMvc.perform(get("/api/products/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(apiGateway, times(1)).sendQuery(any(GetProductQuery.class));
    }

    @Test
    void getAllProducts_ShouldReturnProductListAndOkStatus() throws Exception {
        // Arrange
        when(apiGateway.sendQuery(any(GetAllProductsQuery.class))).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("123"))
                .andExpect(jsonPath("$[1].id").value("456"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(apiGateway, times(1)).sendQuery(any(GetAllProductsQuery.class));
    }
}