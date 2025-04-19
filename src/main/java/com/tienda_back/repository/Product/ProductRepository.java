package com.tienda_back.repository.Product;

import com.tienda_back.model.entity.Products.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
            select p.product_id,p."name" ,p.sku,p.description,p.price,c."name",p.brand, i.stock
            from product p\s
            inner join category_product cp on cp.product_id = p.product_id
            inner join category c on c.category_id = cp.category_id
            inner join inventory i on i.product_id = p.product_id
        """, nativeQuery = true)
    List<String[]> findAllProducts();

    @Query(value = """
            select p.product_id,p."name" ,p.sku,p.description,p.price,c."name",p.brand, i.stock
            from product p\s
            inner join category_product cp on cp.product_id = p.product_id
            inner join category c on c.category_id = cp.category_id
            inner join inventory i on i.product_id = p.product_id
            where p.product_id = :product_id
        """, nativeQuery = true)
    List<String[]> findProductById(@Param("product_id") Long id);

    @Query(value = """
            select p.product_id,p."name" ,p.sku,p.description,p.price,c."name",p.brand, i.stock
            from product p\s
            inner join category_product cp on cp.product_id = p.product_id
            inner join category c on c.category_id = cp.category_id
            inner join inventory i on i.product_id = p.product_id
            where c.category_id = :category_id
        """, nativeQuery = true)
    List<String[]> findProductsByCategoryId(@Param("category_id") Long id);

    @Query(value = """
            select p.brand
            from product p\s
            inner join category_product cp on cp.product_id = p.product_id
            inner join category c on c.category_id = cp.category_id
            inner join inventory i on i.product_id = p.product_id
            """,nativeQuery = true)
    List<String> findAllProductBrand();

    @Query(value = """
            select p.product_id,p."name" ,p.sku,p.description,p.price,c."name",p.brand, i.stock
            from product p\s
            inner join category_product cp on cp.product_id = p.product_id
            inner join category c on c.category_id = cp.category_id
            inner join inventory i on i.product_id = p.product_id
            where p.brand = :brand_name
        """, nativeQuery = true)
    List<String[]> findProductsByBrandName(@Param("brand_name") String name);

    @Query(value = """
            select p.product_id,p."name" ,p.sku,p.description,p.price,c."name",p.brand, i.stock
            from product p\s
            inner join category_product cp on cp.product_id = p.product_id
            inner join category c on c.category_id = cp.category_id
            inner join inventory i on i.product_id = p.product_id
            where p.sku = :product_sku
        """, nativeQuery = true)
    List<String[]> findProductsByProductSku(@Param("product_sku") String name);

    @Query(value = """
            select p.product_id,p."name" ,p.sku,p.description,p.price,c."name",p.brand, i.stock
            from product p\s
            inner join category_product cp on cp.product_id = p.product_id
            inner join category c on c.category_id = cp.category_id
            inner join inventory i on i.product_id = p.product_id
            where i.stock = 0
        """, nativeQuery = true)
    List<String[]> findProductsOutOfStock();

    @Query(value = """
            SELECT
                        p.product_id,
                        p."name",
                        p.sku,
                        p.description,
                        p.price,
                        c."name" AS category_name,
                        p.brand,
                        i.stock
                    FROM product p
                    INNER JOIN category_product cp ON cp.product_id = p.product_id
                    INNER JOIN category c ON c.category_id = cp.category_id
                    INNER JOIN inventory i ON i.product_id = p.product_id
                    WHERE (:key is null OR (
                UPPER(TRANSLATE(p."name", 'ñáéíóúàèìòùãõâêîôûäëïöüçÑÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ',
                                'naeiouaeiouaoaeioucaeiouNAEIOUAEIOUAOAEIOUCAEIOU'))
                    LIKE '%' || UPPER(TRANSLATE(REPLACE(CAST(:key AS TEXT), '-', ''),
                                              'ñáéíóúàèìòùãõâêîôûäëïöüçÑÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ',
                                              'naeiouaeiouaoaeioucaeiouNAEIOUAEIOUAOAEIOUCAEIOU')) || '%'
                OR UPPER(TRANSLATE(p.brand, 'ñáéíóúàèìòùãõâêîôûäëïöüçÑÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ',
                                   'naeiouaeiouaoaeioucaeiouNAEIOUAEIOUAOAEIOUCAEIOU'))
                    LIKE '%' || UPPER(TRANSLATE(REPLACE(CAST(:key AS TEXT), '-', ''),
                                              'ñáéíóúàèìòùãõâêîôûäëïöüçÑÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ',
                                              'naeiouaeiouaoaeioucaeiouNAEIOUAEIOUAOAEIOUCAEIOU')) || '%'
                OR UPPER(TRANSLATE(c."name", 'ñáéíóúàèìòùãõâêîôûäëïöüçÑÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ',
                                   'naeiouaeiouaoaeioucaeiouNAEIOUAEIOUAOAEIOUCAEIOU'))
                    LIKE '%' || UPPER(TRANSLATE(REPLACE(CAST(:key AS TEXT), '-', ''),
                                              'ñáéíóúàèìòùãõâêîôûäëïöüçÑÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ',
                                              'naeiouaeiouaoaeioucaeiouNAEIOUAEIOUAOAEIOUCAEIOU')) || '%'
                OR REPLACE(p.sku, '-', '') LIKE '%' || REPLACE(CAST(:key AS TEXT), '-', '') || '%'
                OR (CAST(:key AS TEXT) ~ '^\\d+$'\s
                AND p.product_id = CAST(:key AS NUMERIC))
            ))
        """,nativeQuery = true)
    Page<String []> findProductsByKeyWord(@Param("key") String keyword, Pageable pageable);
}
