package MeuRemedio.app.repository;

import MeuRemedio.app.models.usuarios.TokenResetarSenha;
import MeuRemedio.app.models.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenResetarSenhaRepository extends JpaRepository<TokenResetarSenha, Long> {
    TokenResetarSenha findByToken(String token);

    void deleteByToken(String token);

    TokenResetarSenha findByUsuario(Usuario usuario);

    void deleteByUsuario(Usuario usuario);
}
