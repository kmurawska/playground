package com.kmurawska.playground.cassandraexample.lwt;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ProductRepositoryTest {
    private static final BigDecimal PRICE_UPDATE_1 = new BigDecimal("10.89");
    private static final BigDecimal PRICE_UPDATE_2 = new BigDecimal("18.90");
    private ProductRepository repository;
    private Product product;

    @BeforeAll
    static void initAll() {
        new Keyspace().setUp();
    }

    @BeforeEach
    void init() {
        repository = new ProductRepository();
        product = new Product("book", "product description", new BigDecimal("3.43"));
        repository.save(product);
    }

    @Test
    @DisplayName("Update the latest version of the product should be successful")
    void updateOfTheLatestVersionOfProductShouldBeSuccessful() {
        product = product.updatePrice(PRICE_UPDATE_1);
        repository.update(product);

        product = repository.findByUUID(product.getProductId());

        product = product.updatePrice(PRICE_UPDATE_2);
        repository.update(product);

        product = repository.findByUUID(product.getProductId());

        assertEquals(PRICE_UPDATE_2, product.getPrice());
    }

    @Test
    @DisplayName("Update the old version of the product should results in optimistic lock violation")
    void updateOfOldVersionOfProductShouldResultInOptimisticLockViolation() {
        product = product.updatePrice(PRICE_UPDATE_1);
        repository.update(product);

        assertThrows(IllegalStateException.class, () -> {
            product = product.updatePrice(new BigDecimal("18.90"));
            repository.update(product);
        }, "Row has been added by another user.");

        product = repository.findByUUID(product.getProductId());

        assertEquals(PRICE_UPDATE_1, product.getPrice());
    }

    @AfterAll
    static void cleanUp() {
        new Keyspace().teardown();
    }
}