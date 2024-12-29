module br.com.eduardo.sistemadistribuido {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;
  requires static lombok;
  requires com.fasterxml.jackson.databind;
  requires java.persistence;
  requires java.validation;

  exports br.com.eduardo.sistemadistribuido.model.response to com.fasterxml.jackson.databind;

  opens br.com.eduardo.sistemadistribuido.cliente to javafx.fxml;
  exports br.com.eduardo.sistemadistribuido.cliente;

  opens br.com.eduardo.sistemadistribuido.cliente.controller to javafx.fxml;
  exports br.com.eduardo.sistemadistribuido.cliente.controller;

  opens br.com.eduardo.sistemadistribuido.util to javafx.fxml;
  exports br.com.eduardo.sistemadistribuido.util;

  opens br.com.eduardo.sistemadistribuido.model.request to javafx.fxml;
  exports br.com.eduardo.sistemadistribuido.model.request;
}