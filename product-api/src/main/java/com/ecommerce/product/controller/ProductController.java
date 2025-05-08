package com.ecommerce.product.controller;

import com.ecommerce.product.dto.ProductRequest;
import com.ecommerce.product.dto.ProductResponseDto;
import com.ecommerce.product.logging.service.LogService;
import com.ecommerce.product.service.ProductBatchService;
import com.ecommerce.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Tag(name = "Product API", description = "상품 관련 API")
public class ProductController {

    private final ProductService productService;
    private final ProductBatchService productBatchService;
    private final LogService logService;

    @GetMapping
    @Operation(summary = "상품 목록 조회", description = "등록된 모든 상품을 조회합니다.")
    public
    ResponseEntity<List<ProductResponseDto>>getAllProducts() {

        List<ProductResponseDto> response = productService.getAllProducts();

        response.forEach(product ->
                logService.logExposeEvent(product.getId(), product.getCategoryId(), 0L)
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "상품 상세 조회", description = "상품 ID로 상품 정보를 조회합니다.")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto response = productService.getProductById(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "상품 생성", description = "새로운 상품을 등록합니다.")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequest product) {
        ProductResponseDto response = productService.createProduct(product);

        logService.logClickEvent(response.getId(), response.getCategoryId(), "/product/");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch")
    @Operation(summary = "대량 상품 생성", description = "새로운 상품을 대량으로 등록합니다.")
    public ResponseEntity<Boolean> saveFromCSV() {
        boolean response = productBatchService.saveProductsFromCSV();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/batch")
    @Operation(summary = "대량 상품 업데이트", description = "상품 정보를 대량으로 갱신합니다.")
    public ResponseEntity<Boolean> updateFromCSV() {
        boolean response = productBatchService.updateProductsFromCSV();
        return ResponseEntity.ok(response);
    }
}
