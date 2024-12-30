package br.com.eduardo.sistemadistribuido.cliente.controller;

import br.com.eduardo.sistemadistribuido.cliente.service.LogoutService;
import br.com.eduardo.sistemadistribuido.util.AlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Objects;

public class PrincipalController {
  private Parent fxmlHome;

  @FXML
  private BorderPane principal;

  @FXML
  void visualizar(ActionEvent event) throws IOException {
    setTela("Home");
  }

  @FXML
  void vincularAviso(ActionEvent event) throws IOException {
    setTela("VincularAviso");
  }

  @FXML
  void visualizarUsuario(ActionEvent event) throws IOException {
    setTela("VisualizarUsuario");
  }

  @FXML
  void visualizarAviso(ActionEvent event) throws IOException {
    setTela("VisualizarAviso");
  }

  @FXML
  void deslogar(ActionEvent event) {
    LogoutService.logout();
  }

  private void setTela(String tela) throws IOException {
    switch (tela) {
      case "Home":
        if (fxmlHome != null) {
          principal.setCenter(fxmlHome);
        } else {
          AlertUtil.alert("Erro", "Erro", "Tela 'Home' não carregada.");
        }
        break;

      case "VincularAviso":
        Parent fxmlVincularAviso = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/br/com/eduardo/sistemadistribuido/cliente/vincular-aviso.fxml")));

        if (fxmlVincularAviso != null) {
          principal.setCenter(fxmlVincularAviso);
        } else {
          AlertUtil.alert("Erro", "Erro", "Tela 'VincularAviso' não carregada.");
        }
        break;

      case "VisualizarAviso":
        Parent fxmlVisualizarAviso = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/br/com/eduardo/sistemadistribuido/cliente/visualizar-aviso.fxml")));

        if (fxmlVisualizarAviso != null) {
          principal.setCenter(fxmlVisualizarAviso);
        } else {
          AlertUtil.alert("Erro", "Erro", "Tela 'VisualizarAviso' não carregada.");
        }
        break;

      case "VisualizarUsuario":
        Parent fxmlVisualizarUsuario = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/br/com/eduardo/sistemadistribuido/cliente/usuario-view.fxml")));

        if (fxmlVisualizarUsuario != null) {
          principal.setCenter(fxmlVisualizarUsuario);
        } else {
          AlertUtil.alert("Erro", "Erro", "Tela 'VisualizarUsuario' não carregada.");
        }
        break;
    }
  }

  @FXML
  void initialize() throws IOException {
    try {
      fxmlHome = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/br/com/eduardo/sistemadistribuido/cliente/home-view.fxml")));

    } catch (IOException e) {
      setTela("Home");
    } finally {
      setTela("Home");
    }
  }
}
