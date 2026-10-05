package br.ufsm.salas;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "API de salas UFSM",
				version = "1.0",
				description = "Documentação do módulo de cadastro de salas da UFSM",
				contact = @Contact(name = "Suporte Técnico", email = "suporte@ufsm.br")
		)
)
@SpringBootApplication
/**
 * Ponto de entrada da aplicação Spring Boot.
 *
 * <p>Ao executar o método {@code main}, o Spring cria o contexto da aplicação,
 * conecta as configurações, registra os controllers e inicia o servidor HTTP.</p>
 */
public class  SalasApiApplication {

	/**
	 * Inicializa a API.
	 *
	 * @param args argumentos opcionais passados pela linha de comando
	 */
	public static void main(final String[] args) {
		SpringApplication.run(SalasApiApplication.class, args);
	}
}
