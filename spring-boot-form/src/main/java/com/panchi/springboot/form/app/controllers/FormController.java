package com.panchi.springboot.form.app.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.panchi.springboot.form.app.editors.NombreMayusculaEditor;
import com.panchi.springboot.form.app.editors.PaisPropertyEditor;
import com.panchi.springboot.form.app.editors.RolesEditor;
import com.panchi.springboot.form.app.models.domain.Pais;
import com.panchi.springboot.form.app.models.domain.Role;
import com.panchi.springboot.form.app.models.domain.Usuario;
import com.panchi.springboot.form.app.services.PaisService;
import com.panchi.springboot.form.app.services.RoleService;
import com.panchi.springboot.form.app.validation.UsuarioValidador;

@Controller
@SessionAttributes("usuario")
public class FormController {

	@Autowired
	private UsuarioValidador validador;
	
	@Autowired
	private PaisService paisService;
	
	@Autowired
	private PaisPropertyEditor paisPropertyEditor;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RolesEditor roleEditor;
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(validador);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, "fechaNacimiento", new CustomDateEditor(dateFormat, false));
		
		binder.registerCustomEditor(String.class, "nombre",new NombreMayusculaEditor());
		binder.registerCustomEditor(String.class, "apellido",new NombreMayusculaEditor());
		
		binder.registerCustomEditor(Pais.class, "pais", paisPropertyEditor);
		binder.registerCustomEditor(Role.class, "roles", roleEditor);
	}
	
	@GetMapping("/form")
	public String form(Model model) {
		Usuario usuario = new Usuario();
		usuario.setNombre("panchi");
		usuario.setApellido("migl");
		usuario.setIdentificador("12.456.789-K");
		usuario.setHabilitar(true);
		usuario.setValorSecreto("algun valor secreto jejej");
		usuario.setPais(new Pais(4, "AR", "Argentina"));
		usuario.setRoles(Arrays.asList(new Role(2, "Usuario", "ROLE_USER")));
		
		model.addAttribute("titulo", "Crear usuario");
		model.addAttribute("usuario", usuario);
		return "form";
	}
	
	@PostMapping("/form")
	public String procesar(@Valid Usuario usuario, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Resultado del form");
			return "form";
		}
		return "redirect:/ver";
	}
	
	@GetMapping("/ver")
	public String ver(@SessionAttribute(name="usuario", required = false) Usuario usuario, Model model, SessionStatus status) {
		
		if (usuario==null) {
			return "redirect:/form"; 
		}
		model.addAttribute("titulo", "Resultado del form");
		status.setComplete();
		return "resultado";
	}

	
	@ModelAttribute("genero")
	public List<String> genero(){
		return Arrays.asList("Hombre", "Mujer");
	}
	
	@ModelAttribute("paises")
	public List<String> paises(){
		return Arrays.asList("Espania", "mexico", "chile", "Argentina");
	}
	
	@ModelAttribute("paisesMap")
	public Map<String, String> paisesMap(){
//		return Arrays.asList("Espania", "mexico", "chile", "Argentina");
		Map<String, String> paises = new HashMap<String, String>();
		paises.put("ES", "Espania");
		paises.put("MX", "mexico");
		paises.put("CL", "chile");
		paises.put("AR", "Argentina");
		
		return paises;
	}
	
	@ModelAttribute("listaPaises")
	public List<Pais> listaPaises(){
		return paisService.listar();
	}
	
	@ModelAttribute("listaRolesString")
	public List<String> listaRolesString(){
		List<String> roles = new ArrayList<>();
		roles.add("ROLE_ADMIN");
		roles.add("ROLE_USER");
		roles.add("ROLE_MODERATOR");
		return roles;
	}
	
	@ModelAttribute("listaRolesMap")
	public Map<String, String> listaRolesMap(){
		Map<String, String> roles = new HashMap<String, String>();
		roles.put("ROLE_ADMIN", "Administrador");
		roles.put("ROLE_USER", "usuario");
		roles.put("ROLE_MODERATOR", "Moderador");
		
		return roles;
	}
	
	@ModelAttribute("listaRoles")
	public List<Role> listaRoles(){
		return this.roleService.listar();
	}
	
}
