package br.com.eduardo.sistemadistribuido.cliente.controller;

import br.com.eduardo.sistemadistribuido.entity.Aviso;
import br.com.eduardo.sistemadistribuido.model.request.ListarAvisosRequest;
import br.com.eduardo.sistemadistribuido.model.request.ListarUsuarioAvisosRequest;
import br.com.eduardo.sistemadistribuido.model.response.ListarAvisosResponse;
import br.com.eduardo.sistemadistribuido.model.response.ListarUsuarioAvisosResponse;
import br.com.eduardo.sistemadistribuido.model.response.MensagemOperacaoResponse;
import br.com.eduardo.sistemadistribuido.util.AlertUtil;
import br.com.eduardo.sistemadistribuido.util.JsonUtil;
import br.com.eduardo.sistemadistribuido.util.SocketManager;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.List;

public class VisualizarAvisoCadastradoController {
  @FXML
  private ListView<String> listViewAvisos;

  @FXML
  private Label mensagenErro;

  public void initialize() throws IOException {
    ListarUsuarioAvisosRequest listarAvisosRequest = new ListarUsuarioAvisosRequest();

    String json = JsonUtil.serialize(listarAvisosRequest);
    String retorno = SocketManager.enviarSocket(json);

    processarListarAvisosJson(retorno);
  }

  private void processarListarAvisosJson(String retorno) throws IOException {
    JsonNode jsonNode = JsonUtil.readTree(retorno);

    switch (jsonNode.get("status").intValue()) {
      case 201:
        ListarUsuarioAvisosResponse listarAvisosResponse = JsonUtil.treeToValue(jsonNode, ListarUsuarioAvisosResponse.class);

        List<Aviso> avisoList = listarAvisosResponse.getAvisos();

        setAvisos(avisoList);

        break;

      case 401:
        MensagemOperacaoResponse erroOperacaoResponse = JsonUtil.treeToValue(jsonNode, MensagemOperacaoResponse.class);

        exibirErro(erroOperacaoResponse.getMensagem());
        AlertUtil.alert("Erro", "Erro ao buscar avisos", erroOperacaoResponse.getMensagem());
        break;

      default:
        exibirErro("Erro desconhecido durante o carregamento das avisos.");
        break;
    }
  }

  private void exibirErro(String mensagem) {
    mensagenErro.setVisible(true);
    mensagenErro.setText(mensagem);
  }

  public void setAvisos(List<Aviso> avisos) {
    listViewAvisos.getItems().clear();

    for (Aviso aviso : avisos) {
      String item = formatarAvisoParaExibicao(aviso);
      listViewAvisos.getItems().add(item);
    }
  }

  private String formatarAvisoParaExibicao(Aviso aviso) {
    return String.format(
        "ID: %d\nTítulo: %s\nDescrição: %s\nCategoria: %s",
        aviso.getId(),
        aviso.getTitulo(),
        aviso.getDescricao(),
        aviso.getNomeCategoria()
    );
  }
}
