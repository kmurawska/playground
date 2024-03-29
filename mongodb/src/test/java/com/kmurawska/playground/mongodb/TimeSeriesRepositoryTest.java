package com.kmurawska.playground.mongodb;

import org.junit.jupiter.api.Test;

class TimeSeriesRepositoryTest {

    private CountryRepository repository = new CountryRepository();

    @Test
    void insertTimeSeries() {
        repository.insert(new TemperatureDataLoader().load());
    }

    @Test
    void count() {
        System.out.println(repository.count());
    }

    @Test
    void findAll() {
        repository.findAll().stream()
                .map(c -> {
                    System.out.println(c);
                    return c.getTemperature();
                })
                .forEach(System.out::println);
    }

    @Test
    void delete() {
        repository.deleteAll();
        System.out.println(repository.count());
    }

    @Test
    void countAverageTemperatureForCountries() {
        repository.countAverageTemperaturePerCountry().forEach(System.out::println);
    }

    @Test
    void countAverageTemperatureForAllCountries() {
        System.out.println(repository.countAverageTemperatureForAllCountries());
    }
}