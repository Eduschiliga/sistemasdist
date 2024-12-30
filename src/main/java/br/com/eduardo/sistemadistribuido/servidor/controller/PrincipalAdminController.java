package br.com.eduardo.sistemadistribuido.servidor.controller;

import br.com.eduardo.sistemadistribuido.util.AlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Objects;

public class PrincipalAdminController {
  private Parent fxmlHome;

  @FXML
  private BorderPane principal;

  @FXML
  void visualizar(ActionEvent event) throws IOException {
    setTela("Home");
  }

  @FXML
  void localizarUsuario(ActionEvent event) throws IOException {
    setTela("LocalizarUsuario");
  }

  @FXML
  public void cadastrarCategoria(ActionEvent event) throws IOException {
    setTela("CadastrarCategoria");
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

      case "LocalizarUsuario":
        Parent fxmlLocalizarUsuario = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/br/com/eduardo/sistemadistribuido/servidor/visualizar-usuario-admin-view.fxml")));

        if (fxmlLocalizarUsuario != null) {
          principal.setCenter(fxmlLocalizarUsuario);
        } else {
          AlertUtil.alert("Erro", "Erro", "Tela 'LocalizarUsuario' não carregada.");
        }
        break;

      case "CadastrarCategoria":
        Parent fxmlCategoria = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/br/com/eduardo/sistemadistribuido/servidor/categoria-admin-view.fxml")));

        if (fxmlCategoria != null) {
          principal.setCenter(fxmlCategoria);
        } else {
          AlertUtil.alert("Erro", "Erro", "Tela 'CadastrarCategoria' não carregada.");
        }
        break;
    }
  }

  @FXML
  void initialize() throws IOException {
    try {
      fxmlHome = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/br/com/eduardo/sistemadistribuido/servidor/home-admin-view.fxml")));

    } catch (IOException e) {
      setTela("Home");
    } finally {
      setTela("Home");
    }
  }
}
