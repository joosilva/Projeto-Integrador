package com.generation.donare.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import com.generation.donare.model.Postagem;
import com.generation.donare.model.Usuario;
import com.generation.donare.model.UsuarioLogin;
import com.generation.donare.repository.PostagemRepository;
import com.generation.donare.repository.UsuarioRepository;
import com.generation.donare.service.PostagemService;
import com.generation.donare.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {
	
	@Autowired
	private PostagemService postagemService;

	@Autowired
	private UsuarioService usuarioService; 
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping("/all")
	public ResponseEntity<List<Usuario>> getAll() {
		return ResponseEntity.ok(usuarioRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> getById(@PathVariable Long id) {
		return usuarioRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("buscaNome/{nome}")
	public ResponseEntity<List<Usuario>> getByNome(@PathVariable String nome) {
		return ResponseEntity.ok(usuarioRepository.findAllByNomeContainingIgnoreCase(nome));
	}
	
	@GetMapping("buscaNickname/{nickname}")
	public ResponseEntity<List<Usuario>> getByNickname(@PathVariable String nickname) {
		return ResponseEntity.ok(usuarioRepository.findAllByNicknameContainingIgnoreCase(nickname));
	}
	
	@PostMapping("/logar")
	public ResponseEntity<UsuarioLogin> login(@RequestBody Optional<UsuarioLogin> usuarioLogin){
		return usuarioService.autenticarUsuario(usuarioLogin)
				.map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
	
	@PostMapping("/cadastrar")
	public ResponseEntity<Usuario> post(@Valid @RequestBody Usuario usuario) {
		return usuarioService.cadastro(usuario)
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
	            .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	}
	
	@PutMapping("/atualizar")
	public ResponseEntity<Usuario> put(@Valid @RequestBody Usuario usuario) {
		return usuarioService.atualizarUsuario(usuario)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);

		if (usuario.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		usuarioRepository.deleteById(id);
	}
	
	//Método Curtir
	@PutMapping("/curtir/{id}")
	public ResponseEntity<Postagem> curtirPostagemId (@PathVariable Long id){
		return postagemService.curtir(id)
				.map(resposta-> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.badRequest().build());
	}
}
 