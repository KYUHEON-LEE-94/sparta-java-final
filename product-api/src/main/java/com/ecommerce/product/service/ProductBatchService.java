package com.ecommerce.product.service;

import com.ecommerce.product.exception.ServiceException;
import com.ecommerce.product.exception.ServiceExceptionCode;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import com.google.common.collect.Lists;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductBatchService {

    private final ProductRepository productRepository;
    private final JdbcTemplate jdbcTemplate;
    private final RedisService redisService;

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Transactional
    public boolean saveProductsFromCSV() {
        try {
            ClassPathResource classPathResource = new ClassPathResource("products_sample_v1.csv");
            CSVReader csvReader = new CSVReader(new InputStreamReader(classPathResource.getInputStream()));

            String[] line;
            List<Product> batch = new ArrayList<>();
            int lineNumber = 0;

            csvReader.readNext();

            while ((line = csvReader.readNext()) != null) {
                lineNumber++;

                try {
                    String name = line[0];
                    String description = line[1];
                    BigDecimal price = new BigDecimal(line[2]);
                    Integer stock = Integer.valueOf(line[3]);

                    Product product = Product.builder()
                            .name(name)
                            .description(description)
                            .price(price)
                            .stock(stock)
                            .build();

                    batch.add(product);

                    if (batch.size() >= batchSize) {
                        saveAll(batch);
                        batch.clear();
                    }
                } catch (Exception e) {
                    log.warn("Line {} 처리 실패: {}", lineNumber, Arrays.toString(line), e);
                }
            }

            if (!batch.isEmpty()) {
                saveAll(batch);
            }

            return true;
        } catch (IOException e) {
            log.error("CSV 파일 로딩 실패", e);
            throw new RuntimeException("CSV 처리 중 오류 발생", e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public boolean updateProductsFromCSV() {

        try {
            ClassPathResource classPathResource = new ClassPathResource("update_products_sample_v1.csv");
            CSVReader csvReader = new CSVReader(
                    new InputStreamReader(classPathResource.getInputStream())
            );

            List<String[]> records = csvReader.readAll();
            List<Product> productsToUpdate = new ArrayList<>();

            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);
                Long id = Long.valueOf(record[0]);
                String name = record[1];
                String description = record[2];
                BigDecimal price = new BigDecimal(record[3]);
                Integer stock = Integer.valueOf(record[4]);

                Product products = productRepository.findById(id)
                        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));


                products.setName(name);
                products.setDescription(description);
                products.setPrice(price);
                products.setStock(stock);

                productsToUpdate.add(products);
            }

            batchUpdateProducts(productsToUpdate);

            return true;

        } catch (Exception e) {
            log.error("업데이트 실패", e);
            throw new ServiceException(ServiceExceptionCode.ERROR_CSV);
        }
    }

    private void saveAll(List<Product> products) {
        String sql =
                "INSERT INTO products (name, description, price, stock, created_at, updated_at) "
                        + "VALUES(?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
        List<Object[]> batchArgs = new ArrayList<>();

        for (Product product : products) {
            Object[] args = new Object[]{
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
            };

            batchArgs.add(args);
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
        redisService.deleteData(RedisKeyUtil.getProductKey("list"));
    }

    private void batchUpdateProducts(List<Product> products) {
        String sql = "UPDATE products SET " +
                "name = ?, description = ?, price = ?, stock = ?, updated_at = CURRENT_TIMESTAMP " +
                "WHERE id = ?";

        List<Object[]> batchArgs = new ArrayList<>();
        for (Product product : products) {
            Object[] args = new Object[]{
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
                    product.getId()
            };
            batchArgs.add(args);
        }

        jdbcTemplate.batchUpdate(sql, batchArgs);
        redisService.deleteData(RedisKeyUtil.getProductKey("list"));
    }
}
