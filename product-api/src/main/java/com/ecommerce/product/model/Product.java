package com.ecommerce.product.model;

import com.ecommerce.product.dto.DiscountType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

import javax.persistence.*;

import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "products")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Setter
    @Column(nullable = false)
    String name;

    @Setter
    @Column(nullable = false, length = 2048)
    String description;

    @Setter
    @Column(nullable = false)
    BigDecimal price;

    @Setter
    @Column(nullable = false)
    Integer stock;

    @Setter
    String categoryId;

    @Setter
    @Column(name = "deleted_yn", length = 1)
    Boolean deletedYn;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    DiscountType discountType;

    @Setter
    @Column(name = "discount_amount")
    BigDecimal discountAmount;

    @Setter
    @Column(name = "discount_rate")
    BigDecimal discountRate;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Builder
    public Product(String name, String description, BigDecimal price, Integer stock,Boolean deletedYn) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.deletedYn = deletedYn;
    }
}
