package io.github.ahumadamob.plangastos.config;

import io.github.ahumadamob.plangastos.entity.Usuario;
import io.github.ahumadamob.plangastos.repository.UsuarioRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevUsuarioSeedConfig {

    @Bean
    @ConditionalOnProperty(
            name = "plangastos.seed.dev-users.enabled",
            havingValue = "true",
            matchIfMissing = false)
    CommandLineRunner seedUsuariosDev(UsuarioRepository usuarioRepository) {
        return args -> {
            List<Usuario> usuarios = List.of(
                    buildUsuario("dev.admin@plangastos.local", "$2a$10$Ndjvn85xwPQsVgj5t8aBMOslqq9KQvvhOIC0M7fL9qYwJafJ06dUi"),
                    buildUsuario("dev.user@plangastos.local", "$2a$10$M1w8w2Z8QxSNabenEwPhMuf7A4E6C7fjcs6M58f35H5TADaBrZEc6"));

            for (Usuario usuario : usuarios) {
                if (usuarioRepository.findByEmail(usuario.getEmail()).isEmpty()) {
                    usuarioRepository.save(usuario);
                }
            }
        };
    }

    private Usuario buildUsuario(String email, String passwordHash) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPasswordHash(passwordHash);
        usuario.setActivo(true);
        return usuario;
    }
}
