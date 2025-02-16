package br.com.eduardo.sistemadistribuido.servidor.handler;

import br.com.eduardo.sistemadistribuido.entity.AvisoCategoriaUsuario;
import br.com.eduardo.sistemadistribuido.entity.Categoria;
import br.com.eduardo.sistemadistribuido.model.request.CadastrarUsuarioCategoriaRequest;
import br.com.eduardo.sistemadistribuido.model.request.DescadastrarUsuarioCategoriaRequest;
import br.com.eduardo.sistemadistribuido.model.request.ListarUsuarioCategoriaRequest;
import br.com.eduardo.sistemadistribuido.model.request.LocalizarCategoriaRequest;
import br.com.eduardo.sistemadistribuido.model.response.*;
import br.com.eduardo.sistemadistribuido.repository.AvisoCategoriaRepository;
import br.com.eduardo.sistemadistribuido.repository.CategoriaRepository;
import br.com.eduardo.sistemadistribuido.util.JPAUtil;
import br.com.eduardo.sistemadistribuido.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolationException;
import java.util.List;

public class CategoriaHandler {
  private static final EntityManager entityManager = JPAUtil.getEntityManagerFactory();
  private static final CategoriaRepository categoriaRepository = new CategoriaRepository(entityManager);
  private static final AvisoCategoriaRepository avisoCategoriaRepository = new AvisoCategoriaRepository(entityManager);

  public static String handleListarUsuarioCategorias(JsonNode jsonNode) throws JsonProcessingException {
    try {
      ListarUsuarioCategoriaRequest listarRequest = JsonUtil.treeToValue(jsonNode, ListarUsuarioCategoriaRequest.class);

      String ra = listarRequest.getRa();

      AvisoCategoriaUsuario avisoCategoriaUsuario = avisoCategoriaRepository.buscarAvisoCategoriasPorRa(ra);

      if (avisoCategoriaUsuario != null) {
        List<Categoria> categorias = avisoCategoriaUsuario.getCategorias();

        ListarUsuarioCategoriaResponse response = new ListarUsuarioCategoriaResponse();
        response.setOperacao("listarUsuarioCategorias");
        response.setStatus(201);
        response.setCategorias(categorias);

        return JsonUtil.serialize(response);
      } else {
        return createErrorResponse("Usuário não encontrado ou sem categorias cadastradas!", "listarUsuarioCategorias");
      }
    } catch (NoResultException e) {
      return createErrorResponse("Usuário não encontrado!", "listarUsuarioCategorias");
    } catch (JsonProcessingException e) {
      return createErrorResponse("Erro ao processar JSON", "listarUsuarioCategorias");
    } catch (Exception e) {
      return createErrorResponse("Erro inesperado: " + e.getMessage(), "listarUsuarioCategorias");
    }
  }

  public static String handleDescadastrarUsuarioCategoria(JsonNode jsonNode) throws JsonProcessingException {
    try {
      DescadastrarUsuarioCategoriaRequest descadastrarRequest = JsonUtil.treeToValue(jsonNode, DescadastrarUsuarioCategoriaRequest.class);

      String ra = descadastrarRequest.getRa();
      Integer idCategoria = descadastrarRequest.getCategoria();

      AvisoCategoriaUsuario avisoCategoriaUsuario = avisoCategoriaRepository.buscarAvisoCategoriasPorRa(ra);

      if (avisoCategoriaUsuario != null) {
        List<Categoria> categorias = avisoCategoriaUsuario.getCategorias();

        Categoria categoriaParaRemover = categoriaRepository.buscarPorId(idCategoria);

        if (categorias.contains(categoriaParaRemover)) {
          categorias.remove(categoriaParaRemover);

          avisoCategoriaUsuario.setCategorias(categorias);
          avisoCategoriaRepository.atualizar(avisoCategoriaUsuario);

          DescadastrarUsuarioCategoriaResponse response = new DescadastrarUsuarioCategoriaResponse();
          response.setStatus(201);
          response.setMensagem("Categoria descadastrada com sucesso!");

          return JsonUtil.serialize(response);
        } else {
          return createErrorResponse("Categoria não encontrada na lista do usuário", "descadastrarUsuarioCategoria");
        }
      } else {
        return createErrorResponse("Usuário não encontrado", "descadastrarUsuarioCategoria");
      }
    } catch (NoResultException e) {
      return createErrorResponse("Erro: Usuário ou categoria não encontrados!", "descadastrarUsuarioCategoria");
    } catch (JsonProcessingException e) {
      return createErrorResponse("Erro ao processar JSON", "descadastrarUsuarioCategoria");
    } catch (Exception e) {
      return createErrorResponse("Erro inesperado: " + e.getMessage(), "descadastrarUsuarioCategoria");
    }
  }

  public static String handleCadastrarUsuarioCategoria(JsonNode jsonNode) throws JsonProcessingException {
    try {
      CadastrarUsuarioCategoriaRequest cadastrarUsuarioCategoria = JsonUtil.treeToValue(jsonNode, CadastrarUsuarioCategoriaRequest.class);

      Integer id = cadastrarUsuarioCategoria.getCategoria();

      Categoria categoria = categoriaRepository.buscarPorId(id);

      if (categoria != null) {
        AvisoCategoriaUsuario avisoCategoriaUsuario = new AvisoCategoriaUsuario();

        avisoCategoriaUsuario.setRa(cadastrarUsuarioCategoria.getRa());

        List<Categoria> categoriaList = avisoCategoriaRepository.buscarCategoriasPorRa(cadastrarUsuarioCategoria.getRa());

        Categoria categoriaParaAdicionar = categoriaRepository.buscarPorId(cadastrarUsuarioCategoria.getCategoria());

        if (categoriaList.contains(categoriaParaAdicionar)) {
          throw new ConstraintViolationException("Categoria já se encontra cadastrada", null);
        }


        categoriaList.add(categoriaParaAdicionar);

        avisoCategoriaUsuario.setCategorias(categoriaList);

        avisoCategoriaRepository.cadastrar(avisoCategoriaUsuario);

        CadastrarUsuarioCategoriaResponse response = new CadastrarUsuarioCategoriaResponse();

        response.setOperacao("cadastrarUsuarioCategoria");
        response.setStatus(201);
        response.setMensagem("Cadastro em categoria realizado com sucesso!");

        return JsonUtil.serialize(response);
      } else {
        throw new NoResultException();
      }
    } catch (NoResultException e) {
      return createErrorResponse("Categoria nao encontrada!", "cadastrarUsuarioCategoria");
    } catch (JsonProcessingException e) {
      return createErrorResponse("Erro ao processar json", "cadastrarUsuarioCategoria");
    } catch (ConstraintViolationException e) {
      return createErrorResponse("Categoria já se encontra cadastrada", "cadastrarUsuarioCategoria");
    }
  }

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
      return createErrorResponse("Ra nao encontrado!", "listarCategorias");
    } catch (JsonProcessingException e) {
      return createErrorResponse("Erro ao processar json", "listarCategorias");
    }
  }

  public static String handleLocalizarCategoria(JsonNode jsonNode) throws JsonProcessingException {
    try {
      LocalizarCategoriaRequest localizarCategoriaRequest = JsonUtil.treeToValue(jsonNode, LocalizarCategoriaRequest.class);

      Integer id = localizarCategoriaRequest.getId();

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
      return createErrorResponse("Categoria nao encontrada!", "localizarCategoria");
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
