package ru.mipt.todo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class Controller {

    private ObservableList<ToDoItem> toDoItems = FXCollections.observableArrayList();

    //для хранения, какой элемент меняем или создаем в редактируемых текстовых полях
    private ToDoItem editorItem = null;

    @FXML
    private TableView<ToDoItem> tvToDoItems;

    @FXML
    private TableColumn<ToDoItem, Integer> clnId;
    @FXML
    private TableColumn<ToDoItem, String> clnDescription;

    @FXML
    private Button btnLoadData;
    @FXML
    private Button btnSaveData;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnAddToDoItem;
    @FXML
    private Button btnEditToDoItem;
    @FXML
    private Button btnDeleteToDoItem;
    @FXML
    private Button btnSaveToList;

    @FXML
    private TextField txtToDoItemId;
    @FXML
    private TextField txtToDoItemDescription;


    // инициализируем форму данными
    @FXML
    private void initialize() {

        //скрываем кнопку сохранение данных в режиме редактирования
        btnSaveToList.setVisible(false);

        loadData();

        tvToDoItems.setEditable(true);

        // устанавливаем тип и значение которое должно хранится в колонке
        clnId.setCellValueFactory(new PropertyValueFactory<ToDoItem, Integer>("id"));
        clnId.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<ToDoItem,Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<ToDoItem, Integer> t) {
                        (t.getTableView().getItems().get(t.getTablePosition().getRow())).setId(t.getNewValue());
                    }
                }
        );

        clnDescription.setCellValueFactory(new PropertyValueFactory<ToDoItem, String>("description"));
        clnDescription.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<ToDoItem,String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<ToDoItem, String> t) {
                        (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDescription(t.getNewValue());
                    }
                }
        );

        // заполняем таблицу данными
        tvToDoItems.setItems(toDoItems);

        //обработчики кнопок:
        btnLoadData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                loadData();
            }
        });

        btnSaveData.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    ToDoUtils.saveToDoToFile("ToDoDataSource.txt", tvToDoItems.getItems());
                } catch (IOException e1) {
                    System.out.println("Ошибка сохранения данных");
                    e1.printStackTrace();
                }
            }
        });

        btnClear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                toDoItems.clear();
            }
        });

        btnAddToDoItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                editorItem = null;
                txtToDoItemId.clear();
                txtToDoItemDescription.clear();

                btnSaveToList.setText("Добавить в список");
                btnSaveToList.setVisible(true);
            }
        });

        btnEditToDoItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                editorItem = tvToDoItems.getSelectionModel().getSelectedItem();
                if (editorItem == null)
                    return;

                txtToDoItemId.setText(Integer.toString(editorItem.getId()));
                txtToDoItemDescription.setText(editorItem.getDescription());

                btnSaveToList.setText("Сохранить изменения");
                btnSaveToList.setVisible(true);
            }
        });

        btnDeleteToDoItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ToDoItem item = tvToDoItems.getSelectionModel().getSelectedItem();
                if (item == null)
                    return;
                toDoItems.remove(item);
            }
        });

        btnSaveToList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (editorItem == null) {
                    //создаем новое дело
                    ToDoItem item = new ToDoItem(Integer.parseInt(txtToDoItemId.getText()), txtToDoItemDescription.getText());
                    toDoItems.add(item);

                    tvToDoItems.getSelectionModel().select(item);
                }
                else {
//TODO: Пример кода: https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
                    tvToDoItems.getSelectionModel().select(editorItem);
                    editorItem = tvToDoItems.getSelectionModel().getSelectedItem();
                    //сохраняем изменения существующего
                    editorItem.setId(Integer.parseInt(txtToDoItemId.getText()));
                    editorItem.setDescription(txtToDoItemDescription.getText());

                    System.out.println(editorItem.getDescription());
                    System.out.println(tvToDoItems.getSelectionModel().getSelectedItem().getDescription());
                }

                CancelEdit();
            }
        });
    }

    //загрузка данных из внешнего источника данных в список объектов, файл - частный случай; вызов из двух мест
    private void loadData()
    {
        toDoItems.clear();
        try {
            toDoItems.addAll(ToDoUtils.loadToDoFromFile("ToDoDataSource.txt"));
        } catch (IOException e1) {
            System.out.println("Ошибка загрузки данных");
            e1.printStackTrace();
        }
    }

    private  void CancelEdit()
    {
        btnSaveToList.setVisible(false);
        editorItem = null;
    }
}
