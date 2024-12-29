package br.com.eduardo.sistemadistribuido.cliente;

import br.com.eduardo.sistemadistribuido.util.SocketManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static br.com.eduardo.sistemadistribuido.util.AlertUtil.exibirDialogoConexao;

public class ClienteApplication extends Application {
  public static String token = "";

  private static Stage stage;

  private static Scene loginScene;
  private static Scene principalScene;

  public static final SocketManager socket = new SocketManager();

  @Override
  public void start(Stage stage) throws IOException {
    ClienteApplication.stage = stage;

    stage.setTitle("Sistema Distribuído - Cliente");

    Parent fxmlLogin = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-view.fxml")));
    loginScene = new Scene(fxmlLogin, 600, 400);

    Parent fxmlPrincipal = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("principal-view.fxml")));
    principalScene = new Scene(fxmlPrincipal, 600, 400);

    exibirDialogoConexao();

    stage.setScene(loginScene);
    stage.show();

    stage.setOnCloseRequest(event -> socket.fecharSocket());
  }


  public static void trocarTela(String tela) {
    switch (tela) {
      case "Principal":
        stage.setScene(principalScene);
        break;

      case "Login":
        stage.setScene(loginScene);
        break;
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}