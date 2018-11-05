package com.cia.lojao.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cia.lojao.domain.Categoria;
import com.cia.lojao.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	CategoriaService service;

	@GetMapping
	public List<Categoria> listar() {
		return service.listar();
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> buscar(@PathVariable Integer id) {
		Categoria obj = service.buscar(id);
		return ResponseEntity.ok().body(obj);
	}

}
