package org.example;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class Main {
    public static void main(String[] args) {
        Path file = getFileByArgument(args[0]);
        TextSorter textSorter = new TextSorter();
        textSorter.sort(file, args);
        log.info("Все готово!");
    }

    private static Path getFileByArgument(String fileName) {
        Path file = Paths.get(fileName);
        if (!Files.exists(file)) {
            String errorMessage = String.format("Не удается найти файл по имени: %s", fileName);
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
        return file;
    }
}