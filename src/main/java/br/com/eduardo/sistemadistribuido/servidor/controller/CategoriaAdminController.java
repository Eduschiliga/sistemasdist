package br.com.eduardo.sistemadistribuido.servidor.controller;

import br.com.eduardo.sistemadistribuido.entity.Categoria;
import br.com.eduardo.sistemadistribuido.repository.CategoriaRepository;
import br.com.eduardo.sistemadistribuido.util.JPAUtil;
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
          {
              "operacao": "salvarCategoria",
              "token": "99999",
              "categoria": {
                  "id": %d,
                  "nome": "%s"
              }
          }
          %n""", categoriaSelecionada != null ? categoriaSelecionada.getId() : 0, nomeCategoria);

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
          {
              "operacao": "excluirCategoria",
              "token": "99999",
              "id": %d
          }
          %n""", categoria.getId());

      categoriaRepository.deletar(categoria);
      tabelaCategorias.getItems().remove(categoria);
      logResposta("excluirCategoria", 201, "Exclusão realizada com sucesso.");
    } catch (Exception e) {
      logResposta("excluirCategoria", 401, "Erro ao excluir a categoria.");
    }
  }


  public void initialize() {
    categoriaRepository = new CategoriaRepository(JPAUtil.getEntityManagerFactory());
    colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
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

  private void carregarDadosTabela() {
    System.out.print(
        """
            {
                "operacao": "listarCategorias",
                "token": "9999999"
            }
            """
    );

    tabelaCategorias.setItems(FXCollections.observableArrayList(categoriaRepository.buscarTodos()));

    System.out.printf(
        """
            {
                "operacao": "listarCategorias",
                "token": "9999999",
                categorias: %s
            }
            """, tabelaCategorias.getItems()
    );
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
        {
            "status": %d,
            "operacao": "%s",
            "mensagem": "%s"
        }
        """, status, operacao, mensagem);
  }
}