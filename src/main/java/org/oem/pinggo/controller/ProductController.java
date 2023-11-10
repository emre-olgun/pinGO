package org.oem.pinggo.controller;


import org.oem.pinggo.dto.ProductCreateRequest;
import org.oem.pinggo.dto.ProductResponse;
import org.oem.pinggo.dto.ProductUpdateRequest;
import org.oem.pinggo.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController

@RequestMapping("/api/products")
public class ProductController {

    //@Autowired
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return productService.findAllProducts();
    }


    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getOneProduct(@PathVariable Long productId) {
        return productService.getOneProduct(productId);
    }


    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('SELLER')&&@productService.currentSellerisOwnerofProduct(#productId)")
    public ProductResponse updateOneProduct(@PathVariable Long productId, @RequestBody ProductUpdateRequest productUpdateRequest) {
        return productService.updateOneProduct(productId, productUpdateRequest);
    }

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateRequest productCreateRequest) {
        return productService.createProduct(productCreateRequest);

    }

    @DeleteMapping("/{productId}")
    @PreAuthorize(" @productService.currentSellerisOwnerofProduct(#productId)")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        return productService.deleteById(productId);
    }


    @GetMapping("/listbyQuery/{qText}/")
    public ResponseEntity<List<ProductResponse>> getlistbyQuery(@PathVariable String qText) {
        System.out.print("getlistbyNameProducts");

        return productService.findWithQueryText(qText);
    }


    @GetMapping("/listbyQuantity/{quantity}")
    public ResponseEntity<List<ProductResponse>> getlistbyQuantity(@PathVariable int quantity) {
        return productService.getlistbyQuantity(quantity);
    }

}
