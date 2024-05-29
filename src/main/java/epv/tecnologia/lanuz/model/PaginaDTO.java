package epv.tecnologia.lanuz.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaginaDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String titulo;

    @NotNull
    private String contenido;

    private Long categoria;

}
