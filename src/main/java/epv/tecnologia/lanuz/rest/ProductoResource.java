package epv.tecnologia.lanuz.rest;

import epv.tecnologia.lanuz.model.ProductoDTO;
import epv.tecnologia.lanuz.service.ProductoService;
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
@RequestMapping(value = "/api/productos", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductoResource {

    private final ProductoService productoService;

    public ProductoResource(final ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getAllProductos() {
        return ResponseEntity.ok(productoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProducto(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(productoService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createProducto(@RequestBody @Valid final ProductoDTO productoDTO) {
        final Long createdId = productoService.create(productoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateProducto(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ProductoDTO productoDTO) {
        productoService.update(id, productoDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = productoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
