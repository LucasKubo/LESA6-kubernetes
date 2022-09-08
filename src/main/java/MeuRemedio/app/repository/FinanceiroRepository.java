package MeuRemedio.app.repository;

import MeuRemedio.app.models.usuarios.Gasto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceiroRepository extends CrudRepository<Gasto, Long> {
}
