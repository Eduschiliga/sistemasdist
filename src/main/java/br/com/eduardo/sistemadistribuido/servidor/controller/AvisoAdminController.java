package br.com.eduardo.sistemadistribuido.servidor.controller;

import br.com.eduardo.sistemadistribuido.entity.Aviso;
import br.com.eduardo.sistemadistribuido.entity.Categoria;
import br.com.eduardo.sistemadistribuido.repository.AvisoRepository;
import br.com.eduardo.sistemadistribuido.repository.CategoriaRepository;
import br.com.eduardo.sistemadistribuido.util.JPAUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.persistence.EntityManager;

import static br.com.eduardo.sistemadistribuido.util.AlertUtil.mostrarErro;
import static br.com.eduardo.sistemadistribuido.util.AlertUtil.mostrarSucesso;

public class AvisoAdminController {

  private Aviso avisoSelecionado;
  private final EntityManager entityManager = JPAUtil.getEntityManagerFactory();
  private final AvisoRepository avisoRepository = new AvisoRepository(entityManager);
  private final CategoriaRepository categoriaRepository = new CategoriaRepository(entityManager);

  @FXML
  private ComboBox<Categoria> campoCategoriaAviso;

  @FXML
  private TextField campoTituloAviso;

  @FXML
  private TextField campoDescricaoAviso;

  @FXML
  private TableView<Aviso> tabelaAvisos;

  @FXML
  private TableColumn<Aviso, Long> colunaId;

  @FXML
  private TableColumn<Aviso, String> colunaNome;

  @FXML
  private TableColumn<Aviso, String> colunaDescricao;

  @FXML
  private TableColumn<Aviso, String> colunaNomeCategoria;

  @FXML
  private TableColumn<Aviso, Void> colunaExcluir;

  @FXML
  private void salvarAviso() {
    String tituloAviso = campoTituloAviso.getText();
    String descricaoAviso = campoDescricaoAviso.getText();
    Categoria categoriaAviso = campoCategoriaAviso.getValue();

    if (tituloAviso.isEmpty() || descricaoAviso.isEmpty() || categoriaAviso == null) {
      mostrarErro("O nome ou descrição do aviso não pode estar vazio.");
      return;
    }

    try {
      if (avisoSelecionado == null) {
        System.out.printf(
            """
            {
                "operacao": "salvarAviso",
                "token": "9999999",
                "aviso": {
                   "id": 0,
                   "categoria": %d,
                   "titulo": "%s",
                   "descricao": "%s"
                }
            }
            """, categoriaAviso.getCategoriaId(), avisoSelecionado.getCategoria(), avisoSelecionado.getDescricao()
        );

        Aviso novoAviso = new Aviso();

        novoAviso.setId(0L);
        novoAviso.setTitulo(tituloAviso);
        novoAviso.setDescricao(descricaoAviso);
        novoAviso.setCategoria(categoriaAviso);

        avisoRepository.cadastrar(novoAviso);

        mostrarSucesso("Aviso salva com sucesso.");

        logResposta("salvarAviso", 201, "Aviso salvo com sucesso.");
      } else {
        avisoSelecionado.setTitulo(tituloAviso);
        avisoSelecionado.setDescricao(descricaoAviso);
        avisoSelecionado.setCategoria(categoriaAviso);

        avisoRepository.atualizar(avisoSelecionado);

        mostrarSucesso("Aviso editado com sucesso.");

        logResposta("salvarAviso", 201, "Aviso salva com sucesso.");
      }

      carregarDadosTabela();
      limparFormulario();
    } catch (Exception e) {
      mostrarErro("Erro ao salvar a aviso.");
      logResposta("salvarAviso", 401, "Erro ao salvar a aviso.");
    }
  }

  @FXML
  private void excluirAviso(Aviso aviso) {
    try {
      avisoRepository.deletar(aviso);
      tabelaAvisos.getItems().remove(aviso);
      logResposta("excluirAviso", 201, "Exclusão realizada com sucesso.");
    } catch (Exception e) {
      logResposta("excluirAviso", 401, "Erro ao excluir a aviso.");
    }
  }

  public void initialize() {
    colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
    colunaNome.setCellValueFactory(new PropertyValueFactory<>("titulo"));
    colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
    colunaNomeCategoria.setCellValueFactory(new PropertyValueFactory<>("nomeCategoria"));

    tabelaAvisos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal != null) {
        avisoSelecionado = newVal;
        campoTituloAviso.setText(avisoSelecionado.getTitulo());
        campoDescricaoAviso.setText(avisoSelecionado.getDescricao());
        campoCategoriaAviso.setValue(avisoSelecionado.getCategoria());
      }
    });

    adicionarColunaExcluir();
    carregarDadosTabela();
    carregarCategoria();
  }

  private void carregarCategoria() {
    campoCategoriaAviso.setItems(FXCollections.observableArrayList(categoriaRepository.buscarTodos()));
  }

  private void limparFormulario() {
    avisoSelecionado = null;
    campoTituloAviso.clear();
    campoDescricaoAviso.clear();
    campoCategoriaAviso.setValue(null);
  }

  private void carregarDadosTabela() {
    tabelaAvisos.setItems(FXCollections.observableArrayList(avisoRepository.buscarTodos()));
    tabelaAvisos.refresh();
  }

  private void adicionarColunaExcluir() {
    colunaExcluir.setCellFactory((_) -> new TableCell<>() {
      private final Button btnExcluir = new Button("Excluir");

      {
        btnExcluir.setOnAction(event -> mostrarConfirmacao(getTableView().getItems().get(getIndex())));
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty ? null : btnExcluir);
      }
    });
  }

  private void mostrarConfirmacao(Aviso aviso) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
        "Deseja realmente excluir esta aviso?", ButtonType.OK, ButtonType.CANCEL);

    alert.showAndWait().ifPresent(response -> {
      if (response == ButtonType.OK) {
        excluirAviso(aviso);
      }
    });
  }

  private void logResposta(String operacao, int status, String mensagem) {
    System.out.printf("""
        {
          "status": %d,
          "operacao": "%s",
          "mensagem": "%s"
        }
        """, status, operacao, mensagem);
  }
}