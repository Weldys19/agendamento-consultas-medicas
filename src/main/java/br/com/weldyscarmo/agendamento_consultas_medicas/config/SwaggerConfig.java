package br.com.weldyscarmo.agendamento_consultas_medicas.config;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Agendamento de consultas médicas")
                        .description("API de agendendamento para consultas médicas")
                        .version("1"));
    }
}
