package com.ecommerce.product.service;

import com.ecommerce.product.dto.OrderCreatedEvent;
import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponseDto;
import com.ecommerce.product.exception.ServiceException;
import com.ecommerce.product.exception.ServiceExceptionCode;
import com.ecommerce.product.metric.annotation.TimedMetric;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final RedisService redisService;

    @TimedMetric("product_create")
    public ProductResponseDto createProduct(ProductRequest request) {
        Product product = buildProduct(request);
        Product saved = productRepository.save(product);

        redisService.deleteData(RedisKeyUtil.getProductKey("list"));

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
        final String CACHE_KEY = RedisKeyUtil.getProductKey("list");

        // 1. Redis 캐시 조회
        List<ProductResponseDto> cachedList = redisService.findObject(CACHE_KEY, new TypeReference<>() {});
        if (cachedList != null) {
            return cachedList;
        }

        List<ProductResponseDto> productList = productRepository.findAll().stream()
                .map(this::buildProductResponseDto)
                .collect(Collectors.toList());

        redisService.setObject(CACHE_KEY, productList, 3000);

        return productList;
    }


    @Transactional
    @TimedMetric("product_decrease")
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
