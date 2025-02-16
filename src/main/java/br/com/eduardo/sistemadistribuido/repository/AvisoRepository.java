package br.com.eduardo.sistemadistribuido.repository;

import br.com.eduardo.sistemadistribuido.entity.Aviso;
import br.com.eduardo.sistemadistribuido.util.AlertUtil;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EntityManager;
import java.util.List;

@NoArgsConstructor
@ToString
public class AvisoRepository {
  private EntityManager entityManager;

  public AvisoRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public void cadastrar(Aviso aviso) {
    try {
      entityManager.getTransaction().begin();
      System.out.println("Aviso recebida para cadastrar: " + aviso);
      entityManager.merge(aviso);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      AlertUtil.alert("Erro", "Erro ao cadastrar aviso", e.getMessage());
      System.out.println(e.getMessage());
    }
  }

  public Aviso buscarPorId(long id) {
    return entityManager.find(Aviso.class, id);
  }

  public List<Aviso> buscarPorCategoriaId(int categoriaId) {
    return entityManager.createQuery(
            "SELECT a FROM Aviso a WHERE a.categoria.id = :categoriaId", Aviso.class)
        .setParameter("categoriaId", categoriaId)
        .getResultList();
  }

  public Aviso buscarPorTitulo(String titulo) {
    return entityManager.createQuery("SELECT a FROM Aviso a WHERE a.titulo = :titulo", Aviso.class)
        .setParameter("titulo", titulo)
        .getSingleResult();
  }

  public void deletar(Aviso aviso) {
    entityManager.getTransaction().begin();

    if (!entityManager.contains(aviso)) {
      aviso = entityManager.merge(aviso);
    }
    entityManager.remove(aviso);

    entityManager.getTransaction().commit();
  }

  public void atualizar(Aviso aviso) {
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

  public List<Aviso> buscarTodos() {
    return entityManager.createQuery("SELECT a FROM Aviso a", Aviso.class).getResultList();
  }

  public List<Aviso> buscarPorCategoriaIds(List<Integer> categoriaIds) {
    return entityManager.createQuery(
            "SELECT a FROM Aviso a WHERE a.categoria.id IN :categoriaIds", Aviso.class)
        .setParameter("categoriaIds", categoriaIds)
        .getResultList();
  }
}
