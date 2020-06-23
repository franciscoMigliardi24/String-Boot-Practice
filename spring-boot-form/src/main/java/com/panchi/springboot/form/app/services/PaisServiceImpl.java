package com.panchi.springboot.form.app.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.panchi.springboot.form.app.models.domain.Pais;

@Service
public class PaisServiceImpl implements PaisService {

	private List<Pais> lista;

	public PaisServiceImpl() {
		this.lista = Arrays.asList(
				new Pais(1, "ES", "Espania"), 
				new Pais(2, "MX", "mexico"), 
				new Pais(3, "CL", "chile"),
				new Pais(4, "AR", "Argentina"));
	}

	@Override
	public List<Pais> listar() {
		return lista;
	}

	@Override
	public Pais obtenerPorId(Integer id) {
		Pais resultado = null;
		for(Pais pais: this.lista) {
			if(id == pais.getId()) {
				resultado = pais;
				break;
			}
		}
		return resultado;
	}

}
