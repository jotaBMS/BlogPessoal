package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		
		usuarioRepository.deleteAll();
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Root", "root@root.com", "rootroot", "-"));
		
	}
	
	@Test
	@DisplayName("Cadastrar Um Usuario")
	public void deveCriarUmUsuario() {
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, "Bruna", "bruna@root.com", "brunaroot", "-"));
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST,corpoRequisicao, Usuario.class);
	
		assertEquals(HttpStatus.CREATED,corpoResposta.getStatusCode());
	
	}
	
	
	@Test
	@DisplayName("Não deve permitir duplicação de um Usuario")
	public void naoDeveDuplicarUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Maria", "maria@root.com", "mariaroot", "-"));
		
		HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, "Maria", "maria@root.com", "mariaroot", "-"));
		
		ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST,corpoRequisicao, Usuario.class);

		assertEquals(HttpStatus.BAD_REQUEST,corpoResposta.getStatusCode());
		
	}
	
	@Test
    @DisplayName("Atualizar um Usuário")
    public void deveAtualizarUmUsuario() {

        Optional<Usuario> usuarioCadastrado = usuarioService
                .cadastrarUsuario(new Usuario(0L, "Juliana Jules", "juliana_jules@gmail.com", "juliana123", "-"));

        Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(), "Juliana Jules Silva",
                "juliana_jules_silva@gmail.com", "juliana123", "-");

        HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);

        ResponseEntity<Usuario> corpoResposta = testRestTemplate.withBasicAuth("root@root.com", "rootroot")
                .exchange("/usuarios/atualizar", HttpMethod.PUT, corpoRequisicao, Usuario.class);

        assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
    }
	
	@Test
	@DisplayName("Listar todos os Usuario")
	public void deveMostrarTodosUsuarios() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Sabrina", "sabrina@root.com", "sabrinaroot", "-"));
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Ricardo", "ricardo@root.com", "ricardoroot", "-"));
		
		ResponseEntity<String> resposta = testRestTemplate.withBasicAuth("root@root.com", "rootroot").exchange("/usuarios/all", HttpMethod.GET, null, String.class);
	
		assertEquals(HttpStatus.OK,resposta.getStatusCode());
		
	}
	
	
	

}
