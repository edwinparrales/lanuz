package epv.tecnologia.lanuz.repos;

import epv.tecnologia.lanuz.domain.Categoria;
import epv.tecnologia.lanuz.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Producto findFirstByCategoria(Categoria categoria);

    boolean existsByCodigoIgnoreCase(String codigo);

}
