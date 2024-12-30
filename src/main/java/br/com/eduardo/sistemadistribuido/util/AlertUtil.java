package br.com.eduardo.sistemadistribuido.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Optional;

import static br.com.eduardo.sistemadistribuido.cliente.ClienteApplication.socket;
import static java.lang.System.exit;

public class AlertUtil {

  public static void alert(String title, String header, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);

    alert.showAndWait();
  }

  public static Optional<String[]> mostrarDialogoConexao() {
    Dialog<String[]> dialog = new Dialog<>();
    dialog.setTitle("Configuração de Conexão");
    dialog.setHeaderText("Informe o IP e a Porta:");

    TextField ipField = new TextField();
    ipField.setPromptText("Endereço IP");
    TextField portaField = new TextField();
    portaField.setPromptText("Porta");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.add(ipField, 0, 0);
    grid.add(portaField, 0, 1);

    dialog.getDialogPane().setContent(grid);

    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    dialog.setResultConverter(button -> {
      if (button == ButtonType.OK) {
        return new String[]{ipField.getText(), portaField.getText()};
      }
      return null;
    });

    return dialog.showAndWait();
  }

  public static void exibirConfirmacao(String ip, int porta) {
    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
    alerta.setTitle("Confirmação");
    alerta.setHeaderText("Conexão Configurada com Sucesso!");
    alerta.setContentText("IP: " + ip + "\nPorta: " + porta);
    alerta.showAndWait();
  }

  public static void exibirDialogoConexao() {
    boolean conectado = false;
    while (!conectado) {
      Optional<String[]> resultado = mostrarDialogoConexao();

      if (resultado.isEmpty()) {
        exit(0);
      }

      String[] dados = resultado.get();
      String ip = dados[0];
      int porta;

      try {
        porta = Integer.parseInt(dados[1]);

        socket.setIp(ip);
        socket.setPorta(porta);

        conectado = socket.conectarSocket();
        if (conectado) {
          exibirConfirmacao(ip, porta);
        } else {
          alert("", "Erro!", "Falha ao conectar-se ao servidor.");
        }
      } catch (NumberFormatException e) {
        alert("", "Erro!", "Porta inválida inserida: " + dados[1]);
      } catch (Exception e) {
        alert("", "Erro inesperado", e.getMessage());
      }
    }
  }
}
