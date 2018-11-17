package com.kmurawska.playground.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

/**
 * http://sdwebx.worldbank.org/climateportal/index.cfm?page=downscaled_data_download&menu=historical
 */

class CountryRepository {
    private MongoCollection<Document> collection;
    private MongoDatabase database;

    CountryRepository() {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("db");
        collection = database.getCollection("country");
    }

    void insert(List<Country> countries) {
        List<Document> documents = countries.stream()
                .map(Country::toDocument)
                .collect(Collectors.toList());

        collection.insertMany(documents);
    }

    long count() {
        return collection.countDocuments();
    }

    List<Country> findAll() {
        try (Stream<Document> documents = stream(collection.find().spliterator(), false)) {
            return documents
                    .map(Country::fromDocument)
                    .collect(toList());
        }
    }

    void deleteAll() {
        collection.deleteMany(new BsonDocument());
    }
}
