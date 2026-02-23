package br.com.deyvisonborges.dbservervotingapi;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class DbserverVotiingApiApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(DbserverVotiingApiApplication.class, args);
  }
  
  @PostConstruct
  public void init() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }
}
