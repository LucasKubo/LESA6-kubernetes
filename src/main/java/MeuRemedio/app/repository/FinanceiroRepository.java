package MeuRemedio.app.repository;

import MeuRemedio.app.models.usuarios.Financeiro;
import MeuRemedio.app.models.usuarios.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceiroRepository extends CrudRepository<Financeiro, Long> {
    Financeiro findById(long id);
    //List<Financeiro> findAllByUsuario(Usuario usuario);
}
