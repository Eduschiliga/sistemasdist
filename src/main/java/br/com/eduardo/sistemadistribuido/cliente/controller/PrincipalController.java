package br.com.eduardo.sistemadistribuido.cliente.controller;

import br.com.eduardo.sistemadistribuido.service.LogoutService;
import br.com.eduardo.sistemadistribuido.util.AlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;

public class PrincipalController {
  private Parent fxmlHome;
  private Parent fxmlVincularAviso;
  private Parent fxmlVisualizarAviso;

  @FXML
  private ResourceBundle resources;

  @FXML
  private BorderPane principal;

  @FXML
  void visualizar(ActionEvent event) {
    setTela("Home");
  }

  @FXML
  void vincularAviso(ActionEvent event) {
    setTela("VincularAviso");
  }

  @FXML
  void visualizarAviso(ActionEvent event) {
    setTela("VisualizarAviso");
  }

  @FXML
  void deslogar(ActionEvent event) {
    LogoutService.logout();
  }

  private void setTela(String tela) {
    switch (tela) {
      case "Home":
        if (fxmlHome != null) {
          principal.setCenter(fxmlHome);
        } else {
          AlertUtil.alert("Erro", "Erro", "Tela 'Home' não carregada.");
        }
        break;

      case "VincularAviso":
        if (fxmlVincularAviso != null) {
          principal.setCenter(fxmlVincularAviso);
        } else {
          AlertUtil.alert("Erro", "Erro", "Tela 'VincularAviso' não carregada.");
        }
        break;

      case "VisualizarAviso":
        if (fxmlVisualizarAviso != null) {
          principal.setCenter(fxmlVisualizarAviso);
        } else {
          AlertUtil.alert("Erro", "Erro", "Tela 'VisualizarAviso' não carregada.");
        }
        break;
    }
  }

  @FXML
  void initialize() {
    try {
      fxmlHome = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/br/com/eduardo/sistemadistribuido/cliente/home-view.fxml")));
      fxmlVisualizarAviso = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/br/com/eduardo/sistemadistribuido/cliente/visualizar-aviso.fxml")));
      fxmlVincularAviso = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/br/com/eduardo/sistemadistribuido/cliente/vincular-aviso.fxml")));

      principal.setCenter(fxmlHome);
    } catch (IOException e) {
      AlertUtil.alert("Erro", "Carregamento de tela", "Erro ao carregar tela principal.");
    }
  }
}
