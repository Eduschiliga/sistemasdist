package br.com.eduardo.sistemadistribuido.repository;

import br.com.eduardo.sistemadistribuido.entity.AvisoCategoriaUsuario;
import br.com.eduardo.sistemadistribuido.entity.Categoria;
import br.com.eduardo.sistemadistribuido.util.AlertUtil;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EntityManager;
import java.util.List;

@NoArgsConstructor
@ToString
public class AvisoCategoriaRepository {
  private EntityManager entityManager;

  public AvisoCategoriaRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public void cadastrar(AvisoCategoriaUsuario avisoCategoriaUsuario) {
    try {
      entityManager.getTransaction().begin();
      entityManager.merge(avisoCategoriaUsuario);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      AlertUtil.alert("Erro", "Erro ao cadastrar aviso", e.getMessage());
      System.out.println(e.getMessage());
    }
  }

  public AvisoCategoriaUsuario buscarPorId(long id) {
    return entityManager.find(AvisoCategoriaUsuario.class, id);
  }

  public AvisoCategoriaUsuario buscarAvisoCategoriasPorRa(String ra) {
    return entityManager.createQuery("SELECT a FROM AvisoCategoriaUsuario a WHERE a.ra = :ra", AvisoCategoriaUsuario.class)
        .setParameter("ra", ra)
        .getSingleResult();
  }

  public List<Categoria> buscarCategoriasPorRa(String ra) {
    return entityManager.createQuery("SELECT a.categoria FROM AvisoCategoriaUsuario a WHERE a.ra = :ra", Categoria.class)
        .setParameter("ra", ra)
        .getResultList();
  }

  public void deletar(AvisoCategoriaUsuario aviso) {
    entityManager.getTransaction().begin();

    if (!entityManager.contains(aviso)) {
      aviso = entityManager.merge(aviso);
    }
    entityManager.remove(aviso);

    entityManager.getTransaction().commit();
  }

  public void atualizar(AvisoCategoriaUsuario aviso) {
    try {
      entityManager.getTransaction().begin();
      entityManager.merge(aviso);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      AlertUtil.alert("Erro", "Erro ao atualizar aviso", e.getMessage());
    }
  }
}
