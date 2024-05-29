package epv.tecnologia.lanuz.repos;

import epv.tecnologia.lanuz.domain.Imagen;
import epv.tecnologia.lanuz.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ImagenRepository extends JpaRepository<Imagen, Long> {

    Imagen findFirstByProducto(Producto producto);

}
