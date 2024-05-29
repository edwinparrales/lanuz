package epv.tecnologia.lanuz.rest;

import epv.tecnologia.lanuz.model.ImagenDTO;
import epv.tecnologia.lanuz.service.ImagenService;
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
@RequestMapping(value = "/api/imagens", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImagenResource {

    private final ImagenService imagenService;

    public ImagenResource(final ImagenService imagenService) {
        this.imagenService = imagenService;
    }

    @GetMapping
    public ResponseEntity<List<ImagenDTO>> getAllImagens() {
        return ResponseEntity.ok(imagenService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImagenDTO> getImagen(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(imagenService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createImagen(@RequestBody @Valid final ImagenDTO imagenDTO) {
        final Long createdId = imagenService.create(imagenDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateImagen(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ImagenDTO imagenDTO) {
        imagenService.update(id, imagenDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImagen(@PathVariable(name = "id") final Long id) {
        imagenService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
