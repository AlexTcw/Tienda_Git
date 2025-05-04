package com.tienda_back.repository.shop;

import com.tienda_back.model.entity.cart.Cart;
import com.tienda_back.model.entity.cart.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface CartProductRepository extends JpaRepository<CartProduct, Integer> {
    CartProduct findCartProductByCart_CartId(long cartCartId);

    void deleteCartProductsBycart(Cart cart);

    @Modifying
    void deleteCartProductsByCart(Cart cart);
}
