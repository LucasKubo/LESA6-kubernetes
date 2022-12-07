package MeuRemedio.app.repository;

import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.models.usuarios.UsuarioNotificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioNotificationTokenRepository extends JpaRepository<UsuarioNotificationToken, Long> {

    List<UsuarioNotificationToken> findAllByIdUsuario(Long idUsuario);
    void deleteAllByIdUsuario(Long idUsuario);
    UsuarioNotificationToken findByIdToken(String token);
}
