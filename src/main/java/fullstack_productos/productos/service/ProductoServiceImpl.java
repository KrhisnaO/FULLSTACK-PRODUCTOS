package fullstack_productos.productos.service;

import fullstack_productos.productos.model.Producto;
import fullstack_productos.productos.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> listar() {
        logger.info("Service: OBTENIENDO LISTA DE PRODUCTOS");
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        logger.info("Service: BUSCANDO PRODUCTO POR ID: {}", id);
        return productoRepository.findById(id);
    }

    @Override
    public Producto guardar(Producto producto) {
        logger.info("Service: GUARDAR NUEVO PRODUCTO, NOMBRE: {}", producto.getNombre());
        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizar(Long id, Producto producto) {
        logger.info("Service: ACTUALIZANDO PRODUCTO ID: {}", id);

        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());

        return productoRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        logger.warn("Service: ELIMINANDO PRODUCTO ID: {}", id);

        Producto existente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        productoRepository.delete(existente);
    }
}