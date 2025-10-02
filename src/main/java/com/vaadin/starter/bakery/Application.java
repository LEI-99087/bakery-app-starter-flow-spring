package com.vaadin.starter.bakery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.vaadin.starter.bakery.app.security.SecurityConfiguration;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.UserRepository;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.MainView;

/**
 * Spring boot web application initializer.
 */

/**
 * Jun's Test
 *Ponto de entrada principal para a aplicação Vaadin Bakery.
 * * Este é o ponto de inicialização do Spring Boot, responsável por configurar
 * e executar a aplicação web. A anotação {@code @SpringBootApplication}
 * habilita a configuração automática do Spring.
 * * <p>
 * O Javadoc também deve mencionar as configurações chave:
 * <ul>
 * <li>**{@code scanBasePackageClasses}**: Define os pacotes onde o Spring deve procurar
 * componentes (Configuration, View, Service, etc.).</li>
 * <li>**{@code exclude}**: Exclui a configuração automática de erro do MVC.</li>
 * <li>**{@code @EnableJpaRepositories}**: Ativa o suporte ao Spring Data JPA para os
 * repositórios do backend.</li>
 * <li>**{@code @EntityScan}**: Especifica a localização das classes de entidade JPA.</li>
 * </ul>
 * * @author [Seu Nome ou Nome do Grupo] 
 * @version 1.0
 * @since 2024-05-20 (Coloque a data de hoje ou a data do início do projeto)
 */

@SpringBootApplication(scanBasePackageClasses = { SecurityConfiguration.class, MainView.class, Application.class,
		UserService.class }, exclude = ErrorMvcAutoConfiguration.class)
@EnableJpaRepositories(basePackageClasses = { UserRepository.class })
@EntityScan(basePackageClasses = { User.class })
public class Application extends SpringBootServletInitializer {

    /**
     * Main method that launches the Spring Boot application.
     *
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Configures the application when deployed as a WAR file.
     *
     * @param application the Spring application builder
     * @return the configured Spring application builder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
}
