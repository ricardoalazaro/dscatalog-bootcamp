package com.devsuperior.dscatalog.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.devsuperior.dscatalog.dto.UserUpdateDTO;
import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.exceptions.FieldMessage;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public void initialize(UserUpdateValid ann) {
	}

	@Override
	public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {
		
		@SuppressWarnings("unchecked")
		var uriVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE); //pega mapa com atributos da URL
		Long userId = Long.parseLong(uriVars.get("id")); // id é o nome do item no Resource {id} 
		
		List<FieldMessage> list = new ArrayList<>();
		
		// Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista
		User user = repository.findByEmail(dto.getEmail());
		if (user != null && userId != user.getId()) {
			list.add(new FieldMessage("email", "Email já existe"));
		}
		
		for (FieldMessage e : list) { // insere na lista de erros do Beans validation
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty(); //se não tiver erros, retorna lista vazia e True
	}
}