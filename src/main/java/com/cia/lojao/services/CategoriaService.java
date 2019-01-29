package com.cia.lojao.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cia.lojao.domain.Categoria;
import com.cia.lojao.repositories.CategoriaRepository;
import com.cia.lojao.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;
	
	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	public List<Categoria> listar() {;
		return repo.findAll();
	}
	
	public Categoria inserir(Categoria categoria) {
		categoria.setId(null);
		return repo.save(categoria);
	}
}