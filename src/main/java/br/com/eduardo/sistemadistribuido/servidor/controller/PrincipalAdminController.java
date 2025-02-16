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
  void visualizarPerfil(ActionEvent event) throws IOException {
    setTela("VisualizarPerfil");
  }

  @FXML
  void visualizarAvisos(ActionEvent event) throws IOException {
    setTela("VisualizarAvisos");
  }

  @FXML
  void localizarUsuario(ActionEvent event) throws IOException {
    setTela("LocalizarUsuario");
  }

  @FXML
  public void cadastrarAviso(ActionEvent event) throws IOException {
    setTela("CadastrarAviso");
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

      case "VisualizarPerfil":
        Parent fxmlVisualizarPerfil = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/br/com/eduardo/sistemadistribuido/servidor/usuario-admin-view.fxml")));

        if (fxmlVisualizarPerfil != null) {
          principal.setCenter(fxmlVisualizarPerfil);
        } else {
          AlertUtil.alert("Erro", "Erro", "Tela 'VisualizarPerfil' não carregada.");
        }
        break;


      case "VisualizarAvisos":
        Parent fxmlVisualizarAvisos = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/br/com/eduardo/sistemadistribuido/servidor/visualizar-aviso-admin.fxml")));

        if (fxmlVisualizarAvisos != null) {
          principal.setCenter(fxmlVisualizarAvisos);
        } else {
          AlertUtil.alert("Erro", "Erro", "Tela 'VisualizarAvisos' não carregada.");
        }
        break;

      case "CadastrarAviso":
        Parent fxmlAviso = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/br/com/eduardo/sistemadistribuido/servidor/aviso-admin-view.fxml")));

        if (fxmlAviso != null) {
          principal.setCenter(fxmlAviso);
        } else {
          AlertUtil.alert("Erro", "Erro", "Tela 'CadastrarAviso' não carregada.");
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
    setTela("VisualizarAvisos");
  }
}
