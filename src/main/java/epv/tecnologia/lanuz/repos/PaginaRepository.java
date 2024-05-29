package epv.tecnologia.lanuz.repos;

import epv.tecnologia.lanuz.domain.CategoriaPagina;
import epv.tecnologia.lanuz.domain.Pagina;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaginaRepository extends JpaRepository<Pagina, Long> {

    Pagina findFirstByCategoria(CategoriaPagina categoriaPagina);

}
