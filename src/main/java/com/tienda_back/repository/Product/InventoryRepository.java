package com.tienda_back.repository.Product;

import com.tienda_back.model.entity.Products.Inventory;
import com.tienda_back.model.entity.Products.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Object findInventoryByProduct(Product product);
}
