package br.com.eduardo.sistemadistribuido.servidor.controller;

import br.com.eduardo.sistemadistribuido.entity.Categoria;
import br.com.eduardo.sistemadistribuido.repository.CategoriaRepository;
import br.com.eduardo.sistemadistribuido.util.JPAUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.persistence.EntityManager;

import static br.com.eduardo.sistemadistribuido.util.AlertUtil.mostrarErro;
import static br.com.eduardo.sistemadistribuido.util.AlertUtil.mostrarSucesso;

public class CategoriaAdminController {

  private Categoria categoriaSelecionada;
  private final EntityManager entityManager = JPAUtil.getEntityManagerFactory();
  private CategoriaRepository categoriaRepository = new CategoriaRepository(entityManager);

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

  @FXML
  private void salvarCategoria() {
    String nomeCategoria = campoNomeCategoria.getText();
    if (nomeCategoria.isEmpty()) {
      mostrarErro("O nome da categoria não pode estar vazio.");
      return;
    }

    try {
      Categoria novaCategoria;

      System.out.printf("""
                SALVAR CATEGORIA (ADMIN)
                C->S
                {
                    "operacao": "salvarCategoria",
                    "token": "123",
                    "categoria": {
                        "id": %d,
                        "nome": "%s"
                    }
                }
                %n""", categoriaSelecionada != null ? categoriaSelecionada.getCategoriaId() : 0, nomeCategoria);

      if (categoriaSelecionada == null) {
        novaCategoria = new Categoria();
        novaCategoria.setNome(nomeCategoria);
        categoriaRepository.cadastrar(novaCategoria);
        mostrarSucesso("Categoria salva com sucesso.");
        logResposta("salvarCategoria", 201, "Categoria salva com sucesso.");
      } else {
        categoriaSelecionada.setNome(nomeCategoria);
        categoriaRepository.atualizar(categoriaSelecionada);
        mostrarSucesso("Categoria editada com sucesso.");
        logResposta("salvarCategoria", 201, "Categoria salva com sucesso.");
      }

      carregarDadosTabela();
      limparFormulario();
    } catch (Exception e) {
      mostrarErro("Erro ao salvar a categoria.");
      logResposta("salvarCategoria", 401, "Erro ao salvar a categoria.");
    }
  }

  @FXML
  private void excluirCategoria(Categoria categoria) {
    try {
      System.out.printf("""
                EXCLUIR CATEGORIA (ADMIN)
                C->S
                {
                    "operacao": "excluirCategoria",
                    "token": "123",
                    "id": %d
                }
                %n""", categoria.getCategoriaId());

      categoriaRepository.deletar(categoria);
      tabelaCategorias.getItems().remove(categoria);
      logResposta("excluirCategoria", 201, "Exclusão realizada com sucesso.");
    } catch (Exception e) {
      logResposta("excluirCategoria", 401, "Erro ao excluir a categoria.");
    }
  }


  public void initialize() throws JsonProcessingException {
    categoriaRepository = new CategoriaRepository(JPAUtil.getEntityManagerFactory());
    colunaId.setCellValueFactory(new PropertyValueFactory<>("categoriaId"));
    colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

    tabelaCategorias.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal != null) {
        categoriaSelecionada = newVal;
        campoNomeCategoria.setText(categoriaSelecionada.getNome());
      }
    });

    adicionarColunaExcluir();
    carregarDadosTabela();
  }

  private void limparFormulario() {
    categoriaSelecionada = null;
    campoNomeCategoria.clear();
  }

  private void carregarDadosTabela() throws JsonProcessingException {
    tabelaCategorias.setItems(FXCollections.observableArrayList(categoriaRepository.buscarTodos()));
  }

  private void adicionarColunaExcluir() {
    colunaExcluir.setCellFactory(column -> new TableCell<>() {
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

  private void mostrarConfirmacao(Categoria categoria) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
        "Deseja realmente excluir esta categoria?", ButtonType.OK, ButtonType.CANCEL);

    alert.showAndWait().ifPresent(response -> {
      if (response == ButtonType.OK) {
        excluirCategoria(categoria);
      }
    });
  }

  private void logResposta(String operacao, int status, String mensagem) {
    System.out.printf("""
            %s (ADMIN)
            S->C
            {
                "status": %d,
                "operacao": "%s",
                "mensagem": "%s"
            }
            %n""", operacao.toUpperCase(), status, operacao, mensagem);
  }
}