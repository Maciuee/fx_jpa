package com.project.controller;

import java.time.LocalDate;
import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.project.dao.ProjektDAO;
import com.project.model.Projekt;
import java.util.Optional;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData; import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import java.time.LocalDateTime;

import java.util.List;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.project.dao.ProjektDAO;
import com.project.dao.ProjektDAOImpl;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;


public class ProjectController {
	
private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
//Zmienne do obs�ugi stronicowania i wyszukiwania
private String search4;
private Integer pageNo;
private Integer pageSize;
private ExecutorService wykonawca;
private ProjektDAO projektDAO;

private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
private static final DateTimeFormatter dateTimeFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//Automatycznie wstrzykiwane komponenty GUI
@FXML
private ChoiceBox<Integer> cbPageSizes;
@FXML
private TableView<Projekt> tblProjekt;
@FXML
private TableColumn<Projekt, Integer> colId;
@FXML
private TableColumn<Projekt, String> colNazwa;
@FXML
private TableColumn<Projekt, String> colOpis;
@FXML
private TableColumn<Projekt, LocalDateTime> colDataCzasUtworzenia;
@FXML
private TableColumn<Projekt, LocalDate> colDataOddania;
@FXML
private TextField txtSzukaj;

private ObservableList<Projekt> projekty;

public ProjectController() { //Utworzeniu pustego konstruktora jest obligatoryjne!
}

@FXML
public void initialize() { //Metoda automatycznie wywo�ywana przez JavaFX zaraz po wstrzykni�ciu
search4 = null; //wszystkich komponent�w. Uwaga! Wszelkie modyfikacje komponent�w
pageNo = 0; //(np. cbPageSizes) trzeba realizowa� wewn�trz tej metody. Nigdy
pageSize = 10; //nie u�ywaj do tego celu konstruktora.
cbPageSizes.getItems().addAll(5, 10, 20, 50, 100);
cbPageSizes.setValue(pageSize);

colId.setCellValueFactory(new PropertyValueFactory<Projekt, Integer>("projektId"));
colNazwa.setCellValueFactory(new PropertyValueFactory<Projekt, String>("nazwa"));
colOpis.setCellValueFactory(new PropertyValueFactory<Projekt, String>("opis"));
colDataCzasUtworzenia.setCellValueFactory(new PropertyValueFactory<Projekt, LocalDateTime>
("dataCzasUtworzenia"));
colDataOddania.setCellValueFactory(new PropertyValueFactory<Projekt, LocalDate>("dataOddania"));
projekty = FXCollections.observableArrayList();
//Powi�zanie tabeli z list� typu ObservableList przechowuj�c� projekty
tblProjekt.setItems(projekty);

//Utworzenie nowej kolumny
TableColumn<Projekt, Void> colEdit = new TableColumn<>("Edycja");
colEdit.setCellFactory(column -> new TableCell<Projekt, Void>() {
private final GridPane pane;
{ //Blok inicjalizuj�cy w anonimowej klasie wewn�trznej
Button btnEdit = new Button("Edycja");
Button btnRemove = new Button("Usu�");
btnEdit.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
btnRemove.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
btnEdit.setOnAction(event -> {
edytujProjekt(getCurrentProjekt());
});
btnRemove.setOnAction(event -> {
//TODO
//usunProjekt(getCurrentProjekt());
});
pane = new GridPane();
pane.setAlignment(Pos.CENTER);
pane.setHgap(10);
pane.setVgap(10);
pane.setPadding(new Insets(5, 5, 5, 5));
pane.add(btnEdit, 0, 0);
pane.add(btnRemove, 0, 1);
}
private Projekt getCurrentProjekt() {
int index = this.getTableRow().getIndex();
return this.getTableView().getItems().get(index);
}
@Override
protected void updateItem(Void item, boolean empty) {
super.updateItem(item, empty);
setGraphic(empty ? null : pane);
}
});
//Dodanie kolumny do tabeli
tblProjekt.getColumns().add(colEdit);
//Ustawienie wzgl�dnej szeroko�ci poszczeg�lnych kolumn
colId.setMaxWidth(5000);
colNazwa.setMaxWidth(10000);
colOpis.setMaxWidth(10000);
colDataCzasUtworzenia.setMaxWidth(9000);
colDataOddania.setMaxWidth(7000);
colEdit.setMaxWidth(7000);

colDataCzasUtworzenia.setCellFactory(column -> new TableCell<Projekt, LocalDateTime>() {
@Override
protected void updateItem(LocalDateTime item, boolean empty) {
super.updateItem(item, empty);
if (item == null || empty) {
setText(null);
} else {
setText(dateTimeFormater.format(item));
}
}
});


projektDAO = new ProjektDAOImpl();
wykonawca = Executors.newFixedThreadPool(1); // W naszej aplikacji wystarczy jeden w�tek do pobierania
// danych. Przekazanie wi�kszej ilo�ci takich zada� do
loadPage(search4, pageNo, pageSize); // puli jednow�tkowej powoduje ich kolejkowanie i sukcesywne
}

private void loadPage(String search4, Integer pageNo, Integer pageSize) {
wykonawca.execute(() -> {
try {
List<Projekt> projektList = search4 == null || search4.isEmpty()
? projektDAO.getProjekty(pageNo * pageSize, pageSize)
: projektDAO.searchByNazwa(search4, pageNo * pageSize, pageSize);
if(projektList != null) {
Platform.runLater(() -> {
projekty.clear();
projekty.addAll(projektList);
});
}
} catch (RuntimeException e) {
String errorInfo = "B��d podczas pobierania listy projekt�w!";
String errorDetails = e.getMessage();
logger.error("{} ({})", errorInfo, e);
Platform.runLater(() -> showError(errorInfo, errorDetails));
}
});
}
/** Metoda pomocnicza do prezentowania u�ytkownikowi informacji o b��dach */
private void showError(String header, String content) {
Alert alert = new Alert(AlertType.ERROR);
alert.setTitle("B��d");
alert.setHeaderText(header);
alert.setContentText(content);
alert.showAndWait();
}
public void shutdown() {
// Wystarczy�oby tylko samo wywo�anie metody wykonawca.shutdownNow(), ale mo�na r�wnie�, tak jak poni�ej,
// zaimplementowa� wersj� z oczekiwaniem na zako�czenie wszystkich zada� wykonywanych w puli w�tk�w.
if(wykonawca != null) {
wykonawca.shutdown();
try {
if(!wykonawca.awaitTermination(3, TimeUnit.SECONDS))
wykonawca.shutdownNow();
} catch (InterruptedException e) {
wykonawca.shutdownNow();
}
}
}

//Grupa metod do obs�ugi przycisk�w
@FXML
private void onActionBtnSzukaj(ActionEvent event) {
}
@FXML
private void onActionBtnDalej(ActionEvent event) {
}
@FXML
private void onActionBtnWstecz(ActionEvent event) {
}
@FXML
private void onActionBtnPierwsza(ActionEvent event) {
}
@FXML
private void onActionBtnOstatnia(ActionEvent event) {
}
@FXML
private void onActionBtnDodaj(ActionEvent event) {
	edytujProjekt(new Projekt());
}
private void edytujProjekt(Projekt projekt) {
Dialog<Projekt> dialog = new Dialog<>();
dialog.setTitle("Edycja");
if (projekt.getProjektId() != null) {
dialog.setHeaderText("Edycja danych projektu");
} else {
dialog.setHeaderText("Dodawanie projektu");
}
dialog.setResizable(true);
Label lblId = getRightLabel("Id: ");
Label lblNazwa = getRightLabel("Nazwa: ");
Label lblOpis = getRightLabel("Opis: ");
Label lblDataCzasUtworzenia = getRightLabel("Data utworzenia: ");
Label lblDataOddania = getRightLabel("Data oddania: ");
Label txtId = new Label();
if (projekt.getProjektId() != null)
txtId.setText(projekt.getProjektId().toString());
TextField txtNazwa = new TextField();
if (projekt.getNazwa() != null)
txtNazwa.setText(projekt.getNazwa());
TextArea txtOpis = new TextArea();
txtOpis.setPrefRowCount(6);
txtOpis.setPrefColumnCount(40);
txtOpis.setWrapText(true);
if (projekt.getOpis() != null)
txtOpis.setText(projekt.getOpis());
Label txtDataUtworzenia = new Label();
if (projekt.getDataCzasUtworzenia() != null)
txtDataUtworzenia.setText(dateTimeFormater.format(projekt.getDataCzasUtworzenia()));
DatePicker dtDataOddania = new DatePicker();
dtDataOddania.setPromptText("RRRR-MM-DD");
dtDataOddania.setConverter(new StringConverter<LocalDate>() {
@Override
public String toString(LocalDate date) {
return date != null ? dateFormatter.format(date) : null;
}
@Override
public LocalDate fromString(String text) {
return text == null || text.trim().isEmpty() ? null : LocalDate.parse(text, dateFormatter);
}
});
dtDataOddania.getEditor().focusedProperty().addListener((obsValue, oldFocus, newFocus) -> {
if (!newFocus) {
try {
dtDataOddania.setValue(dtDataOddania.getConverter().fromString(
dtDataOddania.getEditor().getText()));
} catch (DateTimeParseException e) {
dtDataOddania.getEditor().setText(dtDataOddania.getConverter()
.toString(dtDataOddania.getValue()));
}
}
});
if (projekt.getDataOddania() != null) {
dtDataOddania.setValue(projekt.getDataOddania());
}
GridPane grid = new GridPane();
grid.setHgap(10);
grid.setVgap(10);
grid.setPadding(new Insets(5, 5, 5, 5));
grid.add(lblId, 0, 0);
grid.add(txtId, 1, 0);
grid.add(lblDataCzasUtworzenia, 0, 1);
grid.add(txtDataUtworzenia, 1, 1);
grid.add(lblNazwa, 0, 2);
grid.add(txtNazwa, 1, 2);
grid.add(lblOpis, 0, 3);
grid.add(txtOpis, 1, 3);
grid.add(lblDataOddania, 0, 4);
grid.add(dtDataOddania, 1, 4);
dialog.getDialogPane().setContent(grid);
ButtonType buttonTypeOk = new ButtonType("Zapisz", ButtonData.OK_DONE);
ButtonType buttonTypeCancel = new ButtonType("Anuluj", ButtonData.CANCEL_CLOSE);
dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
dialog.setResultConverter(new Callback<ButtonType, Projekt>() {
@Override
public Projekt call(ButtonType butonType) {
if (butonType == buttonTypeOk) {
projekt.setNazwa(txtNazwa.getText().trim());
projekt.setOpis(txtOpis.getText().trim());
projekt.setDataOddania(dtDataOddania.getValue());
return projekt;
}
return null;
}
});
Optional<Projekt> result = dialog.showAndWait();
if (result.isPresent()) {
wykonawca.execute(() -> {
try {
projektDAO.setProjekt(projekt);
Platform.runLater(() -> {
if (tblProjekt.getItems().contains(projekt)) {
tblProjekt.refresh();
} else {
tblProjekt.getItems().add(0, projekt);
}
});
} catch (RuntimeException e) {
String errorInfo = "B��d podczas zapisywania danych projektu!";
String errorDetails = e.getMessage();
logger.error("{} ({})", errorInfo, e);
Platform.runLater(() -> showError(errorInfo, errorDetails));
}
});
}
}
private Label getRightLabel(String text) {
Label lbl = new Label(text);
lbl.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
lbl.setAlignment(Pos.CENTER_RIGHT);
return lbl;
}
}
