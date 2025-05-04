package com.tienda_back.service.cart;

import com.tienda_back.model.dto.consume.ConsumeJsonCart;
import com.tienda_back.model.dto.consume.ConsumeJsonProductCart;
import com.tienda_back.model.dto.consume.ConsumeJsonProducts;
import com.tienda_back.model.dto.response.ResponseJsonCart;
import com.tienda_back.model.dto.response.ResponseJsonCarts;
import com.tienda_back.model.dto.response.ResponseJsonString;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

public interface CartService {
    @Transactional
    ResponseJsonString sellProducts(ConsumeJsonProducts consume);

    ResponseJsonCarts findCartsByCartStatus(String status);

    ResponseJsonCarts findCartsByUserId(long id);

    ResponseJsonCart findCartById(Long id);

    ResponseJsonCart createCart(ConsumeJsonCart consume);

    ResponseJsonString cancelCartByCartId(Long id);

    ResponseJsonCart updateCart(ConsumeJsonProductCart consume);

    ResponseJsonCarts findCartsByKeyword(String keyword, LocalDateTime startDate, LocalDateTime endDate);
}
