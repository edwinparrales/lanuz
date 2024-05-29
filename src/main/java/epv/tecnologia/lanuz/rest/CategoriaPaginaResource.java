package epv.tecnologia.lanuz.rest;

import epv.tecnologia.lanuz.model.CategoriaPaginaDTO;
import epv.tecnologia.lanuz.service.CategoriaPaginaService;
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
@RequestMapping(value = "/api/categoriaPaginas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoriaPaginaResource {

    private final CategoriaPaginaService categoriaPaginaService;

    public CategoriaPaginaResource(final CategoriaPaginaService categoriaPaginaService) {
        this.categoriaPaginaService = categoriaPaginaService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaPaginaDTO>> getAllCategoriaPaginas() {
        return ResponseEntity.ok(categoriaPaginaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaPaginaDTO> getCategoriaPagina(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(categoriaPaginaService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createCategoriaPagina(
            @RequestBody @Valid final CategoriaPaginaDTO categoriaPaginaDTO) {
        final Long createdId = categoriaPaginaService.create(categoriaPaginaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCategoriaPagina(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CategoriaPaginaDTO categoriaPaginaDTO) {
        categoriaPaginaService.update(id, categoriaPaginaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoriaPagina(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = categoriaPaginaService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        categoriaPaginaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
