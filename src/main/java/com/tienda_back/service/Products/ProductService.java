package com.tienda_back.service.Products;

import com.tienda_back.model.dto.response.ResponseJsonGeneric;
import com.tienda_back.model.dto.response.ResponseJsonProduct;
import com.tienda_back.model.dto.response.ResponseJsonProducts;
import com.tienda_back.model.dto.response.ResponseJsonSet;

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
}
