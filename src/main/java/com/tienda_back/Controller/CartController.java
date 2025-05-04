package com.tienda_back.Controller;

import com.tienda_back.model.dto.consume.ConsumeJsonCart;
import com.tienda_back.model.dto.consume.ConsumeJsonProductCart;
import com.tienda_back.model.dto.response.ResponseJsonCart;
import com.tienda_back.model.dto.response.ResponseJsonCarts;
import com.tienda_back.model.dto.response.ResponseJsonString;
import com.tienda_back.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/cart"})
public class CartController {

    private final CartService cartService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonCart> createCart(@RequestBody ConsumeJsonCart consumeJsonCart) {
        if (consumeJsonCart == null) {
            throw new IllegalArgumentException("Consume json cart is null");
        }
        return ResponseEntity.ok(cartService.createCart(consumeJsonCart));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonCart> findCartByCartId(@PathVariable Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("must be provide a valid id");
        }

        return ResponseEntity.ok(cartService.findCartById(id));
    }

    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonCarts> findCartByUserId(@PathVariable Long id) {

        if (id == null) {
            throw new IllegalArgumentException("must be provide a valid id");
        }

        return ResponseEntity.ok(cartService.findCartsByUserId(id));
    }

    @GetMapping(value = "/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonCarts> findCartByCartStatus(@PathVariable String status) {

        if (status.isEmpty()) {
            throw new IllegalArgumentException("must be provide a valid status");
        }

        return ResponseEntity.ok(cartService.findCartsByCartStatus(status));
    }

    @PatchMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonString> cancelCartByCartId(@PathVariable Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("must be provide a valid id");
        }

        return ResponseEntity.ok(cartService.cancelCartByCartId(id));
    }

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonCart> updateCartProducts(@RequestBody ConsumeJsonProductCart consume) {
        if (consume == null) {
            throw new IllegalArgumentException("consume is null");
        }
        return ResponseEntity.ok(cartService.updateCart(consume));
    }

    @GetMapping(value = "search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonCarts> findCartByKeyWOrd(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {

        if (startDate == null) {
            startDate = LocalDateTime.of(1970, 1, 1, 0, 0); // fecha muy antigua
        }
        if (endDate == null) {
            endDate = LocalDateTime.now(); // fecha actual
        }

        // aquí iría tu lógica de búsqueda usando startDate y endDate
        return ResponseEntity.ok(cartService.findCartsByKeyword(keyword, startDate, endDate));
    }


}
