

package MeuRemedio.app.repository;

import MeuRemedio.app.models.usuarios.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario, String> {
    Usuario findByEmail(String email);


    @Query("SELECT u FROM Usuario u WHERE u.verificationCode = ?1")
    public Usuario findByVerificationCode(String code);
}