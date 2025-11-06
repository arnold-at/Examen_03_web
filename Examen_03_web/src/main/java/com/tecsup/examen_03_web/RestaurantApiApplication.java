package com.tecsup.examen_03_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Habilita auditoría automática (createdDate, lastModifiedDate)
public class RestaurantApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantApiApplication.class, args);
        System.out.println("\n" +
                "==========================================================\n" +
                "   🍽️  RESTAURANT SYSTEM INICIADO CORRECTAMENTE! 🚀      \n" +
                "==========================================================\n" +
                "   Puerto: http://localhost:9090\n" +
                "   Login: http://localhost:9090/login\n" +
                "   Dashboard: http://localhost:9090/dashboard\n" +
                "==========================================================\n");
    }

}