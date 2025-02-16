package br.com.eduardo.sistemadistribuido.cliente.controller;

import br.com.eduardo.sistemadistribuido.entity.Categoria;
import br.com.eduardo.sistemadistribuido.model.request.*;
import br.com.eduardo.sistemadistribuido.model.response.*;
import br.com.eduardo.sistemadistribuido.util.AlertUtil;
import br.com.eduardo.sistemadistribuido.util.JsonUtil;
import br.com.eduardo.sistemadistribuido.util.SocketManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class CategoriaController {
  private Categoria categoriaSelecionada = new Categoria();

  @FXML
  private Label mensagenErro;

  @FXML
  private Button categoriaCadastradas;

  @FXML
  private TableView<Categoria> tabelaCategorias;
  @FXML
  private TableColumn<Categoria, Long> colunaId;
  @FXML
  private TableColumn<Categoria, String> colunaNome;
  @FXML
  private TableColumn<Categoria, Void> colunaCadastrar;
  @FXML
  private TableColumn<Categoria, Void> colunaDescadastrar;


  public void initialize() throws IOException {
    colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
    colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

    adicionarColunaCadastrar();
    adicionarColunaDescadastrar();

    pesquisarCategoria();
  }

  private void adicionarColunaCadastrar() {
    colunaCadastrar.setCellFactory(column -> new TableCell<>() {
      private final Button btnCadastrar = new Button("Cadastrar");

      {
        btnCadastrar
            .setOnAction(event -> mostrarConfirmacaoCadastrar(getTableView().getItems().get(getIndex())));
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty ? null : btnCadastrar);
      }
    });
  }

  private void adicionarColunaDescadastrar() {
    colunaDescadastrar.setCellFactory(column -> new TableCell<>() {
      private final Button btnDescadastrar = new Button("Descadastrar");

      {
        btnDescadastrar
            .setOnAction(event -> mostrarConfirmacaoDescadastrar(getTableView().getItems().get(getIndex())));
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty ? null : btnDescadastrar);
      }
    });
  }

  private void mostrarConfirmacaoDescadastrar(Categoria categoria) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
        "Deseja realmente se DESCADASTRAR desta categoria?\nNome: " + categoria.getNome() + "\n#ID: " + categoria.getId(),
        ButtonType.OK, ButtonType.CANCEL);

    alert.showAndWait().ifPresent(response -> {
      if (response == ButtonType.OK) {
        DescadastrarUsuarioCategoriaRequest descadastrarUsuarioCategoriaRequest = new DescadastrarUsuarioCategoriaRequest();
        descadastrarUsuarioCategoriaRequest.setCategoria(categoria.getId());

        try {
          String json = JsonUtil.serialize(descadastrarUsuarioCategoriaRequest);
          String retorno = SocketManager.enviarSocket(json);

          processarDescadastroCategoriaJson(retorno);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }

  private void processarDescadastroCategoriaJson(String retorno) throws IOException {
    JsonNode jsonNode = JsonUtil.readTree(retorno);

    switch (jsonNode.get("status").intValue()) {
      case 201:
        DescadastrarUsuarioCategoriaResponse cadastrarUsuarioCategoriaResponse = JsonUtil.treeToValue(jsonNode, DescadastrarUsuarioCategoriaResponse.class);

        AlertUtil.alert("Erro", "Descadastro realizado com sucesso!", cadastrarUsuarioCategoriaResponse.getMensagem());
        break;

      case 401:
        MensagemOperacaoResponse erroOperacaoResponse = JsonUtil.treeToValue(jsonNode, MensagemOperacaoResponse.class);

        exibirErro(erroOperacaoResponse.getMensagem());
        AlertUtil.alert("Erro", "Erro ao se descadastrar a categoria", erroOperacaoResponse.getMensagem());
        break;

      default:
        exibirErro("Erro desconhecido durante o carregamento da categoria.");
        break;
    }
  }

  private void mostrarConfirmacaoCadastrar(Categoria categoria) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
        "Deseja realmente se CADASTRAR nesta categoria?\nNome: " + categoria.getNome() + "\n#ID: " + categoria.getId(),
        ButtonType.OK, ButtonType.CANCEL);

    alert.showAndWait().ifPresent(response -> {
      if (response == ButtonType.OK) {
        CadastrarUsuarioCategoriaRequest cadastrarUsuarioCategoriaRequest = new CadastrarUsuarioCategoriaRequest();
        cadastrarUsuarioCategoriaRequest.setCategoria(categoria.getId());

        try {
          String json = JsonUtil.serialize(cadastrarUsuarioCategoriaRequest);
          String retorno = SocketManager.enviarSocket(json);

          processarCadastroCategoriaJson(retorno);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }

  private void processarCadastroCategoriaJson(String retorno) throws IOException {
    JsonNode jsonNode = JsonUtil.readTree(retorno);

    switch (jsonNode.get("status").intValue()) {
      case 201:
        CadastrarUsuarioCategoriaResponse cadastrarUsuarioCategoriaResponse = JsonUtil.treeToValue(jsonNode, CadastrarUsuarioCategoriaResponse.class);

        AlertUtil.alert("Erro", "Cadastro realizado com sucesso!", cadastrarUsuarioCategoriaResponse.getMensagem());
        break;

      case 401:
        MensagemOperacaoResponse erroOperacaoResponse = JsonUtil.treeToValue(jsonNode, MensagemOperacaoResponse.class);

        exibirErro(erroOperacaoResponse.getMensagem());
        AlertUtil.alert("Erro", "Erro ao se cadastrar a categoria", erroOperacaoResponse.getMensagem());
        break;

      default:
        exibirErro("Erro desconhecido durante o carregamento da categoria.");
        break;
    }
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

  public void buscarCategoriasCadastradas(ActionEvent event) throws IOException {
    ListarUsuarioCategoriaRequest listarUsuarioCategoriaRequest = new ListarUsuarioCategoriaRequest();


    String json = JsonUtil.serialize(listarUsuarioCategoriaRequest);
    String retorno = SocketManager.enviarSocket(json);

    processarListarUsuarioCategoriaRequestJson(retorno);
  }

  private void processarListarUsuarioCategoriaRequestJson(String retorno) throws IOException {
    JsonNode jsonNode = JsonUtil.readTree(retorno);

    switch (jsonNode.get("status").intValue()) {
      case 201:
        ListarUsuarioCategoriaResponse buscarCategoriasResponse = JsonUtil.treeToValue(jsonNode, ListarUsuarioCategoriaResponse.class);

        List<Categoria> categoriaList = buscarCategoriasResponse.getCategorias();

        StringBuilder cat = new StringBuilder();

        categoriaList.forEach(categoria -> {
          cat.append("ID: ").append(categoria.getId()).append(" - ").append(categoria.getNome()).append("\n");
        });

        AlertUtil.alert(
            "Erro",
            "Lista de Categorias vinculadas ao Usuário",
            cat.toString());
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
}
