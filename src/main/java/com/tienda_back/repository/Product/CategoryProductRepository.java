package com.tienda_back.repository.Product;

import com.tienda_back.model.entity.Products.CategoryProduct;
import com.tienda_back.model.entity.Products.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryProductRepository extends JpaRepository<CategoryProduct, Long> {
    List<CategoryProduct> findCategoryProductByProduct(Product product);
}
