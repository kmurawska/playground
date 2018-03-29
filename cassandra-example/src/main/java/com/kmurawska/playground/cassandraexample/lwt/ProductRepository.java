package com.kmurawska.playground.cassandraexample.lwt;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;

import java.util.UUID;

import static com.kmurawska.playground.cassandraexample.CassandraConnection.CASSANDRA_CONNECTION;
import static com.kmurawska.playground.cassandraexample.lwt.Keyspace.PRODUCTS_TABLE;

class ProductRepository {
    private final Session session;

    ProductRepository() {
        this.session = CASSANDRA_CONNECTION.getSession();
    }

    Product findByUUID(UUID productId) {
        BoundStatement statement = new BoundStatement(session.prepare(
                "SELECT * FROM " + PRODUCTS_TABLE + " WHERE product_id = ? "
        )).bind(productId);

        ResultSet result = this.session.execute(statement);

        return new Product(result.one());
    }

    void save(final Product product) {
        BoundStatement statement = new BoundStatement(session.prepare(
                "INSERT INTO " + PRODUCTS_TABLE + " (product_id, name, description, price, version) VALUES (?, ?, ?, ?, ?) IF NOT EXISTS;"
        )).bind(product.getProductId(), product.getName(), product.getDescription(), product.getPrice(), product.getVersion());

        ResultSet result = this.session.execute(statement);

        if (!result.wasApplied())
            throw new IllegalStateException("Row has been added by another user.");
    }

    void update(final Product product) {
        BoundStatement statement = new BoundStatement(session.prepare(
                "UPDATE " + PRODUCTS_TABLE + " SET price = ?, version = ? WHERE product_id =? AND name = ? IF version = ?;"
        )).bind(product.getPrice(), UUIDs.timeBased(), product.getProductId(), product.getName(), product.getVersion());

        ResultSet result = this.session.execute(statement);

        if (!result.wasApplied())
            throw new IllegalStateException("Optimistic Lock - Row has been updated by another user.");
    }
}