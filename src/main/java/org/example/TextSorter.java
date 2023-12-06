package org.example;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Data
public class TextSorter {

    public Path sort(Path file, String[] args) {
        checkArgumentsAndGetFile(args);
        List<String> linesFromFile = readFileToCollection(file);
        List<String> sortedLines = sortCollection(linesFromFile, args);
        List<String> formattedLines = formatOutputList(sortedLines);
        return writeResultToFile(file.getFileName().toString(), formattedLines);
    }

    private void checkArgumentsAndGetFile(String[] args) {
        if (args.length < 2) {
            String errorMessage = "Мало аргументов";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        if ("3".equals(args[1]) && args.length == 2) {
            String errorMessage = "При втором аргументе равном 3, обязательно должен быть 3-ий аргумент";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        if (!List.of("1","2","3").contains(args[1])) {
            String errorMessage = "Второй аргумент может иметь значения только: 1,2,3";
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private List<String> readFileToCollection(Path file) {
        try (Stream<String> lines = Files.lines(file)) {
            return lines.collect(Collectors.toList());
        } catch (IOException e) {
            String errorMessage = String.format("Не удается прочитать файл %s", file);
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }

    private List<String> sortCollection(List<String> lines, String[] args) {
        String sortType = args[1];
        int wordIndex = Integer.parseInt(args[2]);
        switch (sortType) {
            case "1": // Алфавитный порядок
                Collections.sort(lines);
                break;
            case "2": // По количеству символов
                lines.sort(Comparator.comparingInt(String::length));
                break;
            case "3": // По слову в строке
                Comparator<String> comparator = Comparator.comparing(line -> Arrays.asList(line.split(" ")).get(wordIndex));
                lines.sort(comparator);
        }
        return lines;
    }

    private List<String> formatOutputList(List<String> lines) {
        Map<String, Long> countedLines = countingRows(lines);
        return lines
                .stream()
                .map(line -> formLine(line, countedLines))
                .collect(Collectors.toList());
    }

    private Map<String, Long> countingRows(List<String> lines) {
        return lines
                .stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private String formLine(String line, Map<String, Long> countedLines) {
        return line
                .concat(" ")
                .concat(countedLines.get(line).toString());
    }

    private Path writeResultToFile(String fileName, List<String> formattedLines) {
        String resultFileName = "sorted_".concat(fileName);
        try {
            Path resultFile = Paths.get(resultFileName);
            Files.write(resultFile, formattedLines);
            return resultFile;
        } catch (IOException e) {
            String errorMessage = String.format("Не удается записать результирующий файл %s", resultFileName);
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }
}
