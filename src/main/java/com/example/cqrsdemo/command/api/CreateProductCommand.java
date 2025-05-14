package com.example.cqrsdemo.command.api;

import lombok.Data;

@Data
public class CreateProductCommand {
    private String name;
    private String description;
    private double price;
    private int quantity;
}