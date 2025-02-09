package br.com.eduardo.sistemadistribuido.cliente.controller;

import br.com.eduardo.sistemadistribuido.cliente.ClienteApplication;
import br.com.eduardo.sistemadistribuido.entity.Usuario;
import br.com.eduardo.sistemadistribuido.model.dto.UsuarioSemAdminDTO;
import br.com.eduardo.sistemadistribuido.model.request.EditarUsuarioRequest;
import br.com.eduardo.sistemadistribuido.model.request.ExcluirUsuarioRequest;
import br.com.eduardo.sistemadistribuido.model.request.LocalizarUsuarioRequest;
import br.com.eduardo.sistemadistribuido.model.response.LocalizarUsuarioResponse;
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

import static br.com.eduardo.sistemadistribuido.util.AlertUtil.mostrarSucesso;

public class UsuarioController {

  private Usuario usuario = new Usuario();

  @FXML
  private Label mensagenErro;

  @FXML
  private TextField inputRa;

  @FXML
  private TextField inputNome;

  @FXML
  private PasswordField inputSenha;

  @FXML
  private Button botaoSalvar;

  @FXML
  private Button botaoExcluir;

  public void initialize() {
    try {
      String json = JsonUtil.serialize(new LocalizarUsuarioRequest());
      String retorno = SocketManager.enviarSocket(json);
      processarRespostaJson(retorno);
    } catch (JsonProcessingException e) {
      exibirErro("Erro ao serializar o usuário: " + e.getMessage());
    } catch (IOException e) {
      exibirErro("Erro ao conectar ao servidor: " + e.getMessage());
    }
  }


  private void processarRespostaJson(String retorno) throws IOException {
    JsonNode jsonNode = JsonUtil.readTree(retorno);

    switch (jsonNode.get("status").intValue()) {
      case 201:
        mensagenErro.setVisible(false);
        LocalizarUsuarioResponse localizarUsuarioResponse = JsonUtil.treeToValue(jsonNode, LocalizarUsuarioResponse.class);
        UsuarioSemAdminDTO usuarioSemAdmin = localizarUsuarioResponse.getUsuario();

        usuario.setNome(usuarioSemAdmin.getNome());
        usuario.setRa(usuarioSemAdmin.getRa());
        usuario.setSenha(usuarioSemAdmin.getSenha());

        carregarDadosUsuario();
        break;

      case 401:
        MensagemOperacaoResponse erroOperacaoResponse = JsonUtil.treeToValue(jsonNode, MensagemOperacaoResponse.class);

        exibirErro(erroOperacaoResponse.getMensagem());
        AlertUtil.alert("Erro", "Erro ao acessar perfil", erroOperacaoResponse.getMensagem());
        break;

      default:
        exibirErro("Erro desconhecido durante o carregamento do usuário.");
        break;
    }
  }

  private void carregarDadosUsuario() {
    if (usuario != null) {
      inputRa.setText(usuario.getRa());
      inputNome.setText(usuario.getNome());
      inputSenha.setText(usuario.getSenha());
    } else {
      exibirErro("Usuário não encontrado.");
    }
  }

  private void exibirErro(String mensagem) {
    mensagenErro.setVisible(true);
    mensagenErro.setText(mensagem);
  }

  public void salvarUsuario(ActionEvent event) throws IOException {
    EditarUsuarioRequest editarUsuarioRequest = new EditarUsuarioRequest();

    editarUsuarioRequest.getUsuario().setNome(inputNome.getText());
    editarUsuarioRequest.getUsuario().setRa(usuario.getRa());
    editarUsuarioRequest.getUsuario().setSenha(inputSenha.getText());

    String json = JsonUtil.serialize(editarUsuarioRequest);
    String retorno = SocketManager.enviarSocket(json);

    processarEdicaoRespostaJson(retorno);
  }

  private void processarEdicaoRespostaJson(String retorno) throws IOException {
    JsonNode jsonNode = JsonUtil.readTree(retorno);

    switch (jsonNode.get("status").intValue()) {
      case 201:
        mostrarSucesso("Usuário editado com sucesso!");
        break;

      case 401:
        MensagemOperacaoResponse erroOperacaoResponse = JsonUtil.treeToValue(jsonNode, MensagemOperacaoResponse.class);

        exibirErro(erroOperacaoResponse.getMensagem());
        AlertUtil.alert("Erro", "Erro ao acessar perfil", erroOperacaoResponse.getMensagem());
        break;

      default:
        exibirErro("Erro desconhecido durante o carregamento do usuário.");
        break;
    }
  }

  private void processarExclusaoRespostaJson(String retorno) throws IOException {
    JsonNode jsonNode = JsonUtil.readTree(retorno);

    switch (jsonNode.get("status").intValue()) {
      case 201:
        mostrarSucesso("Usuário excluído com sucesso!");

        ClienteApplication.token = "";
        ClienteApplication.trocarTela("Login");
        break;

      case 401:
        MensagemOperacaoResponse erroOperacaoResponse = JsonUtil.treeToValue(jsonNode, MensagemOperacaoResponse.class);

        exibirErro(erroOperacaoResponse.getMensagem());
        AlertUtil.alert("Erro", "Erro ao excluir perfil", erroOperacaoResponse.getMensagem());
        break;

      default:
        exibirErro("Erro desconhecido durante a exclusão do usuário.");
        break;
    }
  }

  public void excluirUsuario(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmação de Exclusão");
    alert.setHeaderText("Deseja realmente excluir o usuário?");
    alert.setContentText("Usuário: " + inputNome.getText());

    alert.showAndWait().ifPresent(response -> {
      if (response == ButtonType.OK) {
        try {
          String json = JsonUtil.serialize(new ExcluirUsuarioRequest());
          String retorno = SocketManager.enviarSocket(json);

          processarExclusaoRespostaJson(retorno);
        } catch (IOException e) {
          exibirErro("Erro ao tentar excluir o usuário: " + e.getMessage());
        }
      }
    });
  }
}