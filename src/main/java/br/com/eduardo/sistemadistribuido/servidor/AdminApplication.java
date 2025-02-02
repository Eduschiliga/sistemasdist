package br.com.eduardo.sistemadistribuido.servidor;


import br.com.eduardo.sistemadistribuido.util.SocketManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AdminApplication extends Application {
  public static String token = "9999999";
  private static Stage stage;

  @Override
  public void start(Stage stage) throws IOException {
    AdminApplication.stage = stage;

    stage.setTitle("Sistema Distribuído - Admin");

    Parent fxmlPrincipal = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/br/com/eduardo/sistemadistribuido/servidor/principal-admin-view.fxml")));
    Scene principalScene = new Scene(fxmlPrincipal, 600, 400);

    stage.setScene(principalScene);
    stage.setMaximized(true);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
