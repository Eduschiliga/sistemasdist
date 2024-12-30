package br.com.eduardo.sistemadistribuido.servidor.handler;

import br.com.eduardo.sistemadistribuido.entity.Usuario;
import br.com.eduardo.sistemadistribuido.model.dto.UsuarioDTO;
import br.com.eduardo.sistemadistribuido.model.request.LoginRequest;
import br.com.eduardo.sistemadistribuido.model.response.LoginSucessoResponse;
import br.com.eduardo.sistemadistribuido.model.response.MensagemOperacaoResponse;
import br.com.eduardo.sistemadistribuido.model.response.MensagemStatusResponse;
import br.com.eduardo.sistemadistribuido.repository.UsuarioRepository;
import br.com.eduardo.sistemadistribuido.service.ValidacaoService;
import br.com.eduardo.sistemadistribuido.util.JPAUtil;
import br.com.eduardo.sistemadistribuido.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestHandler {
  public String processRequest(String inputLine) throws IOException {
    JsonNode jsonNode = JsonUtil.readTree(inputLine);
    String operacao = jsonNode.get("operacao").asText();

    return switch (operacao) {
      case "cadastrarUsuario" -> handleRegisterUser(jsonNode);
      case "login" -> handleLogin(jsonNode);
      case "logout" -> handleLogout();
      default -> createErrorResponse(operacao);
    };
  }

  private String handleRegisterUser(JsonNode jsonNode) throws JsonProcessingException {
    EntityManager entityManager = JPAUtil.getEntityManagerFactory();
    UsuarioRepository usuarioRepository = new UsuarioRepository(entityManager);

    MensagemOperacaoResponse response = new MensagemOperacaoResponse();
    response.setOperacao("cadastrarUsuario");

    try {
      UsuarioDTO usuarioDto = JsonUtil.treeToValue(jsonNode, UsuarioDTO.class);
      validateUser(usuarioDto);

      entityManager.getTransaction().begin();
      Usuario usuarioEntity = new Usuario();
      usuarioEntity.setNome(usuarioDto.getNome());
      usuarioEntity.setRa(usuarioDto.getRa());
      usuarioEntity.setSenha(usuarioDto.getSenha());

      usuarioRepository.cadastrar(usuarioEntity);
      entityManager.getTransaction().commit();

      response.setMensagem("Usuário cadastrado com sucesso!");
      response.setStatus(201);
    } catch (IllegalArgumentException e) {
      response.setMensagem("Erro ao cadastrar: " + e.getMessage());
      response.setStatus(401);
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      response.setMensagem("Erro ao cadastrar usuário");
      response.setStatus(500);
    }

    return JsonUtil.serialize(response);
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

  private void validateUser(UsuarioDTO usuarioDto) {
    List<String> invalidFields = new ArrayList<>();

    if (!ValidacaoService.isValidRa(usuarioDto.getRa())) {
      invalidFields.add("RA");
    }
    if (!ValidacaoService.isValidSenha(usuarioDto.getSenha())) {
      invalidFields.add("Senha");
    }
    if (!ValidacaoService.isValidNome(usuarioDto.getNome())) {
      invalidFields.add("Nome");
    }

    if (!invalidFields.isEmpty()) {
      throw new IllegalArgumentException(String.join(", ", invalidFields));
    }
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