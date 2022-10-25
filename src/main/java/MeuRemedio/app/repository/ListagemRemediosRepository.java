package MeuRemedio.app.repository;

import MeuRemedio.app.models.remedios.ListagemRemedios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ListagemRemediosRepository extends JpaRepository<ListagemRemedios, Long> {

    ListagemRemedios findById(long id);

    @Query(value = "select u from ListagemRemedios u " +
            "WHERE u.nome LIKE CONCAT('%',:nome,'%')")
    List<ListagemRemedios> buscarPorNome(@Param("nome") String nome);

//    List<ListagemRemedios> findByNomeContaining(String nome);

}
