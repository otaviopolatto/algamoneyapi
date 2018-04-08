package com.example.algamoney.api.util;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.example.algamoney.api.model.Usuario;

public class UsuarioSistema extends User {
	
	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;
	
	/* UsuarioSistema é um usuário personalizado que extende de User e contém as
	 * informações de e-mail e senha */
	public UsuarioSistema(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getEmail(), usuario.getSenha(), authorities);
		this.usuario = usuario;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	

}
