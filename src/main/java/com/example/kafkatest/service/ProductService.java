package com.example.kafkatest.service;

import com.example.kafkatest.dto.CreateProductRequest;
import com.example.kafkatest.entity.Product;

import java.util.List;

public interface ProductService {

    void createProduct(CreateProductRequest request) ;

//    List<Product> getAllProducts();
}
