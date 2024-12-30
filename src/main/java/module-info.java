module br.com.eduardo.sistemadistribuido {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;
  requires static lombok;
  requires com.fasterxml.jackson.databind;
  requires java.persistence;
  requires java.validation;
  requires com.sun.istack.runtime;
  requires java.sql;
  requires org.hibernate.orm.core;
  requires javafx.base;

  opens br.com.eduardo.sistemadistribuido.entity to org.hibernate.orm.core, javafx.base, com.fasterxml.jackson.databind;
  opens br.com.eduardo.sistemadistribuido.model.dto to org.hibernate.orm.core;

  exports br.com.eduardo.sistemadistribuido.model.response to com.fasterxml.jackson.databind;
  exports br.com.eduardo.sistemadistribuido.model.dto to com.fasterxml.jackson.databind;

  opens br.com.eduardo.sistemadistribuido.cliente to javafx.fxml;
  exports br.com.eduardo.sistemadistribuido.cliente;

  opens br.com.eduardo.sistemadistribuido.cliente.controller to javafx.fxml;
  exports br.com.eduardo.sistemadistribuido.cliente.controller;

  opens br.com.eduardo.sistemadistribuido.servidor to javafx.fxml;
  exports br.com.eduardo.sistemadistribuido.servidor;

  opens br.com.eduardo.sistemadistribuido.servidor.controller to javafx.fxml;
  exports br.com.eduardo.sistemadistribuido.servidor.controller;

  opens br.com.eduardo.sistemadistribuido.servidor.handler to javafx.fxml;
  exports br.com.eduardo.sistemadistribuido.servidor.handler;

  opens br.com.eduardo.sistemadistribuido.util to javafx.fxml;
  exports br.com.eduardo.sistemadistribuido.util;

  opens br.com.eduardo.sistemadistribuido.model.request to javafx.fxml;
  exports br.com.eduardo.sistemadistribuido.model.request;

  opens br.com.eduardo.sistemadistribuido.servidor.service to javafx.fxml;
  exports br.com.eduardo.sistemadistribuido.servidor.service;

}