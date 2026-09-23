package com.pickcloud.backend.service;

import com.pickcloud.backend.dto.ProductRequest;
import com.pickcloud.backend.dto.ProductResponse;
import com.pickcloud.backend.entity.Business;
import com.pickcloud.backend.entity.Category;
import com.pickcloud.backend.entity.Product;
import com.pickcloud.backend.repository.BusinessRepository;
import com.pickcloud.backend.repository.CategoryRepository;
import com.pickcloud.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BusinessRepository businessRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            BusinessRepository businessRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.businessRepository = businessRepository;
    }

    public ProductResponse crearProducto(ProductRequest request) {

        Business negocio = businessRepository.findById(request.getIdNegocio())
                .orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        Category categoria = categoryRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (!categoria.getNegocio().getIdNegocio().equals(negocio.getIdNegocio())) {
            throw new RuntimeException("La categoría no pertenece al negocio");
        }

        Product producto = new Product();

        producto.setNegocio(negocio);
        producto.setCategoria(categoria);
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioCosto(request.getPrecioCosto());
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setStockActual(request.getStockActual());
        producto.setSku(request.getSku());
        producto.setActivo(request.getActivo());
        producto.setUrlImagen(request.getUrlImagen());
        producto.setDescuento(request.getDescuento());

        Product productoGuardado = productRepository.save(producto);

        return convertirAResponse(productoGuardado);
    }

    public List<ProductResponse> obtenerProductosPorNegocio(Integer idNegocio) {

        return productRepository.findByNegocio_IdNegocio(idNegocio)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public ProductResponse obtenerProducto(Integer idProducto) {

        Product producto = productRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        return convertirAResponse(producto);
    }

    private ProductResponse convertirAResponse(Product producto) {

        ProductResponse response = new ProductResponse();

        response.setIdProducto(producto.getIdProducto());
        response.setIdCategoria(producto.getCategoria().getIdCategoria());
        response.setCategoria(producto.getCategoria().getDescripcion());
        response.setIdNegocio(producto.getNegocio().getIdNegocio());

        response.setNombre(producto.getNombre());
        response.setDescripcion(producto.getDescripcion());
        response.setPrecioCosto(producto.getPrecioCosto());
        response.setPrecioVenta(producto.getPrecioVenta());
        response.setStockActual(producto.getStockActual());
        response.setSku(producto.getSku());
        response.setActivo(producto.getActivo());
        response.setUrlImagen(producto.getUrlImagen());
        response.setDescuento(producto.getDescuento());

        return response;
    }
}