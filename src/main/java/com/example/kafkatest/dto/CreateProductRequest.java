package com.example.kafkatest.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateProductRequest {

    private final String name;

    private final Double price;

    private final String status;

}
