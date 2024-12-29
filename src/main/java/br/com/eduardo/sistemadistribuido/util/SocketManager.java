package br.com.eduardo.sistemadistribuido.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@AllArgsConstructor
@NoArgsConstructor
public class SocketManager {
  private String ip;
  private int porta;
  private Socket socket;
  private static PrintWriter out;
  private static BufferedReader in;

  public boolean conectarSocket() {
    try {
      socket = new Socket(this.ip, this.porta);

      out = new PrintWriter(socket.getOutputStream(), true);

      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      return true;
    } catch (Exception e) {
      System.err.println(e)
      ;
      return false;
    }
  }

  public static String enviarSocket(String json) {
    String retorno = "";
    try {
      System.out.println("Enviado: " + json);
      retorno = enviarJson(json);
      System.out.println("Recebido: " + retorno);
    } catch (IOException exception) {
      System.err.println(exception);
      System.exit(1);
    }
    return retorno;
  }

  public static String enviarJson(String json) throws IOException {
    out.println(json);
    return in.readLine();
  }

  public void fecharSocket() {
    try {
      in.close();
      out.close();
      socket.close();

      System.out.println("Conexão com " + this.ip + ":" + this.porta + " fechada com sucesso!");
    } catch (IOException IOE) {
      System.err.println(IOE);
    }
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public void setPorta(int porta) {
    this.porta = porta;
  }
}