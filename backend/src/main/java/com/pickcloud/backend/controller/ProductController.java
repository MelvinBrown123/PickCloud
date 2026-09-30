package com.pickcloud.backend.controller;

import com.pickcloud.backend.dto.ProductUpdateRequest;
import com.pickcloud.backend.dto.StockRequest;
import com.pickcloud.backend.dto.ProductRequest;
import com.pickcloud.backend.dto.ProductResponse;
import com.pickcloud.backend.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for product-related operations.
 * The controller receives HTTP requests, delegates business logic to
 * {@link ProductService}, and returns DTOs instead of exposing JPA entities
 * directly through the API.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // Constructor injection lets Spring provide the ProductService dependency.
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /** Creates a product from the JSON body received by the API. */
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest request) {

        ProductResponse producto = productService.createProduct(request);

        // HTTP 201 indicates that a new resource was successfully created.
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(producto);
    }

    /** Retrieves a single product using the identifier included in the URL. */
    @GetMapping("/{idProducto}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable Integer idProducto) {

        return ResponseEntity.ok(productService.getProduct(idProducto));
    }

    /** Retrieves all products associated with the business sent as a query parameter. */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProductsByBusiness(
            @RequestParam Integer idNegocio) {

        return ResponseEntity.ok(productService.getProductsByBusiness(idNegocio));
    }

    /**
     * Updates an existing product.
     */
    @PutMapping("/{idProducto}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Integer idProducto,
            @RequestBody ProductUpdateRequest request) {

        return ResponseEntity.ok(
                productService.updateProduct(idProducto, request)
        );
    }

    /**
     * Logically deactivates a product without removing it from the database.
     */
    @PatchMapping("/{idProducto}/deactivate")
    public ResponseEntity<ProductResponse> deactivateProduct(
            @PathVariable Integer idProducto) {

        return ResponseEntity.ok(
                productService.deactivateProduct(idProducto)
        );
    }

    /**
     * Adds units to the stock of an existing product.
     */
    @PatchMapping("/{idProducto}/stock/add")
    public ResponseEntity<ProductResponse> addStock(
            @PathVariable Integer idProducto,
            @RequestBody StockRequest request) {

        return ResponseEntity.ok(
                productService.addStock(
                        idProducto,
                        request.getCantidad()
                )
        );
    }

    /**
     * Removes units from the stock of an existing product.
     */
    @PatchMapping("/{idProducto}/stock/remove")
    public ResponseEntity<ProductResponse> removeStock(
            @PathVariable Integer idProducto,
            @RequestBody StockRequest request) {

        return ResponseEntity.ok(
                productService.removeStock(
                        idProducto,
                        request.getCantidad()
                )
        );
    }

}
