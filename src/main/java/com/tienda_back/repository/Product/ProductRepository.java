package com.tienda_back.repository.Product;

import com.tienda_back.model.entity.Products.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
