package br.com.eduardo.sistemadistribuido.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "usuario")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario {
  @Id
  @Column(name = "ra_usuario", unique = true)
  private String ra;

  @Column(name = "is_admin_usuario")
  private Boolean isAdmin = false;

  @Column(name = "nome_usuario")
  @NotNull(message = "O nome é obrigatório")
  private String nome;

  @Column(name = "senha_usuario")
  @NotNull(message = "A senha é obrigatória")
  private String senha;
}
