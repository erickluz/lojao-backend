package com.cia.lojao.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cia.lojao.domain.Cliente;
import com.cia.lojao.repositories.ClienteRepository;
import com.cia.lojao.services.exceptions.DataIntegrityException;
import com.cia.lojao.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	public Cliente buscar(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	public List<Cliente> listar() {;
		return repo.findAll();
	}
	
	public Cliente inserir(Cliente cliente) {
		cliente.setId(null);
		return repo.save(cliente);
	}
	
	public Cliente atualizar(Cliente cliente) {
		Cliente obj = buscar(cliente.getId());
		cliente.setCpfOuCnpj(obj.getCpfOuCnpj());
		cliente.setTipo(obj.getTipo());
		return repo.save(cliente);
	}
	
	public void excluir(Integer id) {
		buscar(id);
		try {
			repo.deleteById(id);
		}catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Nao e possivel excluir um cliente porque ha entidades relacionadas", e);
		}
	}
	
	public Page<Cliente> listarPagina(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	
	
}