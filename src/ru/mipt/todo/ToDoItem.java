package ru.mipt.todo;

import javafx.beans.property.SimpleStringProperty;

public class ToDoItem {

    private int id;
    //private String description;

    //private final SimpleStringProperty firstName;
    //private final SimpleStringProperty lastName;
    private final SimpleStringProperty description;

    public ToDoItem(int id, String description) {
        this.id = id;
        //this.description = description;
        this.description = new SimpleStringProperty(description);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}
