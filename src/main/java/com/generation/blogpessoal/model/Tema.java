package com.generation.blogpessoal.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity // faz isso ser uma tabela no banco de dados 
@Table (name = "tb_temas") // essa tag da um noma pra tabela
public class Tema {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) // faz essa coluna (id) ter autoincremanto 
	private Long id;
	
	@NotNull(message = "O Atributo Descrição é obrigatório")
	private String descricao;
	
	// fetch.lazy: pega sómente oque esta sendo pedido no momento e não vai carregar todo o banco de dados de uma vez
	// cascade: tudo que for feito nesse tema efetara as postagens relacionadas, no caso do remove, ao apagar esse tema todas as postagens relacionadas serão pagadas também
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tema", cascade = CascadeType.REMOVE) 
	@JsonIgnoreProperties("tema") //anotação para não deixar uma resposta em loop no Json
	private List<Postagens> postagens;
	
	

	public List<Postagens> getPostagens() {
		return postagens;
	}

	public void setPostagens(List<Postagens> postagens) {
		this.postagens = postagens;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
