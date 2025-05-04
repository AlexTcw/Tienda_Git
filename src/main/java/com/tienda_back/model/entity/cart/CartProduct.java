package com.tienda_back.model.entity.cart;

import com.tienda_back.model.entity.Products.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_product")
public class CartProduct implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_product_seq")
    @SequenceGenerator(name = "cart_product_seq", sequenceName = "cart_product_seq", allocationSize = 1)
    @Column(name = "cart_product_id")
    private Long cartProductId;
    @Column(name = "quantity")
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
