package epv.tecnologia.lanuz.service;

import epv.tecnologia.lanuz.domain.Categoria;
import epv.tecnologia.lanuz.domain.Producto;
import epv.tecnologia.lanuz.model.CategoriaDTO;
import epv.tecnologia.lanuz.repos.CategoriaRepository;
import epv.tecnologia.lanuz.repos.ProductoRepository;
import epv.tecnologia.lanuz.util.NotFoundException;
import epv.tecnologia.lanuz.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    public CategoriaService(final CategoriaRepository categoriaRepository,
            final ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    public List<CategoriaDTO> findAll() {
        final List<Categoria> categorias = categoriaRepository.findAll(Sort.by("id"));
        return categorias.stream()
                .map(categoria -> mapToDTO(categoria, new CategoriaDTO()))
                .toList();
    }

    public CategoriaDTO get(final Long id) {
        return categoriaRepository.findById(id)
                .map(categoria -> mapToDTO(categoria, new CategoriaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CategoriaDTO categoriaDTO) {
        final Categoria categoria = new Categoria();
        mapToEntity(categoriaDTO, categoria);
        return categoriaRepository.save(categoria).getId();
    }

    public void update(final Long id, final CategoriaDTO categoriaDTO) {
        final Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoriaDTO, categoria);
        categoriaRepository.save(categoria);
    }

    public void delete(final Long id) {
        categoriaRepository.deleteById(id);
    }

    private CategoriaDTO mapToDTO(final Categoria categoria, final CategoriaDTO categoriaDTO) {
        categoriaDTO.setId(categoria.getId());
        categoriaDTO.setDescripcion(categoria.getDescripcion());
        categoriaDTO.setNombre(categoria.getNombre());
        return categoriaDTO;
    }

    private Categoria mapToEntity(final CategoriaDTO categoriaDTO, final Categoria categoria) {
        categoria.setDescripcion(categoriaDTO.getDescripcion());
        categoria.setNombre(categoriaDTO.getNombre());
        return categoria;
    }

    public boolean nombreExists(final String nombre) {
        return categoriaRepository.existsByNombreIgnoreCase(nombre);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Producto categoriaProducto = productoRepository.findFirstByCategoria(categoria);
        if (categoriaProducto != null) {
            referencedWarning.setKey("categoria.producto.categoria.referenced");
            referencedWarning.addParam(categoriaProducto.getId());
            return referencedWarning;
        }
        return null;
    }

}
