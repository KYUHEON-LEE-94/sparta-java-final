package com.ecommerce.user.model;

import com.ecommerce.user.dto.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "users")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Setter
    @Column(nullable = false, length = 50)
    String username;

    @Setter
    @Column(nullable = false, unique = true)
    String email;

    @Setter
    @Column(nullable = false)
    String passwordHash;

    @Setter
    @Column(length = 15)
    String phoneNumber;

    @Setter
    @Column(columnDefinition = "TEXT")
    String address;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    Role role;

    @Column(name = "delete_yn", length = 1)
    @Setter
    Boolean deletedYn;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updatedAt;
}
