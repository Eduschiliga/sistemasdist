package br.com.eduardo.sistemadistribuido.cliente.service;

import br.com.eduardo.sistemadistribuido.cliente.ClienteApplication;
import br.com.eduardo.sistemadistribuido.model.request.LogoutRequest;
import br.com.eduardo.sistemadistribuido.model.response.ErroResponse;
import br.com.eduardo.sistemadistribuido.util.AlertUtil;
import br.com.eduardo.sistemadistribuido.util.JsonUtil;
import br.com.eduardo.sistemadistribuido.util.SocketManager;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class LogoutService {
  public static void logout() {
    try {
      LogoutRequest logout = new LogoutRequest();

      logout.setToken(ClienteApplication.token);

      String resposta = SocketManager.enviarSocket(JsonUtil.serialize(logout));

      JsonNode jsonNode = JsonUtil.readTree(resposta);

      switch (jsonNode.get("status").intValue()) {
        case 200:
          AlertUtil.alert("Sucesso!", "Logout realizado com sucesso!", "Logout realizado com sucesso!");

          ClienteApplication.token = "";
          ClienteApplication.trocarTela("Login");
          break;

        case 401:
          ErroResponse erro = JsonUtil.treeToValue(jsonNode, ErroResponse.class);

          AlertUtil.alert("Erro!", "Erro ao realizar o logout!", erro.getMensagem());

          break;

        default:
          AlertUtil.alert("Erro!", "", "Erro desconhecido.");
          break;
      }
    } catch (IOException e) {
      AlertUtil.alert("Erro!", "Erro ao realizar o logout!", "Erro ao realizar o logout: " + e.getMessage());
    }
  }
}
