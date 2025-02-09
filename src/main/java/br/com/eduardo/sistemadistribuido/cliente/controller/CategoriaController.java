package br.com.eduardo.sistemadistribuido.cliente.controller;

import br.com.eduardo.sistemadistribuido.entity.Categoria;
import br.com.eduardo.sistemadistribuido.model.request.LocalizarCategoriaRequest;
import br.com.eduardo.sistemadistribuido.model.request.OperacaoTokenRequest;
import br.com.eduardo.sistemadistribuido.model.response.BuscarCategoriasResponse;
import br.com.eduardo.sistemadistribuido.model.response.LocalizarCategoriaResponse;
import br.com.eduardo.sistemadistribuido.model.response.MensagemOperacaoResponse;
import br.com.eduardo.sistemadistribuido.util.AlertUtil;
import br.com.eduardo.sistemadistribuido.util.JsonUtil;
import br.com.eduardo.sistemadistribuido.util.SocketManager;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class CategoriaController {
  private Categoria categoriaSelecionada = new Categoria();

  @FXML
  private Label mensagenErro;

  @FXML
  private TextField campoNomeCategoria;
  @FXML
  private TableView<Categoria> tabelaCategorias;
  @FXML
  private TableColumn<Categoria, Long> colunaId;
  @FXML
  private TableColumn<Categoria, String> colunaNome;
  @FXML
  private TableColumn<Categoria, Void> colunaExcluir;

  public void initialize() throws IOException {
    colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
    colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

    pesquisarCategoria();
  }

  public void localizarCategoria(int id) throws IOException {
    LocalizarCategoriaRequest localizarCategoriaResponse = new LocalizarCategoriaRequest();

    localizarCategoriaResponse.setOperacao("localizarCategoria");
    localizarCategoriaResponse.setId(id);

    String json = JsonUtil.serialize(localizarCategoriaResponse);
    String retorno = SocketManager.enviarSocket(json);

    processarLocalizarCategoriaJson(retorno);
  }

  private void processarLocalizarCategoriaJson(String retorno) throws IOException {
    JsonNode jsonNode = JsonUtil.readTree(retorno);

    switch (jsonNode.get("status").intValue()) {
      case 201:
        LocalizarCategoriaResponse buscarCategoriasResponse = JsonUtil.treeToValue(jsonNode, LocalizarCategoriaResponse.class);

        categoriaSelecionada = buscarCategoriasResponse.getCategoria();
        break;

      case 401:
        MensagemOperacaoResponse erroOperacaoResponse = JsonUtil.treeToValue(jsonNode, MensagemOperacaoResponse.class);

        exibirErro(erroOperacaoResponse.getMensagem());
        AlertUtil.alert("Erro", "Erro ao buscar categoria", erroOperacaoResponse.getMensagem());
        break;

      default:
        exibirErro("Erro desconhecido durante o carregamento da categoria.");
        break;
    }
  }

  public void pesquisarCategoria() throws IOException {
    OperacaoTokenRequest operacaoTokenRequest = new OperacaoTokenRequest();

    operacaoTokenRequest.setOperacao("listarCategorias");

    String json = JsonUtil.serialize(operacaoTokenRequest);
    String retorno = SocketManager.enviarSocket(json);

    processarPesquisarCategoriaJson(retorno);
  }

  private void processarPesquisarCategoriaJson(String retorno) throws IOException {
    JsonNode jsonNode = JsonUtil.readTree(retorno);

    switch (jsonNode.get("status").intValue()) {
      case 201:
        BuscarCategoriasResponse buscarCategoriasResponse = JsonUtil.treeToValue(jsonNode, BuscarCategoriasResponse.class);

        List<Categoria> categoriaList = buscarCategoriasResponse.getCategorias();

        carregarDadosTabela(categoriaList);
        break;

      case 401:
        MensagemOperacaoResponse erroOperacaoResponse = JsonUtil.treeToValue(jsonNode, MensagemOperacaoResponse.class);

        exibirErro(erroOperacaoResponse.getMensagem());
        AlertUtil.alert("Erro", "Erro ao buscar categorias", erroOperacaoResponse.getMensagem());
        break;

      default:
        exibirErro("Erro desconhecido durante o carregamento das categorias.");
        break;
    }
  }

  private void carregarDadosTabela(List<Categoria> categoriaList) {
    tabelaCategorias.setItems(FXCollections.observableArrayList(categoriaList));
  }

  private void exibirErro(String mensagem) {
    mensagenErro.setVisible(true);
    mensagenErro.setText(mensagem);
  }
}
