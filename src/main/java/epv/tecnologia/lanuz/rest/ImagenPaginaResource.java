package epv.tecnologia.lanuz.rest;

import epv.tecnologia.lanuz.model.ImagenPaginaDTO;
import epv.tecnologia.lanuz.service.ImagenPaginaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/imagenPaginas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImagenPaginaResource {

    private final ImagenPaginaService imagenPaginaService;

    public ImagenPaginaResource(final ImagenPaginaService imagenPaginaService) {
        this.imagenPaginaService = imagenPaginaService;
    }

    @GetMapping
    public ResponseEntity<List<ImagenPaginaDTO>> getAllImagenPaginas() {
        return ResponseEntity.ok(imagenPaginaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImagenPaginaDTO> getImagenPagina(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(imagenPaginaService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createImagenPagina(
            @RequestBody @Valid final ImagenPaginaDTO imagenPaginaDTO) {
        final Long createdId = imagenPaginaService.create(imagenPaginaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateImagenPagina(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ImagenPaginaDTO imagenPaginaDTO) {
        imagenPaginaService.update(id, imagenPaginaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImagenPagina(@PathVariable(name = "id") final Long id) {
        imagenPaginaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
