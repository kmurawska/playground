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

class TemperatureDataLoader {
    private static final String SEPARATOR = ",";

    List<Country> load() {
        try (Stream<Path> files = filesInDirectory()) {
            return files.map(this::readLines)
                    .map(this::toCountry)
                    .collect(toList());
        }
    }

    private Stream<Path> filesInDirectory() {
        try {
            return list(Paths.get(Objects.requireNonNull(TemperatureDataLoader.class.getClassLoader().getResource("temperature")).toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> readLines(Path file) {
        try {
            return Files.lines(file).collect(toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Country toCountry(List<String> lines) {
        Country country = toCountry(lines.get(0).split(SEPARATOR));

        List<TimeSeries> timeSeries = lines.stream()
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
