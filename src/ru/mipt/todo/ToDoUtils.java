package ru.mipt.todo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ToDoUtils {


    public static final String PROPERTY_SEPARATOR = ";";

    public static List<ToDoItem> loadToDoFromFile(String fileName) throws IOException {
        return Files.lines(Paths.get(fileName), StandardCharsets.UTF_8).map(line -> {
            String[] toDoItemProperties = line.split(PROPERTY_SEPARATOR);
            return new ToDoItem(Integer.parseInt(toDoItemProperties[0]), toDoItemProperties[1]);
        }).collect(Collectors.toList());
    }

    public static void saveToDoToFile(String fileName, List<ToDoItem> items) throws IOException {
        Files.write(Paths.get(fileName),
                items.stream().map(toDoItem -> toDoItem.getId()+PROPERTY_SEPARATOR+ toDoItem.getDescription()).collect(Collectors.toList()),
                StandardCharsets.UTF_8);
    }
}
