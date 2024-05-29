package epv.tecnologia.lanuz.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoriaDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String descripcion;

    @NotNull
    @Size(max = 255)
    @CategoriaNombreUnique
    private String nombre;

}
