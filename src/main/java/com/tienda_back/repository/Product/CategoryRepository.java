package com.tienda_back.repository.Product;

import com.tienda_back.model.entity.Products.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = """
            select c.*
            from category c
            """, nativeQuery = true)
    List<Category> findAllCategories();

    Category findCategoryByCategoryId(Long categoryId);

    boolean existsCategoryByCategoryId(Long categoryId);
}
