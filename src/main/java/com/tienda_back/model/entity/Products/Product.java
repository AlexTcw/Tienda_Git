package com.tienda_back.model.entity.Products;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "sku", nullable = false, unique = true, updatable = false, length = 50)
    private String sku;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "price")
    private Double price;
    @Column(name = "brand", nullable = false)
    private String brand;
    @Column(name = "stock")
    private Integer stock;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryProduct> categoryProducts = new ArrayList<>();
}
