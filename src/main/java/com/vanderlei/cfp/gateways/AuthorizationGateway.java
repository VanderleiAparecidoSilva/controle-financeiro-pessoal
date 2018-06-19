package com.vanderlei.cfp.gateways;

import com.vanderlei.cfp.email.EmailService;
import com.vanderlei.cfp.entities.Usuario;
import com.vanderlei.cfp.exceptions.ObjectNotFoundException;
import com.vanderlei.cfp.gateways.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthorizationGateway {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private Random random = new Random();

    public void enviarNovaSenha(final String email) {
        Optional<Usuario> obj = usuarioRepository.findByEmail(email);
        Usuario usuario = obj.orElseThrow(() ->
                new ObjectNotFoundException("Usuário com o e-mail: " + email + " não encontrado"));

        String novaSenha = novaSenha();
        usuario.setSenha(passwordEncoder.encode(novaSenha));

        usuarioRepository.save(usuario);
        emailService.enviarNovasenhaEmail(usuario, novaSenha);
    }

    private String novaSenha() {
        char[] vet = new char[10];
        for (int i = 0; i < 10; i++) {
            vet[i] = randomChar();
        }

        return new String(vet);
    }

    private char randomChar() {
        int opt = random.nextInt(3);
        if (opt == 0) { //gera digito
            return (char) (random.nextInt(10) + 48);
        } else if (opt == 1) { //gera letra maiuscula
            return (char) (random.nextInt(26) + 65);
        } else { //gera letra minuscula
            return (char) (random.nextInt(26) + 97);
        }
    }
}