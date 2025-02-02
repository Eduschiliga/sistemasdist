package br.com.eduardo.sistemadistribuido.servidor.handler;

import br.com.eduardo.sistemadistribuido.entity.Categoria;
import br.com.eduardo.sistemadistribuido.model.request.LocalizarCategoriaRequest;
import br.com.eduardo.sistemadistribuido.model.response.BuscarCategoriasResponse;
import br.com.eduardo.sistemadistribuido.model.response.LocalizarCategoriaResponse;
import br.com.eduardo.sistemadistribuido.model.response.MensagemOperacaoResponse;
import br.com.eduardo.sistemadistribuido.repository.CategoriaRepository;
import br.com.eduardo.sistemadistribuido.util.JPAUtil;
import br.com.eduardo.sistemadistribuido.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class CategoriaHandler {
  private static final EntityManager entityManager = JPAUtil.getEntityManagerFactory();
  private static final CategoriaRepository categoriaRepository = new CategoriaRepository(entityManager);

  public static String handlePesquisarCategorias(JsonNode jsonNode) throws JsonProcessingException {

    try {
      List<Categoria> categoriaList = categoriaRepository.buscarTodos();

      if (categoriaList != null) {
        BuscarCategoriasResponse response = new BuscarCategoriasResponse();

        response.setOperacao("listarCategorias");
        response.setStatus(201);
        response.setCategorias(categoriaList);

        return JsonUtil.serialize(response);
      } else {
        throw new NoResultException();
      }
    } catch (NoResultException e) {
      return createErrorResponse("Ra não encontrado!", "listarCategorias");
    } catch (JsonProcessingException e) {
      return createErrorResponse("Erro ao processar json", "listarCategorias");
    }
  }

  public static String handleLocalizarCategoria(JsonNode jsonNode) throws JsonProcessingException {
    try {
      LocalizarCategoriaRequest localizarCategoriaRequest = JsonUtil.treeToValue(jsonNode, LocalizarCategoriaRequest.class);

      long id = localizarCategoriaRequest.getId();

      Categoria categoria = categoriaRepository.buscarPorId(id);

      if (categoria != null) {
        LocalizarCategoriaResponse response = new LocalizarCategoriaResponse();

        response.setOperacao("localizarCategoria");
        response.setStatus(201);
        response.setCategoria(categoria);

        return JsonUtil.serialize(response);
      } else {
        throw new NoResultException();
      }
    } catch (NoResultException e) {
      return createErrorResponse("Categoria não encontrada!", "localizarCategoria");
    } catch (JsonProcessingException e) {
      return createErrorResponse("Erro ao processar json", "localizarCategoria");
    }
  }

  private static String createErrorResponse(String mensagem, String operacao) throws JsonProcessingException {
    MensagemOperacaoResponse response = new MensagemOperacaoResponse();
    response.setOperacao(operacao);
    response.setMensagem(mensagem);
    response.setStatus(401);
    return JsonUtil.serialize(response);
  }
}
