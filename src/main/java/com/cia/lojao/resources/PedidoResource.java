package com.cia.lojao.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cia.lojao.domain.Pedido;
import com.cia.lojao.services.PedidoService;

@RestController
@RequestMapping(value="/pedidos")
public class PedidoResource {

	@Autowired
	PedidoService pedidos;
	
	@GetMapping(value="/{id}")
	public ResponseEntity<?> listar(@PathVariable Integer id){
		return ResponseEntity.ok(pedidos.buscar(id));
	}
	
	@PostMapping
	public ResponseEntity<Void> inserir(@RequestBody Pedido obj){
		pedidos.inserir(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping
	public ResponseEntity<Void> alterar(@RequestBody Pedido obj, @PathVariable Integer id){
		obj.setId(id);
		pedidos.alterar(obj);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Integer id){
		pedidos.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
