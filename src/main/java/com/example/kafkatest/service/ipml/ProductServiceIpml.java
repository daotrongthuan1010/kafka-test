package com.example.kafkatest.service.ipml;

import com.example.kafkatest.dto.CreateProductRequest;
import com.example.kafkatest.entity.Product;
import com.example.kafkatest.repository.ProductRepository;
import com.example.kafkatest.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceIpml implements ProductService {

    private final ProductRepository productRepository;

    private final KafkaTemplate<String, CreateProductRequest> kafkaTemplate;


    @Override
    public void createProduct(CreateProductRequest request) {

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .status("Created")
                .build();

        productRepository.save(product);

        kafkaTemplate.send("product-topic", request);

    }

//    public void findAll(List<Product> productList) {
//        for (Product product : productList) {
//            ProducerRecord<String, Product> record = new ProducerRecord<>("product-topic", product);
//            kafkaTemplate.send((Message<?>) record);
//        }
//        kafkaTemplate.flush();
//    }
//
//    private List<Product> productList = new ArrayList<>();
//
//    @KafkaListener(topics = "product-topic", groupId = "product-group")
//    public void consumeFromKafka(Product product) {
//        productList.add(product);
//    }
//
//    public List<Product> getAllProducts() {
//        return productList;
//    }


}
