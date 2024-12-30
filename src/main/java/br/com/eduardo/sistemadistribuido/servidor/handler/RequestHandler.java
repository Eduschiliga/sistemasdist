package br.com.eduardo.sistemadistribuido.servidor.handler;

import br.com.eduardo.sistemadistribuido.entity.Usuario;
import br.com.eduardo.sistemadistribuido.model.request.LoginRequest;
import br.com.eduardo.sistemadistribuido.model.response.LoginSucessoResponse;
import br.com.eduardo.sistemadistribuido.model.response.MensagemOperacaoResponse;
import br.com.eduardo.sistemadistribuido.model.response.MensagemStatusResponse;
import br.com.eduardo.sistemadistribuido.repository.UsuarioRepository;
import br.com.eduardo.sistemadistribuido.util.JPAUtil;
import br.com.eduardo.sistemadistribuido.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.IOException;

import static br.com.eduardo.sistemadistribuido.servidor.handler.UsuarioHandler.handleRegisterUser;
import static br.com.eduardo.sistemadistribuido.servidor.handler.UsuarioHandler.handlelocalizarUsuario;

public class RequestHandler {
  public String processRequest(String inputLine) throws IOException {
    JsonNode jsonNode = JsonUtil.readTree(inputLine);
    String operacao = jsonNode.get("operacao").asText();

    return switch (operacao) {
      case "cadastrarUsuario" -> handleRegisterUser(jsonNode);
      case "login" -> handleLogin(jsonNode);
      case "localizarUsuario" -> handlelocalizarUsuario(jsonNode);
      case "logout" -> handleLogout();
      default -> createErrorResponse(operacao);
    };
  }

  private String handleLogin(JsonNode jsonNode) throws JsonProcessingException {
    EntityManager entityManager = JPAUtil.getEntityManagerFactory();
    UsuarioRepository usuarioRepository = new UsuarioRepository(entityManager);

    try {
      LoginRequest loginRequest = JsonUtil.treeToValue(jsonNode, LoginRequest.class);
      Usuario usuario = usuarioRepository.buscarPorRaESenha(loginRequest.getRa(), loginRequest.getSenha());

      if (usuario != null) {
        LoginSucessoResponse response = new LoginSucessoResponse();
        response.setStatus(200);
        response.setToken(usuario.getRa());
        return JsonUtil.serialize(response);
      } else {
        return createErrorResponse("Usuário ou senha inválidos", 401, "login");
      }
    } catch (NoResultException e) {
      return createErrorResponse("Usuário ou senha inválidos", 401, "login");
    } catch (JsonProcessingException e) {
      return createErrorResponse("Erro ao processar json", 401, "login");
    }
  }

  private String handleLogout() throws JsonProcessingException {
    MensagemStatusResponse response = new MensagemStatusResponse();
    response.setStatus(200);
    return JsonUtil.serialize(response);
  }

  private String createErrorResponse(String operacao) throws JsonProcessingException {
    return createErrorResponse("Operação desconhecida", 400, operacao);
  }

  private String createErrorResponse(String mensagem, int status, String operacao) throws JsonProcessingException {
    MensagemOperacaoResponse response = new MensagemOperacaoResponse();
    response.setOperacao(operacao);
    response.setMensagem(mensagem);
    response.setStatus(status);
    return JsonUtil.serialize(response);
  }
}