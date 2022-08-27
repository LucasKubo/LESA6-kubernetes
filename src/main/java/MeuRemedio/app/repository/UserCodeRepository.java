package MeuRemedio.app.repository;

import MeuRemedio.app.models.agendamentos.Agendamento;
import MeuRemedio.app.models.usuarios.Usuario;
import MeuRemedio.app.models.usuarios.Usuario_code;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserCodeRepository extends CrudRepository<Usuario_code, Long> {

    Usuario_code findByEmail(String email);
    Usuario_code findByCodigo(String codigo);

    Usuario_code deleteByCodigo(String codigo);

}
