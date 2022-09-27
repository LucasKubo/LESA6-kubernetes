package MeuRemedio.app.repository;

import MeuRemedio.app.models.remedios.Remedio;
import MeuRemedio.app.models.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RemedioRepository extends JpaRepository<Remedio, Long> {
    Remedio findById (long id);
    List<Remedio> findAllByUsuario(Usuario usuario);



}
