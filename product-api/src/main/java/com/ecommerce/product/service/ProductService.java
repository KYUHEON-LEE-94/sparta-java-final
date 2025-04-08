package com.ecommerce.product.service;

import com.ecommerce.product.dto.DiscountType;
import com.ecommerce.product.dto.OrderCreatedEvent;
import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponseDto;
import com.ecommerce.product.exception.ServiceException;
import com.ecommerce.product.exception.ServiceExceptionCode;
import com.ecommerce.product.metric.annotation.TimedMetric;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @TimedMetric("product_create")
    public ProductResponseDto createProduct(ProductRequest request) {
        Product product = buildProduct(request);
        Product saved = productRepository.save(product);
        return buildProductResponseDto(saved);
    }

    @TimedMetric("product_get")
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));
        return buildProductResponseDto(product);
    }

    @TimedMetric("product_list")
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::buildProductResponseDto)
                .collect(Collectors.toList());
    }

    @TimedMetric("product_decrease")
    @Transactional
    public void decreaseProduct(OrderCreatedEvent event) {
        Product product = productRepository.findById(event.getProductId())
                .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));

        int remaining = product.getStock() - event.getQuantity();
        if (remaining < 0) {
            throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK);
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
