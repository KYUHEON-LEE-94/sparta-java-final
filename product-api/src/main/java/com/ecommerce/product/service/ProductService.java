package com.ecommerce.product.service;

import com.ecommerce.product.dto.DiscountType;
import com.ecommerce.product.dto.OrderCreatedEvent;
import com.ecommerce.product.dto.ProductRequest;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDto createProduct(ProductRequest request) {
        Product product = buildProduct(request);

        Product productEntity = productRepository.save(product);

        return buildProductResponseDto(productEntity);
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

    @Transactional
    public void decreaseProduct(OrderCreatedEvent event) {
        Product product = productRepository.findById(event.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found"));

        int remaining = product.getStock() - event.getQuantity();
        if (remaining < 0) {
            throw new RuntimeException("재고 부족");
        }
        product.setStock(remaining);

        productRepository.save(product);
    }

    private Product buildProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setDiscountType(request.getDiscountType());
        product.setDiscountAmount(request.getDiscountAmount());
        product.setDiscountRate(request.getDiscountRate());
        return product;
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
