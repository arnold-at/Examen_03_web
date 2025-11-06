package com.tecsup.examen_03_web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

/**
 * Configuración de Spring MVC y Thymeleaf
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Dialecto de seguridad para Thymeleaf
     * Permite usar th:sec en las plantillas
     */
    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    /**
     * Configurar controladores de vista simples
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/").setViewName("redirect:/dashboard");
    }
}