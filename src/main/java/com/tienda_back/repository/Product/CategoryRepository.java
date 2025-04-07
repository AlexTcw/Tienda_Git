package com.tienda_back.repository.Product;

import com.tienda_back.model.entity.Products.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Object findCategoryByCategoryId(Long categoryId);
}
