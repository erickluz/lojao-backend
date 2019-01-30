package com.cia.lojao.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cia.lojao.domain.Pedido;
import com.cia.lojao.repositories.PedidoRepository;

@Service
public class PedidoService {

	@Autowired
	PedidoRepository repo;
	
	public List<Pedido> listar(){
		return repo.findAll();
	}
	
	public Optional<Pedido> buscar(Integer id){
		return repo.findById(id);
	}
	
	public void inserir(Pedido obj) {
		obj.setId(null);
		repo.save(obj);
	}
	
	public void alterar(Pedido obj) {
		repo.save(obj);
	}
	
	public void excluir(Integer id) {
		buscar(id);
		repo.deleteById(id);
	}
	
	
}
