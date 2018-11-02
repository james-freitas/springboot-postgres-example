package com.codeonblue.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeonblue.domain.Product;
import com.codeonblue.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductResource {

	private final ProductService productService;	

	
	public ProductResource(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Product> findById(@PathVariable Long id){
		Product productFound = productService.getById(id);
		return ResponseEntity.ok().body(productFound);
	}
	
	@GetMapping("")
	public ResponseEntity<List<Product>> list(){
		List<Product> productList = productService.listAll(); 
		return ResponseEntity.ok().body(productList);		
	}
	
	@PostMapping("")
	public ResponseEntity<Product> insert(@Valid @RequestBody Product product){
		Product productSaved = productService.save(product);
		return new ResponseEntity<Product>(productSaved, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody Product productFrom, 
									   @PathVariable Long id){

		Product productTo = productService.getById(id);
		
		populateProductToChange(productFrom, productTo);
		
		productService.save(productTo);
		return ResponseEntity.noContent().build();
		
	}

	private void populateProductToChange(Product productFrom, Product productTo) {
		productTo.setDescription(productFrom.getDescription());
		productTo.setImageUrl(productFrom.getImageUrl());
		productTo.setPrice(productFrom.getPrice());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		productService.delete(id);
		return ResponseEntity.noContent().build();		
	}

	
    // Bug on Eclipse in the map (https://bugs.eclipse.org/bugs/show_bug.cgi?id=512486)
//	@PutMapping("/{id}")
//    public Product update(@PathVariable Long id,
//                                   @Valid @RequestBody Product productRequest) {
//        return productRepository.findById(id)
//                .map(product -> {
//                    product.setDescription(productRequest.getDescription());
//                    product.setDescription(productRequest.getDescription());
//                    return productRepository.save(question);
//                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
//    }
}
