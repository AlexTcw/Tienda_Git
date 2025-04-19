package com.tienda_back.Controller;

import com.tienda_back.model.dto.response.*;
import com.tienda_back.service.Products.ProductService;
import com.tienda_back.service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"*"})
@RequiredArgsConstructor
@RequestMapping(value = {"/product"})
public class ProductController {

    private final ProductService productService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonProducts> findAllProducts() {
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonProduct> findProductByID(@PathVariable Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("must be provide a valid id");
        }

        return ResponseEntity.ok(productService.findProductById(id));
    }

    @GetMapping(value = "/category",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonSet> findAllCategories() {
        return ResponseEntity.ok(productService.findAllCategories());
    }

    @GetMapping(value = "/category/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonProducts> findAllProductsByCategory(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findProductsByCategoryId(id));
    }

    @GetMapping(value = "/brand")
    public ResponseEntity<ResponseJsonSet> findAllBrands() {
        return ResponseEntity.ok(productService.findAllBrands());
    }

    @GetMapping(value = "/brand/{brandName}")
    public ResponseEntity<ResponseJsonProducts> findProductsByBrand(@PathVariable String brandName) {
        return ResponseEntity.ok(productService.findProductsByBrandName(brandName));
    }

    @GetMapping(value = "/sku/{sku}")
    public ResponseEntity<ResponseJsonProducts> findProductsBySku(@PathVariable String sku) {
        return ResponseEntity.ok(productService.findProductsBySku(sku));
    }

    @GetMapping(value = "/out-of-stock")
    public ResponseEntity<ResponseJsonProducts> findProductsOutOfStock() {
        return ResponseEntity.ok(productService.findProductsOutOfStock());
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseJsonGeneric> findProductsByKeyword(@RequestParam(required = false, defaultValue = "") String keyword,
                                                                     @RequestParam(required = false, defaultValue = "0") int page,
                                                                     @RequestParam(required = false, defaultValue = "10") int size) {
        if(keyword.isEmpty()) {
            keyword = null;
        }
        return ResponseEntity.ok(productService.findProductsByKeyword(keyword, page, size));
    }

}
