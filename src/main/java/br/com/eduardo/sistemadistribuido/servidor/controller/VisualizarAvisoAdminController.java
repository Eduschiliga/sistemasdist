package br.com.eduardo.sistemadistribuido.servidor.controller;

import br.com.eduardo.sistemadistribuido.entity.Aviso;
import br.com.eduardo.sistemadistribuido.repository.AvisoRepository;
import br.com.eduardo.sistemadistribuido.util.JPAUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

public class VisualizarAvisoAdminController {

  @FXML
  private ListView<String> listViewAvisos;

  @FXML
  private Label mensagenErro;

  private static final EntityManager entityManager = JPAUtil.getEntityManagerFactory();
  private static final AvisoRepository avisoRepository = new AvisoRepository(entityManager);

  public void initialize() throws IOException {

    List<Aviso> avisoList = avisoRepository.buscarTodos();
    setAvisos(avisoList);
  }

  public void setAvisos(List<Aviso> avisos) {
    listViewAvisos.getItems().clear();

    for (Aviso aviso : avisos) {
      String item = formatarAvisoParaExibicao(aviso);
      listViewAvisos.getItems().add(item);
    }
  }

  private String formatarAvisoParaExibicao(Aviso aviso) {
    return String.format(
        "ID: %d\nTítulo: %s\nDescrição: %s\nCategoria: %s",
        aviso.getId(),
        aviso.getTitulo(),
        aviso.getDescricao(),
        aviso.getNomeCategoria()
    );
  }
}