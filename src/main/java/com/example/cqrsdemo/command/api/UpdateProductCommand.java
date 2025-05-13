package com.example.cqrsdemo.command.api;

import lombok.Data;

@Data
public class UpdateProductCommand {
    private String id;
    private String name;
    private String description;
    private double price;
    private int quantity;
}