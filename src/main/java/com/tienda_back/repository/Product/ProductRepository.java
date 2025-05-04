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
                SELECT\s
                	   p.product_id, p."name",
                       p.sku, p.description, p.price,
                	   p.brand, p.stock, string_agg(c."name", ', ') AS categories
                FROM product p
                INNER JOIN category_product cp ON cp.product_id = p.product_id
                INNER JOIN category c ON c.category_id = cp.category_id
                GROUP BY\s
                    p.product_id, p."name", p.sku, p.description, p.price, p.brand, p.stock
            """, nativeQuery = true)
    List<String[]> findAllProducts();

    @Query(value = """
                SELECT\s
                	   p.product_id, p."name",
                       p.sku, p.description, p.price,
                	   p.brand, p.stock, string_agg(c."name", ', ') AS categories
                FROM product p
                INNER JOIN category_product cp ON cp.product_id = p.product_id
                INNER JOIN category c ON c.category_id = cp.category_id
                where p.product_id = :product_id
                GROUP BY\s
                    p.product_id, p."name", p.sku, p.description, p.price, p.brand, p.stock
            """, nativeQuery = true)
    List<String[]> findProductById(@Param("product_id") Long id);

    @Query(value = """
                SELECT\s
                	   p.product_id, p."name",
                       p.sku, p.description, p.price,
                	   p.brand, p.stock, string_agg(c."name", ', ') AS categories
                FROM product p
                INNER JOIN category_product cp ON cp.product_id = p.product_id
                INNER JOIN category c ON c.category_id = cp.category_id
                where c.category_id = :category_id
                GROUP BY\s
                    p.product_id, p."name", p.sku, p.description, p.price, p.brand, p.stock
            """, nativeQuery = true)
    List<String[]> findProductsByCategoryId(@Param("category_id") Long id);

    @Query(value = """
            select p.brand
            from product p\s
            inner join category_product cp on cp.product_id = p.product_id
            inner join category c on c.category_id = cp.category_id
            """, nativeQuery = true)
    List<String> findAllProductBrand();

    @Query(value = """
                SELECT\s
                	   p.product_id, p."name",
                       p.sku, p.description, p.price,
                	   p.brand, p.stock, string_agg(c."name", ', ') AS categories
                FROM product p
                INNER JOIN category_product cp ON cp.product_id = p.product_id
                INNER JOIN category c ON c.category_id = cp.category_id
                where p.brand = :brand_name
                GROUP BY\s
                    p.product_id, p."name", p.sku, p.description, p.price, p.brand, p.stock
            """, nativeQuery = true)
    List<String[]> findProductsByBrandName(@Param("brand_name") String name);

    @Query(value = """
                SELECT\s
                	   p.product_id, p."name",
                       p.sku, p.description, p.price,
                	   p.brand, p.stock, string_agg(c."name", ', ') AS categories
                FROM product p
                INNER JOIN category_product cp ON cp.product_id = p.product_id
                INNER JOIN category c ON c.category_id = cp.category_id
                where p.sku = :product_sku
                GROUP BY\s
                    p.product_id, p."name", p.sku, p.description, p.price, p.brand, p.stock
            """, nativeQuery = true)
    List<String[]> findProductsByProductSku(@Param("product_sku") String name);

    @Query(value = """
                SELECT\s
                	   p.product_id, p."name",
                       p.sku, p.description, p.price,
                	   p.brand, p.stock, string_agg(c."name", ', ') AS categories
                FROM product p
                INNER JOIN category_product cp ON cp.product_id = p.product_id
                INNER JOIN category c ON c.category_id = cp.category_id
                where p.stock = 0
                GROUP BY\s
                    p.product_id, p."name", p.sku, p.description, p.price, p.brand, p.stock
            """, nativeQuery = true)
    List<String[]> findProductsOutOfStock();

    @Query(value = """
                SELECT
                            p.product_id,p."name",
                            p.sku, p.description, p.price,
                            p.brand, p.stock,string_agg(c."name", ', ') AS category_names
                        FROM product p
                        INNER JOIN category_product cp ON cp.product_id = p.product_id
                        INNER JOIN category c ON c.category_id = cp.category_id
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
                    OR (CAST(:key AS TEXT) ~ '^\\d+$' 
                    AND p.product_id = CAST(:key AS NUMERIC))
                ))
            GROUP BY p.product_id, p."name", p.sku, p.description, p.price, p.brand, p.stock
            """, nativeQuery = true)
    List<String[]> findProductsByKeyWord(@Param("key") String keyword);

    @Query(value = """
                SELECT
                            p.product_id,
                            p."name",
                            p.sku,
                            p.description,
                            p.price,
                            string_agg(c."name", ', ') AS category_names,
                            p.brand,
                            p.stock
                        FROM product p
                        INNER JOIN category_product cp ON cp.product_id = p.product_id
                        INNER JOIN category c ON c.category_id = cp.category_id
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
                    OR (CAST(:key AS TEXT) ~ '^\\d+$' 
                    AND p.product_id = CAST(:key AS NUMERIC))
                ))
            GROUP BY p.product_id, p."name", p.sku, p.description, p.price, p.brand, p.stock
            """, nativeQuery = true)
    Page<String[]> findProductsByKeyWordPage(@Param("key") String keyword, Pageable pageable);




    @Query(value = """
            SELECT COUNT(*) FROM(
                            SELECT DISTINCT p.brand FROM product p
                            where p.brand = :brand
                        ) as "p*";
            """, nativeQuery = true)
    int countBrandByName(@Param("brand") String brandName);

    Product findProductByProductId(Long productId);

    boolean existsProductByProductId(Long productId);

    @Query(value = """
            SELECT\s
                p.product_id,
                p."name",
                p.sku,
                p.price,
                p.brand,
                cp.quantity
            FROM cart c
            JOIN cart_product cp ON cp.cart_id = c.cart_id
            JOIN product p ON cp.product_id = p.product_id
            WHERE c.cart_id = :cart_id""",nativeQuery = true)
    List<Object []> findProductsByCartId(@Param("cart_id") Long cartId);
}
