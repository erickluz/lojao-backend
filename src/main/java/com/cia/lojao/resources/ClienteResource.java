package com.cia.lojao.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cia.lojao.domain.Cliente;
import com.cia.lojao.dto.ClienteDTO;
import com.cia.lojao.dto.NewClienteDTO;
import com.cia.lojao.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

	@Autowired
	ClienteService service;

	@GetMapping
	public ResponseEntity<List<ClienteDTO>> listar() {
		List<Cliente> cliente = service.listar();
		List<ClienteDTO> clienteDTO = cliente.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(clienteDTO);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Cliente> buscar(@PathVariable Integer id) {
		Cliente obj = service.buscar(id);
		return ResponseEntity.ok().body(obj);
	}

	@PostMapping
	public ResponseEntity<Void> inserir(@Valid @RequestBody NewClienteDTO obj){
		Cliente cliente = service.fromDTO(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<Void> alterar(@Valid @RequestBody ClienteDTO obj, @PathVariable Integer id){
		Cliente cliente = new Cliente(obj.getId(), obj.getNome(), obj.getEmail(), null, null);
		cliente.setId(id);
		cliente = service.atualizar(cliente);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<?> excluir(@PathVariable Integer id){
		service.excluir(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(value="/page")
	public ResponseEntity<Page<ClienteDTO>> listarPagina(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		Page<Cliente> lista = service.listarPagina(page, linesPerPage, orderBy, direction);
		Page<ClienteDTO> listaDTO = lista.map(obj -> new ClienteDTO(obj));
		return ResponseEntity.ok().body(listaDTO);
	}
	
	
}
