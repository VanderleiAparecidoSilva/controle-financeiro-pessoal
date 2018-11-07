package com.vanderlei.cfp.security;

import com.vanderlei.cfp.entities.Permissao;
import com.vanderlei.cfp.entities.PermissaoUsuario;
import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.gateways.repository.PermissaoRepository;
import com.vanderlei.cfp.gateways.repository.PermissaoUsuarioRepository;
import com.vanderlei.cfp.gateways.repository.UsuarioRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AppUserDetailsService implements UserDetailsService {

  @Autowired public UsuarioRepository usuarioRepository;

  @Autowired private PermissaoUsuarioRepository permissaoUsuarioRepository;

  @Autowired private PermissaoRepository permissaoRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
    Usuario usuario =
        usuarioOptional.orElseThrow(
            () -> new UsernameNotFoundException("Usuário e/ou senha incorretos"));
    return new UsuarioSecurity(usuario, getPermissoes(usuario));
  }

  private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
    Set<SimpleGrantedAuthority> authorities = new HashSet<>();
    List<PermissaoUsuario> permissoesUsuario =
        permissaoUsuarioRepository.findByIdUsuario(usuario.getId());
    permissoesUsuario.forEach(
        pu ->
            authorities.add(
                new SimpleGrantedAuthority(getDescricaoPermissao(pu.getIdPermissao()))));
    return authorities;
  }

  private String getDescricaoPermissao(final String id) {
    Optional<Permissao> permissao = permissaoRepository.findById(id);
    if (permissao.isPresent()) {
      return permissao.get().getDescricao().toUpperCase();
    }

    return StringUtils.EMPTY;
  }
}
