package com.kmurawska.playground.mongodb;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.nio.file.Files.list;
import static java.util.stream.Collectors.toList;

class CountryReader {
    private static final String SEPARATOR = ",";

    List<Country> read() {
        try (Stream<Path> files = filesInDirectory()) {
            return files.map(this::readLines)
                    .map(this::toCountry)
                    .collect(toList());
        }
    }

    private Stream<Path> filesInDirectory() {
        try {
            return list(Paths.get(Objects.requireNonNull(CountryReader.class.getClassLoader().getResource("temperature")).toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<String> readLines(Path file) {
        try {
            return Files.lines(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Country toCountry(Stream<String> lines) {
        Country country = lines.findFirst()
                .map(this::split)
                .map(this::toCountry)
                .orElse(Country.UNKNOWN_COUNTRY);

        List<TimeSeries> timeSeries = lines
                .skip(2)
                .map(this::split)
                .map(this::toTimeSeries)
                .collect(toList());

        return country.withTemperature(timeSeries);
    }

    private String[] split(String line) {
        return line.replaceAll("\"", "").split(SEPARATOR);
    }

    private Country toCountry(String[] line) {
        return Country.create(line[0], line[1]);
    }

    private TimeSeries toTimeSeries(String[] line) {
        return TimeSeries.create(parseInt(line[1]), parseInt(line[2]), parseDouble(line[0]));
    }
}
