package com.cia.lojao.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.cia.lojao.domain.enums.TipoCliente;
import com.cia.lojao.dto.NewClienteDTO;
import com.cia.lojao.resources.exceptions.FieldMessage;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, NewClienteDTO> {
	@Override
	public void initialize(ClienteInsert ann) {
		
	}

	@Override
	public boolean isValid(NewClienteDTO objDto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod())) {
			if (!BR.isValidCPF(objDto.getCpfOuCnpj())){
				list.add(new FieldMessage("cpfOuCnpj", "CPF INVALIDO"));
			}
		}else if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod())) {
			if (!BR.isValidCNPJ(objDto.getCpfOuCnpj())){
				list.add(new FieldMessage("cpfOuCnpj", "CNPJ INVALIDO"));
			}
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}