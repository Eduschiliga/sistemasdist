package br.com.eduardo.sistemadistribuido.cliente.controller;

import br.com.eduardo.sistemadistribuido.cliente.ClienteApplication;
import br.com.eduardo.sistemadistribuido.model.request.CadastroUsuarioRequest;
import br.com.eduardo.sistemadistribuido.model.response.MensagemOperacaoResponse;
import br.com.eduardo.sistemadistribuido.util.AlertUtil;
import br.com.eduardo.sistemadistribuido.util.JsonUtil;
import br.com.eduardo.sistemadistribuido.util.SocketManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class CadastroUsuarioController {
  @FXML
  private Label mensagenErro;

  @FXML
  private TextField nome;

  @FXML
  private TextField ra;

  @FXML
  private PasswordField senha;

  @FXML
  private Button cadastrar;

  @FXML
  private Hyperlink login;

  public void efetuarCadastro(ActionEvent event) {
    CadastroUsuarioRequest cadastroRequest = new CadastroUsuarioRequest();

    cadastroRequest.setNome(nome.getText());
    cadastroRequest.setRa(ra.getText());
    cadastroRequest.setSenha(senha.getText());

    // TODO: validar campos
    if (cadastroRequest.getRa().isEmpty() || cadastroRequest.getSenha().isEmpty() || nome.getText().isEmpty()) {
      mensagenErro.setText("Preencha todos os campos.");
      return;
    }

    try {
      String json = JsonUtil.serialize(cadastroRequest);

      String retorno = SocketManager.enviarSocket(json);

      JsonNode jsonNode = JsonUtil.readTree(retorno);

      switch (jsonNode.get("status").intValue()) {
        case 201:
          mensagenErro.setText("");

          AlertUtil.alert("Sucesso", "Cadastro Realizado", "Cadastro realizado com sucesso!");

          ClienteApplication.trocarTela("Login");
          break;

        case 401:
          MensagemOperacaoResponse erroOperacaoResponse = JsonUtil.treeToValue(jsonNode, MensagemOperacaoResponse.class);

          AlertUtil.alert("Erro!", "Erro ao realizar cadastro", erroOperacaoResponse.getMensagem());


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

  public void efetuarLogin(ActionEvent event) throws IOException {
    ClienteApplication.trocarTela("Login");
  }
}
