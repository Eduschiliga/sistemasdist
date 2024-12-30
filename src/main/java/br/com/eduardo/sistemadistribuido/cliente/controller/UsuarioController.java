package br.com.eduardo.sistemadistribuido.cliente.controller;

import br.com.eduardo.sistemadistribuido.entity.Usuario;
import br.com.eduardo.sistemadistribuido.model.dto.UsuarioSemAdminDTO;
import br.com.eduardo.sistemadistribuido.model.request.LocalizarUsuarioRequest;
import br.com.eduardo.sistemadistribuido.model.response.LocalizarUsuarioResponse;
import br.com.eduardo.sistemadistribuido.model.response.MensagemOperacaoResponse;
import br.com.eduardo.sistemadistribuido.util.AlertUtil;
import br.com.eduardo.sistemadistribuido.util.JsonUtil;
import br.com.eduardo.sistemadistribuido.util.SocketManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class UsuarioController {
  private Usuario usuario = new Usuario();

  @FXML
  private Label mensagenErro;

  public void initialize() {
    try {
      String json = JsonUtil.serialize(new LocalizarUsuarioRequest());

      String retorno = SocketManager.enviarSocket(json);

      JsonNode jsonNode = JsonUtil.readTree(retorno);

      switch (jsonNode.get("status").intValue()) {
        case 200:
          mensagenErro.setText("");
          LocalizarUsuarioResponse localizarUsuarioResponse = JsonUtil.treeToValue(jsonNode, LocalizarUsuarioResponse.class);

          UsuarioSemAdminDTO usuarioSemAdmin = localizarUsuarioResponse.getUsuario();

          usuario.setNome(usuarioSemAdmin.getNome());
          usuario.setRa(usuarioSemAdmin.getRa());
          usuario.setSenha(usuarioSemAdmin.getSenha());

          System.out.println("Usuario recebido: " + usuario);

          break;

        case 401:
          MensagemOperacaoResponse erroOperacaoResponse = JsonUtil.treeToValue(jsonNode, MensagemOperacaoResponse.class);

          AlertUtil.alert("Erro!", "Erro ao acessar perfil!", erroOperacaoResponse.getMensagem());

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
}
