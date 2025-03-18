package com.ecommerce.product.service;

import com.ecommerce.product.dto.DiscountType;
import com.ecommerce.product.dto.ProductResponseDto;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDto createProduct(Product request) {
        Product product = productRepository.save(request);

        return buildProductResponseDto(product);
    }

    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        return buildProductResponseDto(product);
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
            .map(this::buildProductResponseDto)
            .collect(Collectors.toList());
    }

    private ProductResponseDto buildProductResponseDto(Product product) {
        return ProductResponseDto.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .stock(product.getStock())
            .categoryId(product.getCategoryId())
            .deletedYn(product.getDeletedYn())
            .discountType(product.getDiscountType())
            .discountAmount(product.getDiscountAmount())
            .discountRate(product.getDiscountRate())
            .createdAt(product.getCreatedAt())
            .updatedAt(product.getUpdatedAt())
            .build();
    }
}
