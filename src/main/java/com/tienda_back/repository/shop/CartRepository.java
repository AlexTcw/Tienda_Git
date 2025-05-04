package com.tienda_back.repository.shop;

import com.tienda_back.model.entity.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query(value = """
        SELECT\s
            c.cart_id,
            json_agg(p.product_id) AS product_ids,
            c.cart_date,
            c.status
        FROM cart c
        JOIN cart_product cp ON cp.cart_id = c.cart_id
        JOIN product p ON cp.product_id = p.product_id
        WHERE c.cart_id = :cart_id
        GROUP BY c.cart_id, c.cart_date, c.status
       \s""", nativeQuery = true)
    List<Object[]> getCartByCartId(@Param("cart_id") Long cartId);

    @Query(value = """
            SELECT\s
                        c.cart_id,
                        json_agg(p.product_id) AS product_ids,
                        c.cart_date,
                        c.status
                    FROM cart c
                    JOIN cart_product cp ON cp.cart_id = c.cart_id
                    JOIN product p ON cp.product_id = p.product_id
                    WHERE c.user_id = :user_id
                    GROUP BY c.cart_id, c.cart_date, c.status""", nativeQuery = true)
    List<Object[]> getCartByUserId(@Param("user_id") Long userId);


    @Query(value = """
            SELECT\s
                        c.cart_id,
                        json_agg(p.product_id) AS product_ids,
                        c.cart_date,
                        c.status
                    FROM cart c
                    JOIN cart_product cp ON cp.cart_id = c.cart_id
                    JOIN product p ON cp.product_id = p.product_id
                    WHERE c.status = CAST(:cart_status AS cart_status)
                    GROUP BY c.cart_id, c.cart_date, c.status""", nativeQuery = true)
    List<Object[]> getCartByCartStatus(@Param("cart_status") String cartStatus);

    @Query(value = """
        SELECT\s
            c.cart_id,
            json_agg(p.product_id) AS product_ids,
            c.cart_date,
            c.status
        FROM cart c
        JOIN cart_product cp ON cp.cart_id = c.cart_id
        JOIN product p ON cp.product_id = p.product_id
        WHERE (
            :key IS NULL OR (
                (
                    UPPER(TRANSLATE(p.name, 'ñáéíóúàèìòùãõâêîôûäëïöüçÑÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'naeiouaeiouaoaeioucaeiouNAEIOUAEIOUAOAEIOU')) 
                    LIKE '%' || UPPER(TRANSLATE(REPLACE(CAST(:key AS TEXT), '-', ''), 'ñáéíóúàèìòùãõâêîôûäëïöüçÑÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'naeiouaeiouaoaeioucaeiouNAEIOUAEIOUAOAEIOUCAEIOU')) || '%'
                )
                OR c.cart_id = CAST(:key AS BIGINT)
            )
        )
        AND (
            (:start_date IS NULL OR c.cart_date >= CAST(:start_date AS TIMESTAMP))
            AND (:end_date IS NULL OR c.cart_date <= CAST(:end_date AS TIMESTAMP))
        )
        GROUP BY c.cart_id, c.cart_date, c.status
        ORDER BY c.cart_date DESC
    """, nativeQuery = true)
    List<Object[]> findCartsByKeyword(@Param("key") String keyword,
                                      @Param("start_date") String startDate,
                                      @Param("end_date") String endDate);

    boolean existsCartByCartId(long cartId);

    @Query(value = """
            select c.*
            from cart c
            where c.cart_id =:cart_id
            """,nativeQuery = true)
    Cart findCartByCartId(@Param("cart_id") long cartId);

    void deleteCartsByCartId(long cartId);
}
