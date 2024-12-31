package br.com.eduardo.sistemadistribuido.repository;

import br.com.eduardo.sistemadistribuido.entity.Categoria;
import br.com.eduardo.sistemadistribuido.util.AlertUtil;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.EntityManager;
import java.util.List;

@NoArgsConstructor
@ToString
public class CategoriaRepository {
  private EntityManager entityManager;

  public CategoriaRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public void cadastrar(Categoria categoria) {
    try {
      entityManager.getTransaction().begin();
      System.out.println("Categoria recebida para cadastrar: " + categoria);
      entityManager.persist(categoria);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      AlertUtil.alert("Erro", "Erro ao cadastrar categoria", e.getMessage());
    }
  }

  public Categoria buscarPorId(long id) {
    return entityManager.find(Categoria.class, id);
  }

  public Categoria buscarPorNome(String nome) {
    return entityManager.createQuery("SELECT a FROM Categoria a WHERE a.nome = :nome", Categoria.class)
        .setParameter("nome", nome)
        .getSingleResult();
  }

  public void deletar(Categoria categoria) {
    entityManager.getTransaction().begin();

    if (!entityManager.contains(categoria)) {
      categoria = entityManager.merge(categoria);
    }
    entityManager.remove(categoria);

    entityManager.getTransaction().commit();
  }

  public void atualizar(Categoria categoria) {
    try {
      entityManager.getTransaction().begin();
      entityManager.merge(categoria);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      if (entityManager.getTransaction().isActive()) {
        entityManager.getTransaction().rollback();
      }
      AlertUtil.alert("Erro", "Erro ao atualizar categoria", e.getMessage());
    }
  }

  public List<Categoria> buscarTodos() {
    return entityManager.createQuery("SELECT a FROM Categoria a", Categoria.class)
        .getResultList();
  }
}
