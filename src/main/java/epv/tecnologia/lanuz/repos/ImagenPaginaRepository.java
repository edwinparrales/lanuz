package epv.tecnologia.lanuz.repos;

import epv.tecnologia.lanuz.domain.ImagenPagina;
import epv.tecnologia.lanuz.domain.Pagina;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImagenPaginaRepository extends JpaRepository<ImagenPagina, Long> {

    ImagenPagina findFirstByPagina(Pagina pagina);

}
