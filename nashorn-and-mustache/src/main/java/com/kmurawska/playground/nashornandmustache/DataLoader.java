package com.kmurawska.playground.nashornandmustache;

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
import static java.util.stream.Collectors.toList;

class DataLoader {
    private static final String SEPARATOR = ",";

    List<TimeSeries> load() {
        try (Stream<String> lines = lines()) {
            return lines.map(this::split)
                    .map(this::toTimeSeries)
                    .collect(toList());
        }
    }

    private Stream<String> lines() {
        try {
            Path path = Paths.get(Objects.requireNonNull(DataLoader.class.getClassLoader().getResource("data.csv")).toURI());
            return Files.lines(path);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String[] split(String line) {
        return line.replaceAll("\"", "").split(SEPARATOR);
    }

    private TimeSeries toTimeSeries(String[] line) {
        return TimeSeries.create(parseInt(line[1]), parseInt(line[2]), parseDouble(line[0]), line[3]);
    }
}
