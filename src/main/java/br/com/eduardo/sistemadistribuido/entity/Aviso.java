package br.com.eduardo.sistemadistribuido.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "aviso")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Aviso {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "aviso_id")
  private long id;

  @Column(name = "titulo")
  private String titulo;

  @Column(name = "descricao")
  private String descricao;

  @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
  @JoinColumn(name = "categoria_id")
  private Categoria categoria;

  public String getNomeCategoria() {
    return categoria != null ? categoria.getNome() : "Sem Categoria";
  }
}
