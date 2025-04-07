package com.tienda_back.Controller;

import com.tienda_back.model.dto.response.ResponseJsonGeneric;
import com.tienda_back.model.dto.response.ResponseJsonLogin;
import com.tienda_back.model.dto.response.ResponseJsonProducts;
import com.tienda_back.service.Products.ProductService;
import com.tienda_back.service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"*"})
@RequiredArgsConstructor
@RequestMapping(value = {"/product"})
public class ProductController {

    private final ProductService productService;

    @GetMapping(value = {"/all"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonProducts> findAllProducts() {
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @GetMapping(value = {"/id"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonLogin> findProductByID(@PathVariable Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("must be provide a valid id");
        }

        return ResponseEntity.ok(null);
    }

}
