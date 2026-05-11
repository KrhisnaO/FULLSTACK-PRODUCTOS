package fullstack_productos.productos;

import fullstack_productos.productos.model.Producto;
import fullstack_productos.productos.repository.ProductoRepository;
import fullstack_productos.productos.service.ProductoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para ProductoServiceImpl.
 * Cubre los métodos: listar, buscarPorId, guardar, actualizar y eliminar.
 */
@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto productoBase;

    @BeforeEach
    void setUp() {
        productoBase = new Producto(1L, "Laptop Gamer", "Laptop de alto rendimiento", 999.99, 10);
    }

    // ---- listar() ----

    @Test
    void listar_retornaListaDeProductos() {
        Producto p2 = new Producto(2L, "Mouse Inalámbrico", "Mouse ergonómico", 29.99, 50);
        when(productoRepository.findAll()).thenReturn(List.of(productoBase, p2));

        List<Producto> resultado = productoService.listar();

        assertEquals(2, resultado.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void listar_sinProductos_retornaListaVacia() {
        when(productoRepository.findAll()).thenReturn(List.of());

        List<Producto> resultado = productoService.listar();

        assertTrue(resultado.isEmpty());
    }

    // ---- buscarPorId() ----

    @Test
    void buscarPorId_existente_retornaProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoBase));

        Optional<Producto> resultado = productoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Laptop Gamer", resultado.get().getNombre());
        assertEquals(999.99, resultado.get().getPrecio());
    }

    @Test
    void buscarPorId_noExistente_retornaEmpty() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Producto> resultado = productoService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
    }

    // ---- guardar() ----

    @Test
    void guardar_productoValido_retornaProductoGuardado() {
        when(productoRepository.save(any(Producto.class))).thenReturn(productoBase);

        Producto resultado = productoService.guardar(productoBase);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Laptop Gamer", resultado.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    // ---- actualizar() ----

    @Test
    void actualizar_productoExistente_retornaProductoActualizado() {
        Producto datosNuevos = new Producto(
                null, "Laptop Pro", "Versión mejorada", 1299.99, 5
        );
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoBase));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        Producto resultado = productoService.actualizar(1L, datosNuevos);

        assertEquals("Laptop Pro", resultado.getNombre());
        assertEquals(1299.99, resultado.getPrecio());
        assertEquals(5, resultado.getStock());
    }

    @Test
    void actualizar_productoNoExistente_lanzaRuntimeException() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> productoService.actualizar(99L, productoBase));

        verify(productoRepository, never()).save(any());
    }

    // ---- eliminar() ----

    @Test
    void eliminar_productoExistente_llamaDelete() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoBase));
        doNothing().when(productoRepository).delete(productoBase);

        assertDoesNotThrow(() -> productoService.eliminar(1L));

        verify(productoRepository, times(1)).delete(productoBase);
    }

    @Test
    void eliminar_productoNoExistente_lanzaRuntimeException() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> productoService.eliminar(99L));

        verify(productoRepository, never()).delete(any());
    }
}