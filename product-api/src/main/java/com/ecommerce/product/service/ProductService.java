package com.ecommerce.product.service;

import com.ecommerce.product.dto.DiscountType;
import com.ecommerce.product.dto.OrderCreatedEvent;
import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponseDto;
import com.ecommerce.product.exception.ServiceException;
import com.ecommerce.product.exception.ServiceExceptionCode;
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
    private final MeterRegistry meterRegistry;

    public ProductResponseDto createProduct(ProductRequest request) {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            Product product = buildProduct(request);
            Product productEntity = productRepository.save(product);

            meterRegistry.counter("product_created_total").increment();

            return buildProductResponseDto(productEntity);
        } catch (Exception e) {
            meterRegistry.counter("product_create_failed_total").increment();
            throw e;
        } finally {
            sample.stop(meterRegistry.timer("product_create_duration"));
        }
    }

    public ProductResponseDto getProductById(Long id) {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> {
                        meterRegistry.counter("product_not_found_total").increment();
                        return new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT);
                    });

            meterRegistry.counter("product_view_total").increment();

            return buildProductResponseDto(product);
        } finally {
            sample.stop(meterRegistry.timer("product_get_duration"));
        }
    }

    public List<ProductResponseDto> getAllProducts() {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            List<Product> products = productRepository.findAll();
            meterRegistry.counter("product_list_total").increment();

            return products.stream()
                    .map(this::buildProductResponseDto)
                    .collect(Collectors.toList());
        } finally {
            sample.stop(meterRegistry.timer("product_list_duration"));
        }
    }

    @Transactional
    public void decreaseProduct(OrderCreatedEvent event) {
        Timer.Sample sample = Timer.start(meterRegistry);
        try {
            Product product = productRepository.findById(event.getProductId())
                    .orElseThrow(() -> {
                        meterRegistry.counter("product_not_found_total").increment();
                        return new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT);
                    });

            int remaining = product.getStock() - event.getQuantity();
            if (remaining < 0) {
                meterRegistry.counter("product_stock_failed_total").increment();
                throw new ServiceException(ServiceExceptionCode.OUT_OF_STOCK);
            }

            product.setStock(remaining);
            productRepository.save(product);

            meterRegistry.counter("product_stock_decreased_total").increment();
        } finally {
            sample.stop(meterRegistry.timer("product_decrease_duration"));
        }
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
