package com.ecommerce.order.model;

import com.ecommerce.order.dto.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "`order`")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Setter
    String user_id;

    @Setter
    String product_id;

    @Setter
    @Column(nullable = false)
    String shoppingAddress;

    @Setter
    @Column(nullable = false)
    Integer quantity;

    @Setter
    @Column(nullable = false)
    BigDecimal totalPrice;

    @Setter
    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    @Column(nullable = false, length = 50)
    OrderStatus status;

    @Column(name = "deleted_yn", length = 1)
    @Setter
    Boolean deletedYn;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;
}
