package br.com.eduardo.sistemadistribuido.servidor.handler;

import br.com.eduardo.sistemadistribuido.entity.Usuario;
import br.com.eduardo.sistemadistribuido.model.dto.UsuarioDTO;
import br.com.eduardo.sistemadistribuido.model.dto.UsuarioSemAdminDTO;
import br.com.eduardo.sistemadistribuido.model.request.EditarUsuarioRequest;
import br.com.eduardo.sistemadistribuido.model.request.ExcluirUsuarioRequest;
import br.com.eduardo.sistemadistribuido.model.request.LocalizarUsuarioRequest;
import br.com.eduardo.sistemadistribuido.model.response.LocalizarUsuarioResponse;
import br.com.eduardo.sistemadistribuido.model.response.MensagemOperacaoResponse;
import br.com.eduardo.sistemadistribuido.repository.UsuarioRepository;
import br.com.eduardo.sistemadistribuido.servidor.service.ValidacaoService;
import br.com.eduardo.sistemadistribuido.util.JPAUtil;
import br.com.eduardo.sistemadistribuido.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioHandler {
  public static String handleEditarUsuario(JsonNode jsonNode) throws JsonProcessingException {
    EntityManager entityManager = JPAUtil.getEntityManagerFactory();
    UsuarioRepository usuarioRepository = new UsuarioRepository(entityManager);

    try {
      EditarUsuarioRequest editarUsuarioRequest = JsonUtil.treeToValue(jsonNode, EditarUsuarioRequest.class);
      Usuario usuario = usuarioRepository.buscarPorRa(editarUsuarioRequest.getUsuario().getRa());

      if (usuario != null) {
        usuario.setNome(editarUsuarioRequest.getUsuario().getNome());
        usuario.setSenha(editarUsuarioRequest.getUsuario().getSenha());

        usuarioRepository.atualizar(usuario);

        MensagemOperacaoResponse response = new MensagemOperacaoResponse();

        response.setOperacao("editarUsuario");
        response.setStatus(201);
        response.setMensagem("Usuario editado com sucesso!");

        return JsonUtil.serialize(response);
      } else {
        throw new NoResultException();
      }
    } catch (NoResultException e) {
      return createErrorResponse("Ra nao encontrado!", 401, "editarUsuario");
    } catch (JsonProcessingException e) {
      return createErrorResponse("Erro ao processar json", 401, "editarUsuario");
    }
  }

  public static String handleExcluirUsuario(JsonNode jsonNode) throws JsonProcessingException {
    EntityManager entityManager = JPAUtil.getEntityManagerFactory();
    UsuarioRepository usuarioRepository = new UsuarioRepository(entityManager);

    try {
      ExcluirUsuarioRequest editarUsuarioRequest = JsonUtil.treeToValue(jsonNode, ExcluirUsuarioRequest.class);
      Usuario usuario = usuarioRepository.buscarPorRa(editarUsuarioRequest.getRa());

      if (usuario != null) {

        usuarioRepository.deletar(usuario);

        MensagemOperacaoResponse response = new MensagemOperacaoResponse();

        response.setOperacao("excluirUsuario");
        response.setStatus(201);
        response.setMensagem("Usuário excluido com sucesso!");

        return JsonUtil.serialize(response);
      } else {
        throw new NoResultException();
      }
    } catch (NoResultException e) {
      return createErrorResponse("Ra nao encontrado!", 401, "excluirUsuario");
    } catch (JsonProcessingException e) {
      return createErrorResponse("Erro ao processar json", 401, "excluirUsuario");
    }
  }

  public static String handlelocalizarUsuario(JsonNode jsonNode) throws JsonProcessingException {
    EntityManager entityManager = JPAUtil.getEntityManagerFactory();
    UsuarioRepository usuarioRepository = new UsuarioRepository(entityManager);

    try {
      LocalizarUsuarioRequest loginRequest = JsonUtil.treeToValue(jsonNode, LocalizarUsuarioRequest.class);
      Usuario usuario = usuarioRepository.buscarPorRa(loginRequest.getRa());

      if (usuario != null) {
        LocalizarUsuarioResponse response = new LocalizarUsuarioResponse();

        UsuarioSemAdminDTO usuarioSemAdminDTO = new UsuarioSemAdminDTO();
        usuarioSemAdminDTO.setNome(usuario.getNome());
        usuarioSemAdminDTO.setRa(usuario.getRa());
        usuarioSemAdminDTO.setSenha(usuario.getSenha());

        response.setStatus(201);
        response.setUsuario(usuarioSemAdminDTO);

        return JsonUtil.serialize(response);
      } else {
        throw new NoResultException();
      }
    } catch (NoResultException e) {
      return createErrorResponse("Ra nao encontrado!", 401, "localizarUsuario");
    } catch (JsonProcessingException e) {
      return createErrorResponse("Erro ao processar json", 401, "localizarUsuario");
    }
  }

  public static String handleRegisterUser(JsonNode jsonNode) throws JsonProcessingException {
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

      response.setMensagem("Usuario cadastrado com sucesso!");
      response.setStatus(201);
    } catch (IllegalArgumentException e) {
      response.setMensagem("Erro ao cadastrar: " + e.getMessage());
      response.setStatus(401);
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      response.setMensagem("Erro ao cadastrar usuario");
      response.setStatus(500);
    }

    return JsonUtil.serialize(response);
  }

  private static void validateUser(UsuarioDTO usuarioDto) {
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


  private static String createErrorResponse(String operacao) throws JsonProcessingException {
    return createErrorResponse("Operacao desconhecida", 400, operacao);
  }

  private static String createErrorResponse(String mensagem, int status, String operacao) throws JsonProcessingException {
    MensagemOperacaoResponse response = new MensagemOperacaoResponse();
    response.setOperacao(operacao);
    response.setMensagem(mensagem);
    response.setStatus(status);
    return JsonUtil.serialize(response);
  }
}
