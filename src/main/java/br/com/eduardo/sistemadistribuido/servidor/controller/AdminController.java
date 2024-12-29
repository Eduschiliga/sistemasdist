package br.com.eduardo.sistemadistribuido.servidor.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminController {
  @FXML
  private Label welcomeText;

  @FXML
  protected void goToLoginView(ActionEvent event) {
    try {
      // Carrega o arquivo login-view.fxm+l
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
      Parent loginView = fxmlLoader.load();

      // Obtém o palco atual (janela atual) a partir do botão que disparou o evento
      Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

      // Cria e define a nova cena para o estágio
      Scene scene = new Scene(loginView);
      stage.setScene(scene);

      // Exibe a nova cena
      stage.show();
    } catch (IOException e) {
      e.printStackTrace(); // Log do erro para depuração
    }
  }
}
