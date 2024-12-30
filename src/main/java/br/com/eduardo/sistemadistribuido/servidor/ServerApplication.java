package br.com.eduardo.sistemadistribuido.servidor;

import br.com.eduardo.sistemadistribuido.servidor.handler.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerApplication extends Thread {
  protected Socket clientSocket;

  public ServerApplication() {
  }

  private ServerApplication(Socket clientSoc) {
    this.clientSocket = clientSoc;
    start();
  }

  public static void main(String[] args) {
    try {
      new ServerApplication().executarServidor();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void executarServidor() throws IOException {
    ServerSocket serverSocket = null;

    try {
      Scanner scanner = new Scanner(System.in);

      System.out.println("Digite a porta para o servidor: ");
      int porta = scanner.nextInt();

      scanner.close();

      serverSocket = new ServerSocket(porta);
      System.out.println("Conexão de Socket criada");
      try {
        while (true) {
          System.out.println("Aguardando conexão");
          new ServerApplication(serverSocket.accept());
        }
      } catch (IOException e) {
        System.err.println("Falha ao aceitar conexão. Erro: " + e.getMessage());
      }
    } catch (IOException e) {
      System.err.println("Não foi possível ouvir a porta\nErro: " + e.getMessage());
    } finally {
      try {
        if (serverSocket != null) {
          serverSocket.close();
        }
      } catch (IOException e) {
        System.err.println("Não foi possível fechar a porta: 10008. Erro: " + e.getMessage());
      }
    }
  }

  public void run() {
    System.out.println("Novo Cliente Conectado");

    try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

      String inputLine;

      while ((inputLine = in.readLine()) != null) {
        System.out.println("Json recebido: " + inputLine);

        RequestHandler requestHandler = new RequestHandler();

        String mensagemJson = requestHandler.processRequest(inputLine);

        out.println(mensagemJson);
      }
    } catch (IOException e) {
      System.err.println("Problema com o servidor de comunicação. Erro: " + e.getMessage());
    } finally {
      try {
        if (clientSocket != null) {
          clientSocket.close();
        }
      } catch (IOException e) {
        System.err.println("Erro ao fechar socket do cliente. Erro: " + e.getMessage());
      }
    }
  }
}