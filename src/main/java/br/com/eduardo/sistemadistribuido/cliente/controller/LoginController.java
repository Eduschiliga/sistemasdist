package br.com.eduardo.sistemadistribuido.cliente.controller;

import br.com.eduardo.sistemadistribuido.cliente.ClienteApplication;
import br.com.eduardo.sistemadistribuido.model.request.LoginRequest;
import br.com.eduardo.sistemadistribuido.model.response.LoginSucessoResponse;
import br.com.eduardo.sistemadistribuido.model.response.MensagemOperacaoResponse;
import br.com.eduardo.sistemadistribuido.util.AlertUtil;
import br.com.eduardo.sistemadistribuido.util.JsonUtil;
import br.com.eduardo.sistemadistribuido.util.SocketManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
  @FXML
  private Label mensagenErro;

  @FXML
  private TextField ra;

  @FXML
  private PasswordField senha;

  @FXML
  private Button entrar;

  @FXML
  private Hyperlink cadastrar;

  public void efetuarLogin(ActionEvent event) {
    LoginRequest loginRequest = new LoginRequest();

    loginRequest.setRa(ra.getText());
    loginRequest.setSenha(senha.getText());

    if (loginRequest.getRa().isEmpty() || loginRequest.getSenha().isEmpty()) {
      mensagenErro.setText("Preencha todos os campos.");
      return;
    }

    try {
      String json = JsonUtil.serialize(loginRequest);

      String retorno = SocketManager.enviarSocket(json);

      JsonNode jsonNode = JsonUtil.readTree(retorno);

      switch (jsonNode.get("status").intValue()) {
        case 200:
          mensagenErro.setText("");

          LoginSucessoResponse mensagemLoginSucesso = JsonUtil.treeToValue(jsonNode, LoginSucessoResponse.class);
          ClienteApplication.token = mensagemLoginSucesso.getToken();

          ClienteApplication.trocarTela("Principal");
          break;

        case 401:
          MensagemOperacaoResponse erroOperacaoResponse = JsonUtil.treeToValue(jsonNode, MensagemOperacaoResponse.class);

          AlertUtil.alert("Erro!", "Erro ao realizar o login!", erroOperacaoResponse.getMensagem());

          mensagenErro.setText(erroOperacaoResponse.getMensagem());
          break;

        default:
          mensagenErro.setText("Erro desconhecido.");
          break;
      }
    } catch (JsonProcessingException e) {
      mensagenErro.setText("Erro ao serializar o usuário: " + e.getMessage());
    } catch (IOException e) {
      mensagenErro.setText("Erro: " + e.getMessage());
    }
  }

  public void efetuarCadastro(ActionEvent event) {
    try {
      Parent cadastroFXML = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/br/com/eduardo/sistemadistribuido/cliente/cadastro-usuario-view.fxml")));

      Scene cadastroScene = new Scene(cadastroFXML);
      Stage stage = (Stage) ((Hyperlink) event.getSource()).getScene().getWindow();
      stage.setScene(cadastroScene);
      stage.setTitle("Cadastro de Usuário");
    } catch (IOException e) {
      mensagenErro.setText("Erro ao abrir a tela de cadastro.");
      AlertUtil.alert("Erro!", "Erro ao abrir a tela de cadastro!", e.getMessage());
    }
  }
}