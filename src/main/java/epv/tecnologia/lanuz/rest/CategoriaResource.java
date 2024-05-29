package epv.tecnologia.lanuz.rest;

import epv.tecnologia.lanuz.model.CategoriaDTO;
import epv.tecnologia.lanuz.service.CategoriaService;
import epv.tecnologia.lanuz.util.ReferencedException;
import epv.tecnologia.lanuz.util.ReferencedWarning;
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
@RequestMapping(value = "/api/categorias", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoriaResource {

    private final CategoriaService categoriaService;

    public CategoriaResource(final CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> getAllCategorias() {
        return ResponseEntity.ok(categoriaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> getCategoria(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(categoriaService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createCategoria(
            @RequestBody @Valid final CategoriaDTO categoriaDTO) {
        final Long createdId = categoriaService.create(categoriaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCategoria(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CategoriaDTO categoriaDTO) {
        categoriaService.update(id, categoriaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = categoriaService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
