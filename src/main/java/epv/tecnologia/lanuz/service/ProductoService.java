package epv.tecnologia.lanuz.service;

import epv.tecnologia.lanuz.domain.Categoria;
import epv.tecnologia.lanuz.domain.Imagen;
import epv.tecnologia.lanuz.domain.Producto;
import epv.tecnologia.lanuz.model.ProductoDTO;
import epv.tecnologia.lanuz.repos.CategoriaRepository;
import epv.tecnologia.lanuz.repos.ImagenRepository;
import epv.tecnologia.lanuz.repos.ProductoRepository;
import epv.tecnologia.lanuz.util.NotFoundException;
import epv.tecnologia.lanuz.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ImagenRepository imagenRepository;

    public ProductoService(final ProductoRepository productoRepository,
            final CategoriaRepository categoriaRepository,
            final ImagenRepository imagenRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.imagenRepository = imagenRepository;
    }

    public List<ProductoDTO> findAll() {
        final List<Producto> productoes = productoRepository.findAll(Sort.by("id"));
        return productoes.stream()
                .map(producto -> mapToDTO(producto, new ProductoDTO()))
                .toList();
    }

    public ProductoDTO get(final Long id) {
        return productoRepository.findById(id)
                .map(producto -> mapToDTO(producto, new ProductoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProductoDTO productoDTO) {
        final Producto producto = new Producto();
        mapToEntity(productoDTO, producto);
        return productoRepository.save(producto).getId();
    }

    public void update(final Long id, final ProductoDTO productoDTO) {
        final Producto producto = productoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productoDTO, producto);
        productoRepository.save(producto);
    }

    public void delete(final Long id) {
        productoRepository.deleteById(id);
    }

    private ProductoDTO mapToDTO(final Producto producto, final ProductoDTO productoDTO) {
        productoDTO.setId(producto.getId());
        productoDTO.setCantidad(producto.getCantidad());
        productoDTO.setCodigo(producto.getCodigo());
        productoDTO.setDescripcion(producto.getDescripcion());
        productoDTO.setMarca(producto.getMarca());
        productoDTO.setModelo(producto.getModelo());
        productoDTO.setPrecio(producto.getPrecio());
        productoDTO.setCategoria(producto.getCategoria() == null ? null : producto.getCategoria().getId());
        return productoDTO;
    }

    private Producto mapToEntity(final ProductoDTO productoDTO, final Producto producto) {
        producto.setCantidad(productoDTO.getCantidad());
        producto.setCodigo(productoDTO.getCodigo());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setMarca(productoDTO.getMarca());
        producto.setModelo(productoDTO.getModelo());
        producto.setPrecio(productoDTO.getPrecio());
        final Categoria categoria = productoDTO.getCategoria() == null ? null : categoriaRepository.findById(productoDTO.getCategoria())
                .orElseThrow(() -> new NotFoundException("categoria not found"));
        producto.setCategoria(categoria);
        return producto;
    }

    public boolean codigoExists(final String codigo) {
        return productoRepository.existsByCodigoIgnoreCase(codigo);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Producto producto = productoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Imagen productoImagen = imagenRepository.findFirstByProducto(producto);
        if (productoImagen != null) {
            referencedWarning.setKey("producto.imagen.producto.referenced");
            referencedWarning.addParam(productoImagen.getId());
            return referencedWarning;
        }
        return null;
    }

}
