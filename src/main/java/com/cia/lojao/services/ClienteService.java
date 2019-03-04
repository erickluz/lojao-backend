package com.cia.lojao.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cia.lojao.domain.Cidade;
import com.cia.lojao.domain.Cliente;
import com.cia.lojao.domain.Endereco;
import com.cia.lojao.domain.enums.TipoCliente;
import com.cia.lojao.dto.NewClienteDTO;
import com.cia.lojao.repositories.ClienteRepository;
import com.cia.lojao.repositories.EnderecoRepository;
import com.cia.lojao.services.exceptions.DataIntegrityException;
import com.cia.lojao.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe; 
	
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
	
	public Cliente fromDTO(NewClienteDTO obj) {
		
		Cliente cliente = new Cliente(null, obj.getNome(), obj.getEmail(), obj.getCpfOuCnpj(), TipoCliente.toEnum(obj.getTipo()), pe.encode(obj.getSenha()));
		
		Set<String> telefones = new HashSet<>();
		
		if(obj.getTelefone1() != null) {
			telefones.add(obj.getTelefone1());
		}
		if(obj.getTelefone2() != null) {
			telefones.add(obj.getTelefone2());
		}
		if(obj.getTelefone3() != null) {
			telefones.add(obj.getTelefone3());
		}
		
		cliente.setTelefones(telefones);
		Cidade cidade = new Cidade(obj.getCidadeId(), null, null);
		Endereco endereco = new Endereco(null, obj.getLogradouro(), obj.getNumero(), null, null, obj.getCep(), cliente, cidade);
		cliente.getEnderecos().add(endereco);
		
		repo.save(cliente);
		enderecoRepository.save(endereco);
		
		
		
		return cliente;
	}

}