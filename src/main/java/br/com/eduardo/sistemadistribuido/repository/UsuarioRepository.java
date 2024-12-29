package br.com.eduardo.sistemadistribuido.repository;

import br.com.eduardo.sistemadistribuido.entity.Usuario;
import lombok.*;

import javax.persistence.EntityManager;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UsuarioRepository {
  private EntityManager entityManager;

  public void cadastrar(Usuario usuario) {
    try {
      System.out.println("Usuario recebido para cadastrar: " + usuario);

      entityManager.merge(usuario);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Usuario buscarPorRa(String ra) {
    return entityManager.createQuery("SELECT a FROM Usuario a WHERE a.ra = :ra", Usuario.class)
        .setParameter("ra", ra)
        .getSingleResult();
  }

  public Usuario buscarPorRaESenha(String ra, String senha) {
    return entityManager.createQuery("SELECT a FROM Usuario a WHERE a.ra = :ra AND a.senha = :senha", Usuario.class)
        .setParameter("ra", ra)
        .setParameter("senha", senha)
        .getSingleResult();
  }
}
