package epv.tecnologia.lanuz.rest;

import epv.tecnologia.lanuz.model.PaginaDTO;
import epv.tecnologia.lanuz.service.PaginaService;
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
@RequestMapping(value = "/api/paginas", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaginaResource {

    private final PaginaService paginaService;

    public PaginaResource(final PaginaService paginaService) {
        this.paginaService = paginaService;
    }

    @GetMapping
    public ResponseEntity<List<PaginaDTO>> getAllPaginas() {
        return ResponseEntity.ok(paginaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaginaDTO> getPagina(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(paginaService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createPagina(@RequestBody @Valid final PaginaDTO paginaDTO) {
        final Long createdId = paginaService.create(paginaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePagina(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PaginaDTO paginaDTO) {
        paginaService.update(id, paginaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePagina(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = paginaService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        paginaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
