package fullstack_productos.productos;

import com.fasterxml.jackson.databind.ObjectMapper;
import fullstack_productos.productos.controller.ProductoController;
import fullstack_productos.productos.model.Producto;
import fullstack_productos.productos.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para ProductoController usando MockMvc.
 * Verifica los endpoints REST: listar, buscarPorId, crear, actualizar y eliminar.
 */
@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto buildProducto() {
        return new Producto(1L, "Laptop Gamer", "Laptop de alto rendimiento", 999.99, 10);
    }

    // ---- GET /api/productos ----

    @Test
    void listar_retorna200ConListaDeProductos() throws Exception {
        Producto p2 = new Producto(2L, "Mouse Inalámbrico", "Mouse ergonómico", 29.99, 50);
        when(productoService.listar()).thenReturn(List.of(buildProducto(), p2));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ---- GET /api/productos/{id} ----

    @Test
    void obtenerPorId_existente_retorna200() throws Exception {
        when(productoService.buscarPorId(1L)).thenReturn(Optional.of(buildProducto()));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Laptop Gamer"))
                .andExpect(jsonPath("$.precio").value(999.99));
    }

    @Test
    void obtenerPorId_noExistente_retorna404() throws Exception {
        when(productoService.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    // ---- POST /api/productos ----

    @Test
    void crear_productoValido_retorna201() throws Exception {
        when(productoService.guardar(any(Producto.class))).thenReturn(buildProducto());

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildProducto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Laptop Gamer"));
    }

    // ---- PUT /api/productos/{id} ----

    @Test
    void actualizar_productoExistente_retorna200() throws Exception {
        Producto actualizado = new Producto(1L, "Laptop Pro", "Versión mejorada", 1299.99, 5);
        when(productoService.actualizar(eq(1L), any(Producto.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laptop Pro"))
                .andExpect(jsonPath("$.precio").value(1299.99));
    }

    @Test
    void actualizar_productoNoExistente_retorna404() throws Exception {
        when(productoService.actualizar(eq(99L), any(Producto.class)))
                .thenThrow(new RuntimeException("Producto no encontrado con id: 99"));

        mockMvc.perform(put("/api/productos/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildProducto())))
                .andExpect(status().isNotFound());
    }

    // ---- DELETE /api/productos/{id} ----

    @Test
    void eliminar_productoExistente_retorna204() throws Exception {
        doNothing().when(productoService).eliminar(1L);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_productoNoExistente_retorna404() throws Exception {
        doThrow(new RuntimeException("Producto no encontrado con id: 99"))
                .when(productoService).eliminar(99L);

        mockMvc.perform(delete("/api/productos/99"))
                .andExpect(status().isNotFound());
    }
}