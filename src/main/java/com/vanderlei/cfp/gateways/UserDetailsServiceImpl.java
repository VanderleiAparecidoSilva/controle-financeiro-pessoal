package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.gateways.repository.UsuarioRepository;
import com.vanderlei.cfp.security.UsuarioSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    public UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        Optional<Usuario> obj = usuarioRepository.findByEmail(email);
        Usuario usuario = obj.orElseThrow(() -> new UsernameNotFoundException(email));
        return new UsuarioSecurity(usuario.getId(), usuario.getEmail(), usuario.getSenha(), usuario.getPerfis());
    }
}