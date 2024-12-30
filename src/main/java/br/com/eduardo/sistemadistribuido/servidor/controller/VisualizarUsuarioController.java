package br.com.eduardo.sistemadistribuido.servidor.controller;

import br.com.eduardo.sistemadistribuido.entity.Usuario;
import br.com.eduardo.sistemadistribuido.repository.UsuarioRepository;
import br.com.eduardo.sistemadistribuido.util.JPAUtil;
import br.com.eduardo.sistemadistribuido.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class VisualizarUsuarioController {

  @FXML
  private TableView<Usuario> tabelaUsuarios;

  @FXML
  private TableColumn<Usuario, String> colunaRA;

  @FXML
  private TableColumn<Usuario, String> colunaNome;

  @FXML
  private TableColumn<Usuario, String> colunaSenha;

  @FXML
  private TableColumn<Usuario, Void> colunaExcluir;

  private UsuarioRepository usuarioRepository;

  public void initialize() throws JsonProcessingException {
    EntityManager entityManager = JPAUtil.getEntityManagerFactory();
    usuarioRepository = new UsuarioRepository(entityManager);

    colunaRA.setCellValueFactory(new PropertyValueFactory<>("ra"));
    colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
    colunaSenha.setCellValueFactory(new PropertyValueFactory<>("senha"));

    adicionarColunaExcluir();

    carregarDadosTabela();
  }


  private void carregarDadosTabela() throws JsonProcessingException {
    System.out.println("""
        {
          "operacao": "localizarUsuario"
          "token": "123"
        }
        """);

    List<Usuario> usuarios = usuarioRepository.buscarTodos();

    tabelaUsuarios.setItems(FXCollections.observableArrayList(usuarios));


    System.out.printf("""
        {
          "status": 200,
          "operacao": "localizarUsuario",
          "listaUsuarios": %s
        }
        %n""", JsonUtil.serialize(usuarios));;

  }

  private void adicionarColunaExcluir() {
    colunaExcluir.setCellFactory(column -> new TableCell<>() {
      private final Button btnExcluir = new Button("Excluir");

      {
        btnExcluir.setOnAction(event -> {
          Usuario usuario = getTableView().getItems().get(getIndex());
          mostrarConfirmacao(usuario);
        });
      }

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
          setGraphic(null);
        } else {
          setGraphic(btnExcluir);
        }
      }
    });
  }

  private void mostrarConfirmacao(Usuario usuario) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmação de Exclusão");
    alert.setHeaderText("Deseja realmente excluir este usuário?");
    alert.setContentText("Usuário: " + usuario.getNome());

    alert.showAndWait().ifPresent(response -> {
      if (response == ButtonType.OK) {
        excluirUsuario(usuario);
      }
    });
  }

  private void excluirUsuario(Usuario usuario) {
    try {
      usuarioRepository.deletar(usuario);
      tabelaUsuarios.getItems().remove(usuario);

      System.out.printf("""
            {
              "status": 200,
              "operacao": "excluirUsuario",
              "usuario": "%s"
            }
            %n""", JsonUtil.serialize(usuario));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
