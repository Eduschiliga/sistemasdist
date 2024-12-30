package br.com.eduardo.sistemadistribuido.model.dto;

public class UsuarioSemAdminDTO {
  private String nome;
  private String senha;
  private String ra;

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public String getRa() {
    return ra;
  }

  public void setRa(String ra) {
    this.ra = ra;
  }
}

