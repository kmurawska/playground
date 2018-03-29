package com.kmurawska.playground.cassandraexample.lwt;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.utils.UUIDs;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {
    private final UUID productId;
    private final String name, description;
    private BigDecimal price;
    private UUID version;

    public Product(String name, String description, BigDecimal price) {
        this.productId = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.price = price;
        this.version = UUIDs.timeBased();
    }

    public Product(Row row) {
        this.productId = row.getUUID("product_id");
        this.name = row.getString("name");
        this.description = row.getString("description");
        this.price = row.getDecimal("price");
        this.version = row.getUUID("version");
    }

    public UUID getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product updatePrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public UUID getVersion() {
        return version;
    }
}
