package br.com.eduardo.sistemadistribuido.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "aviso_categoria_usuario")
@Getter
@Setter
public class AvisoCategoriaUsuario {
  @Id
  @Column(name = "ra_usuario")
  private String ra;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
  private List<Categoria> categorias = new ArrayList<>();
}
