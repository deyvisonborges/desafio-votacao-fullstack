package br.com.deyvisonborges.dbservervotingapi;

import org.springframework.boot.SpringApplication;

public class TestDbserverVotiingApiApplication {
  
  public static void main(String[] args) {
    SpringApplication.from(DbserverVotiingApiApplication::main).with(TestcontainersConfiguration.class).run(args);
  }
  
}
