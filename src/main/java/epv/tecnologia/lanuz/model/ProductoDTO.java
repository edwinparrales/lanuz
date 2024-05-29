package epv.tecnologia.lanuz.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductoDTO {

    private Long id;

    @NotNull
    private Integer cantidad;

    @NotNull
    @Size(max = 255)
    @ProductoCodigoUnique
    private String codigo;

    @NotNull
    @Size(max = 255)
    private String descripcion;

    @Size(max = 255)
    private String marca;

    @Size(max = 255)
    private String modelo;

    @NotNull
    private Double precio;

    private Long categoria;

}
