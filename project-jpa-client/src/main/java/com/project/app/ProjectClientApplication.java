package com.project.app;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.project.controller.ProjectController;
import com.project.dao.ProjektDAO;
import com.project.dao.ProjektDAOImpl;
import com.project.model.Projekt;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProjectClientApplication extends Application {
private Parent root;
private FXMLLoader fxmlLoader;
public static void main(String[] args) {
launch(ProjectClientApplication.class, args);
}
@Override
public void start(Stage primaryStage) throws Exception {
fxmlLoader = new FXMLLoader();
fxmlLoader.setLocation(getClass().getResource("/fxml/ProjectFrame.fxml"));
root = fxmlLoader.load();
primaryStage.setTitle("Projekty");
Scene scene = new Scene(root);
scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
ProjectController controller = fxmlLoader.getController();
primaryStage.setOnCloseRequest(event -> { primaryStage.hide();
controller.shutdown();
Platform.exit();
System.exit(0);
});
primaryStage.setScene(scene);
primaryStage.sizeToScene();
primaryStage.show();
}
}
/*
 * 
... try {
Projekt projekt = new Projekt("Projekt testowy", "Opis testowy", LocalDate.of(2020, 06, 22));
projektDAO.setProjekt(projekt);
logger.info("Id utworzonego projektu: {}", projekt.getProjektId());
//System.out.println("Id utworzonego projektu: " + projekt.getProjektId());
} catch (RuntimeException e) {
logger.error("B³¹d podczas dodawania projektu!", e);
}
... try {
Integer projektId = 1;
Projekt projekt1 = projektDAO.getProjekt(projektId);
logger.info("Projekt - Id: {}, nazwa: {}, opis: {}", projekt1.getProjektId(), projekt1.getNazwa(),
projekt1.getOpis());
} catch (RuntimeException e) {
logger.error("B³¹d podczas pobierania projektu (Id: {})!", projektId, e);
}
...*/
