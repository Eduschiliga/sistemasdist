package br.com.eduardo.sistemadistribuido.servidor.controller;

import br.com.eduardo.sistemadistribuido.cliente.ClienteApplication;
import br.com.eduardo.sistemadistribuido.entity.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UsuarioAdminController {

  private final Usuario usuario = new Usuario();

  @FXML
  private Label mensagenErro;

  @FXML
  private TextField inputRa;

  @FXML
  private TextField inputNome;

  @FXML
  private PasswordField inputSenha;

  public void initialize() {
    printarRequisicao();
    processarUsuario();
  }

  private void printarRequisicao() {
    String json = """
      {
          "operacao": "localizarUsuario",
          "token":  "9999999",
          "ra": "9999999"
      }
   """;
    System.out.println(json);
  }

  private void printarUsuario() {
    String json = """
      {
        "status": 201,
        "operacao": "localizarUsuario",
        "usuario": {
           "ra": "9999999",
           "senha": "USERADMIN123",
           "nome": "USER ADMIN"
        }
      }
    """;
    System.out.println(json);
  }

  private void processarUsuario() {
    mensagenErro.setVisible(false);

    usuario.setNome("USER ADMIN");
    usuario.setRa("9999999");
    usuario.setSenha("USERADMIN123");

    carregarDadosUsuario();
    printarUsuario();
  }

  private void carregarDadosUsuario() {
    inputRa.setText(usuario.getRa());
    inputNome.setText(usuario.getNome());
    inputSenha.setText(usuario.getSenha());
  }

  private void exibirErro(String mensagem) {
    mensagenErro.setVisible(true);
    mensagenErro.setText(mensagem);
  }
}