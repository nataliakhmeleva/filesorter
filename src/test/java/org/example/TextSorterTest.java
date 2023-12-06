package org.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
class TextSorterTest {

    private final TextSorter textSorter = new TextSorter();

    @Test
    @Order(1)
    void testSorting1() throws IOException {
        String[] args = {"file.txt", "1", "1"};
        Path file = new File(TextSorterTest.class.getClassLoader().getResource(args[0]).getFile()).toPath();

        Path result = textSorter.sort(file, args);
        List<String> expectedList = List.of(
                "Кошка убегает от собаки 1",
                "Мышка спокойно сидит в своей норе 1",
                "Собака пытается догнать кошку 2",
                "Собака пытается догнать кошку 2");
        List<String> resultLines = readFileToCollection(result);

        Assertions.assertEquals(expectedList, resultLines);

        Files.deleteIfExists(result);
    }

    @Test
    @Order(2)
    void testSorting2() throws IOException {
        String[] args = {"file.txt", "2", "1"};
        Path file = new File(TextSorterTest.class.getClassLoader().getResource(args[0]).getFile()).toPath();

        Path result = textSorter.sort(file, args);
        List<String> expectedList = List.of(
                "Кошка убегает от собаки 1",
                "Собака пытается догнать кошку 2",
                "Собака пытается догнать кошку 2",
                "Мышка спокойно сидит в своей норе 1");
        List<String> resultLines = readFileToCollection(result);

        Assertions.assertEquals(expectedList, resultLines);

        Files.deleteIfExists(result);
    }

    @Test
    @Order(3)
    void testSorting3() throws IOException {
        String[] args = {"file.txt", "3", "2"};
        Path file = new File(TextSorterTest.class.getClassLoader().getResource(args[0]).getFile()).toPath();

        Path result = textSorter.sort(file, args);
        List<String> expectedList = List.of(
                "Собака пытается догнать кошку 2",
                "Собака пытается догнать кошку 2",
                "Кошка убегает от собаки 1",
                "Мышка спокойно сидит в своей норе 1");
        List<String> resultLines = readFileToCollection(result);

        Assertions.assertEquals(expectedList, resultLines);

        Files.deleteIfExists(result);
    }

    @Test
    void whenCheckArgumentsThenFewArguments() {
        String[] args = {"file.txt"};
        Path file = new File(TextSorterTest.class.getClassLoader().getResource(args[0]).getFile()).toPath();

        Assertions.assertThrows(IllegalArgumentException.class, () -> textSorter.sort(file, args), "Мало аргументов");
    }

    @Test
    void whenCheckArgumentsThenNo3Args() {
        String[] args = {"file.txt", "3"};
        Path file = new File(TextSorterTest.class.getClassLoader().getResource(args[0]).getFile()).toPath();

        Assertions.assertThrows(IllegalArgumentException.class, () -> textSorter.sort(file, args),
                "При втором аргументе равном 3, обязательно должен быть 3-ий аргумент");
    }

    @Test
    void whenCheckArgumentsThen2ArgsDontNecessaryValue() {
        String[] args = {"file.txt", "6"};
        Path file = new File(TextSorterTest.class.getClassLoader().getResource(args[0]).getFile()).toPath();

        Assertions.assertThrows(IllegalArgumentException.class, () -> textSorter.sort(file, args),
                "Второй аргумент может иметь значения только: 1,2,3");
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
}