package com.example.stockservice.stock.db.repository;

import com.example.stockservice.stock.db.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link Product} entities.
 * @see Product
 * @author DaoThuan1010
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
