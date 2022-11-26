package MeuRemedio.app.repository;

import MeuRemedio.app.models.dash.Dash;
import MeuRemedio.app.models.usuarios.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DashBoardsRepository extends CrudRepository<Dash, Long> {

    void deleteAllByUsuario(Usuario usuario);
}
