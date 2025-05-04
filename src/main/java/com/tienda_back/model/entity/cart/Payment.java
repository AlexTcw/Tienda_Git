package com.tienda_back.model.entity.cart;

import com.tienda_back.model.enums.PaymentStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "payment_id")
    private Long paymentId;
    @OneToOne
    @JoinColumn(name = "cart_id", nullable = false,unique = true)
    private Cart cart;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatusEnum paymentStatus;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "payment_date")
    private LocalDateTime localDateTime;
}
