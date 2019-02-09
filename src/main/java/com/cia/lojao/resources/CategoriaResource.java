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

import com.cia.lojao.domain.Categoria;
import com.cia.lojao.dto.CategoriaDTO;
import com.cia.lojao.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	CategoriaService service;

	@GetMapping
	public ResponseEntity<List<CategoriaDTO>> listar() {
		List<Categoria> lista = service.listar();
		List<CategoriaDTO> listaDTO = lista.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaDTO);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> buscar(@PathVariable Integer id) {
		Categoria obj = service.buscar(id);
		return ResponseEntity.ok().body(obj);
	}

	@PostMapping
	public ResponseEntity<Void> inserir(@Valid @RequestBody CategoriaDTO objDTO){
		Categoria obj = new Categoria(objDTO.getId(), objDTO.getNome());
		service.inserir(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<Void> alterar(@Valid @RequestBody CategoriaDTO catDTO, @PathVariable Integer id){
		Categoria obj = new Categoria(catDTO.getId(), catDTO.getNome());
		obj.setId(id);
		obj = service.atualizar(obj);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<?> excluir(@PathVariable Integer id){
		service.excluir(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping(value="/page")
	public ResponseEntity<Page<CategoriaDTO>> listarPagina(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		Page<Categoria> lista = service.listarPagina(page, linesPerPage, orderBy, direction);
		Page<CategoriaDTO> listaDTO = lista.map(obj -> new CategoriaDTO(obj));
		return ResponseEntity.ok().body(listaDTO);
	}
	
	
	
}
