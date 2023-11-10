package org.oem.pinggo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oem.pinggo.config.Translator;
import org.oem.pinggo.dto.ProductCreateRequest;
import org.oem.pinggo.dto.ProductResponse;
import org.oem.pinggo.dto.ProductUpdateRequest;
import org.oem.pinggo.entity.Product;
import org.oem.pinggo.entity.Seller;
import org.oem.pinggo.exception.ItemOwnerException;
import org.oem.pinggo.exception.NoSuchElementFoundException;
import org.oem.pinggo.repository.ProductRepository;
import org.oem.pinggo.repository.SellerRepository;
import org.oem.pinggo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final SellerRepository sellerRepository;


    private final Translator translator;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public ResponseEntity<List<ProductResponse>> findAllProducts() {

        List<ProductResponse> productResponseList = productRepository.findAll().stream().map(p -> new ProductResponse(p)).toList();
        return ResponseEntity.ok(productResponseList);


    }

    public Product getRef(Long productId){
        Product product = productRepository.findById(productId).orElseThrow(() -> {
            String errorMessage = translator.toLocale("product.not.found.with.id.exception",productId);
            
log.error("Product is not found : {}",errorMessage);
            return new NoSuchElementFoundException(errorMessage);
        });
        return product;

    }


    public void decreaseQuantity(Product product, int decreaseValue){
        product.setQuantity(product.getQuantity()-decreaseValue);
        productRepository.save(product);
    }

    public void increaseQuantityforReturn(Product product, int increaseValue){
        product.setQuantity(product.getQuantity()+increaseValue);
        productRepository.save(product);
    }

    public ResponseEntity<ProductResponse> getOneProduct(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> {
            String errorMessage = translator.toLocale("product.not.found.with.id.exception",productId);
            log.error("Product is not found : {}",errorMessage);

            return new NoSuchElementFoundException(errorMessage);
        });
        return ResponseEntity.ok(new ProductResponse(product));

    }


    public ProductResponse updateOneProduct(Long productId, ProductUpdateRequest productUpdateRequest) {
        System.out.println("testable");


        Product p = productRepository.getReferenceById(productId);
        if (productUpdateRequest.getDescription() != null && !productUpdateRequest.getDescription().isEmpty() ) {
            p.setDescription(productUpdateRequest.getDescription());
        }
        if (productUpdateRequest.getName() != null && !productUpdateRequest.getName().isEmpty() ) {
            p.setName(productUpdateRequest.getName());
        }

        if (productUpdateRequest.getQuantity() != null ) {
            p.setQuantity(productUpdateRequest.getQuantity());
        }
            return new ProductResponse(productRepository.save(p));

    }


    public ResponseEntity<?> deleteById(Long productId) {


        productRepository.deleteById(productId);
        return ResponseEntity.ok("Deleled");


    }




    public boolean currentSellerisOwnerofProduct(Long productId) {
        System.out.printf("currentSellerisOwnerofProduct");
        Long currentSellerID = userService.getCurrentUserId();
        if (!productRepository.existsProductSellerwithIDs(productId, currentSellerID)) {


            String errorMessage = translator.toLocale("with.id.product.is.from.different.seller",productId);
           
log.error("{} seller is trying to edit {} : {}",currentSellerID,productId,errorMessage);

            throw  new ItemOwnerException(errorMessage);

        }

        return true;
    }

    public ResponseEntity<?> createProduct(ProductCreateRequest productCreateRequest) {
        Product p = new Product();

        Seller currentUser = userService.getCurrentSeller();
                p.setSeller(currentUser);
        p.setName(productCreateRequest.getName());
        p.setDescription(productCreateRequest.getDescription());
        p.setQuantity(productCreateRequest.getQuantity());
        return ResponseEntity.ok(new ProductResponse(productRepository.save(p)));

    }

    ////##############################################
    public ResponseEntity<List<ProductResponse>> findWithQueryText(String qText) {

        List<ProductResponse> productResponseList = productRepository.findWithQueryText(qText).stream().map(ProductResponse::new).toList();

        return ResponseEntity.ok(productResponseList);
    }

    public ResponseEntity<List<ProductResponse>> getlistbyQuantity(int quantity) {
        return ResponseEntity.ok(productRepository.findByQuantityGreaterThanEqual(quantity).stream().map(ProductResponse::new).toList());
    }
}
