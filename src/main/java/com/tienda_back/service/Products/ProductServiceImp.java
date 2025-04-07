package com.tienda_back.service.Products;

import com.tienda_back.model.dto.response.ResponseJsonProduct;
import com.tienda_back.model.dto.response.ResponseJsonProducts;
import com.tienda_back.model.entity.Products.Category;
import com.tienda_back.model.entity.Products.Inventory;
import com.tienda_back.model.entity.Products.Product;
import com.tienda_back.repository.Product.CategoryProductRepository;
import com.tienda_back.repository.Product.CategoryRepository;
import com.tienda_back.repository.Product.InventoryRepository;
import com.tienda_back.repository.Product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryProductRepository categoryProductRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public ResponseJsonProducts findAllProducts() {
        // Verifica si la lista de productos está vacía
        var products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new IllegalArgumentException("No products found");
        }

        // Creamos una lista de productos de respuesta
        List<ResponseJsonProduct> responseProducts = new ArrayList<>();

        // Iteramos sobre los productos encontrados
        for (Product product : products) {
            // Buscamos el inventario asociado al producto
            Inventory inventory = (Inventory) inventoryRepository.findInventoryByProduct(product);

            // Buscamos las categorías asociadas al producto
            var cps = categoryProductRepository.findCategoryProductByProduct(product);

            // Inicializamos la categoría como nula, ya que un producto podría tener varias
            Category category = null;

            // Solo tomamos la primera categoría encontrada (ajustar según tu lógica de negocio)
            if (!cps.isEmpty()) {
                category = (Category) categoryRepository.findCategoryByCategoryId(cps.getFirst().getCategory().getCategoryId());
            }

            // Si no encontramos inventario o categoría, podemos lanzar una excepción o manejarlo según se desee
            if (inventory == null || category == null) {
                throw new IllegalStateException("Inventory or category missing for product " + product.getProductId());
            }

            // Construimos el objeto de respuesta para cada producto
            responseProducts.add(new ResponseJsonProduct(
                    product.getProductId(),
                    product.getName(),
                    product.getSku(),
                    product.getDescription(),
                    product.getPrice(),
                    category.getName(),
                    product.getBrand(),
                    inventory.getStock()
            ));
        }

        // Retornamos la lista de productos en la respuesta JSON
        return new ResponseJsonProducts(responseProducts);
    }

}
