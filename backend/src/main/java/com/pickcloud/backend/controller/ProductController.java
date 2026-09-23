package com.pickcloud.backend.controller;

import com.pickcloud.backend.dto.ProductRequest;
import com.pickcloud.backend.dto.ProductResponse;
import com.pickcloud.backend.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> crearProducto(
            @RequestBody ProductRequest request) {

        ProductResponse producto = productService.crearProducto(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(producto);
    }

    @GetMapping("/{idProducto}")
    public ResponseEntity<ProductResponse> obtenerProducto(
            @PathVariable Integer idProducto) {

        return ResponseEntity.ok(
                productService.obtenerProducto(idProducto)
        );
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> obtenerProductosPorNegocio(
            @RequestParam Integer idNegocio) {

        return ResponseEntity.ok(
                productService.obtenerProductosPorNegocio(idNegocio)
        );
    }
}