package com.tienda_back.service.Products;

import com.tienda_back.model.dto.consume.ConsumeJsonLong;
import com.tienda_back.model.dto.consume.ConsumeJsonProduct;
import com.tienda_back.model.dto.response.*;

public interface ProductService {

    ResponseJsonProducts findAllProducts();

    ResponseJsonProduct findProductById(Long id);

    ResponseJsonSet findAllCategories();

    ResponseJsonProducts findProductsByCategoryId(Long categoryId);

    ResponseJsonProducts findProductsBySku(String sku);

    ResponseJsonSet findAllBrands();

    ResponseJsonProducts findProductsByBrandName(String brandName);

    ResponseJsonProducts findProductsOutOfStock();

    ResponseJsonGeneric findProductsByKeyword(String keyword, int page, int size);

    ResponseJsonProduct createProduct(ConsumeJsonProduct consumeJsonProduct);

    ResponseJsonProduct updateProduct(ConsumeJsonProduct consumeJsonProduct);

    ResponseJsonString deleteProductById(ConsumeJsonLong consume);
}
