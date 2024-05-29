package epv.tecnologia.lanuz.repos;

import epv.tecnologia.lanuz.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

}
