package br.com.eduardo.sistemadistribuido.servidor.service;

import java.util.regex.Pattern;

public class ValidacaoService {
  public static boolean isValidSenha(String senha) {
    return senha != null && senha.length() >= 8 && senha.length() <= 20 && Pattern.matches("^[a-zA-Z]+$", senha);
  }

  public static boolean isValidRa(String ra) {
    return ra != null && ra.length() == 7 && Pattern.matches("^[0-9]+$", ra);
  }

  public static boolean isValidNome(String nome) {
    return nome != null && nome.length() <= 50 && Pattern.matches("^[A-Z]+$", nome);
  }
}
