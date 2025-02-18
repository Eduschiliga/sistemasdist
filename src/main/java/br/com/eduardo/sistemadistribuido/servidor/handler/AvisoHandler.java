package br.com.eduardo.sistemadistribuido.servidor.handler;

import br.com.eduardo.sistemadistribuido.entity.Aviso;
import br.com.eduardo.sistemadistribuido.entity.Categoria;
import br.com.eduardo.sistemadistribuido.model.request.ListarAvisosRequest;
import br.com.eduardo.sistemadistribuido.model.request.ListarUsuarioAvisosRequest;
import br.com.eduardo.sistemadistribuido.model.request.LocalizarAvisoRequest;
import br.com.eduardo.sistemadistribuido.model.response.ListarAvisosResponse;
import br.com.eduardo.sistemadistribuido.model.response.LocalizarAvisoResponse;
import br.com.eduardo.sistemadistribuido.repository.AvisoCategoriaRepository;
import br.com.eduardo.sistemadistribuido.repository.AvisoRepository;
import br.com.eduardo.sistemadistribuido.util.JPAUtil;
import br.com.eduardo.sistemadistribuido.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

import static br.com.eduardo.sistemadistribuido.servidor.handler.CategoriaHandler.createErrorResponse;

public class AvisoHandler {
  private static final EntityManager entityManager = JPAUtil.getEntityManagerFactory();
  private static final AvisoRepository avisoRepository = new AvisoRepository(entityManager);
  private static final AvisoCategoriaRepository avisoCategoriaRepository = new AvisoCategoriaRepository(entityManager);

  public static String handleListarAvisos(JsonNode jsonNode) throws JsonProcessingException {
    try {
      ListarAvisosRequest listarAvisosRequest = JsonUtil.treeToValue(jsonNode, ListarAvisosRequest.class);

      Integer categoriaId = listarAvisosRequest.getCategoria();
      List<Aviso> avisos;

      if (categoriaId != null && categoriaId > 0) {
        avisos = avisoRepository.buscarPorCategoriaId(categoriaId);
      } else {
        avisos = avisoRepository.buscarTodos();
      }

      ListarAvisosResponse response = new ListarAvisosResponse();
      response.setOperacao("listarAvisos");
      response.setStatus(201);
      response.setAvisos(avisos);

      return JsonUtil.serialize(response);
    } catch (NoResultException e) {
      return createErrorResponse("Categoria não encontrada!", "listarAvisos");
    } catch (JsonProcessingException e) {
      return createErrorResponse("Erro ao processar JSON", "listarAvisos");
    } catch (Exception e) {
      return createErrorResponse("Erro: " + e.getMessage(), "listarAvisos");
    }
  }

  public static String handleLocalizarAviso(JsonNode jsonNode) throws JsonProcessingException {
    try {
      LocalizarAvisoRequest localizarAvisoRequest = JsonUtil.treeToValue(jsonNode, LocalizarAvisoRequest.class);

      Integer avisoId = localizarAvisoRequest.getId();

      if (avisoId == null || avisoId <= 0) {
        return createErrorResponse("ID inválido para localizar aviso", "localizarAviso");
      }

      Aviso aviso = avisoRepository.buscarPorId(avisoId);

      if (aviso == null) {
        return createErrorResponse("Aviso não encontrado com o ID fornecido", "localizarAviso");
      }

      LocalizarAvisoResponse response = new LocalizarAvisoResponse();
      response.setStatus(201);
      response.setAviso(aviso);

      return JsonUtil.serialize(response);
    } catch (NoResultException e) {
      return createErrorResponse("Aviso não encontrado!", "localizarAviso");
    } catch (JsonProcessingException e) {
      return createErrorResponse("Erro ao processar JSON", "localizarAviso");
    } catch (Exception e) {
      return createErrorResponse("Erro inesperado: " + e.getMessage(), "localizarAviso");
    }
  }

  public static String handleListarUsuarioAvisos(JsonNode jsonNode) throws JsonProcessingException {
    try {
      ListarUsuarioAvisosRequest listarRequest = JsonUtil.treeToValue(jsonNode, ListarUsuarioAvisosRequest.class);

      String ra = listarRequest.getRa();

      List<Categoria> categoriaList = avisoCategoriaRepository.buscarCategoriasPorRa(ra);

      List<Aviso> avisos = new ArrayList<>();

      if (categoriaList != null && !categoriaList.isEmpty()) {
        List<Integer> categoriaIds = categoriaList.stream()
            .map(Categoria::getId)
            .toList();

        avisos = avisoRepository.buscarPorCategoriaIds(categoriaIds);
      }

      ListarAvisosResponse response = new ListarAvisosResponse();
      response.setStatus(201);
      response.setAvisos(avisos);

      return JsonUtil.serialize(response);

    } catch (NoResultException e) {
      ListarAvisosResponse response = new ListarAvisosResponse();
      response.setStatus(201);
      response.setAvisos(new ArrayList<>());

      return JsonUtil.serialize(response);
    } catch (JsonProcessingException e) {
      return createErrorResponse("Erro ao processar JSON", "listarAvisosPorCategoriasUsuario");
    } catch (Exception e) {
      return createErrorResponse("Erro inesperado: " + e.getMessage(), "listarAvisosPorCategoriasUsuario");
    }
  }
}
