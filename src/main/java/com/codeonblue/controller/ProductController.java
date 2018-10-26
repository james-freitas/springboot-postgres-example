package com.codeonblue.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codeonblue.domain.Product;
import com.codeonblue.repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductRepository productRepository;
	
	public ProductController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Product> findById(@PathVariable Long id){
		Product productFound = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Not found"));
		return ResponseEntity.ok().body(productFound);
	}
	
	@GetMapping("")
	public ResponseEntity<List<Product>> list(){
		List<Product> productList = productRepository.findAll();
		return ResponseEntity.ok().body(productList);		
	}
	
	@PostMapping("")
	public ResponseEntity<Void> insert(@Valid @RequestBody Product product){
		Product productSaved = productRepository.save(product);
		URI uri = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(productSaved.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody Product productRequest, 
									   @PathVariable Long id){

		Product productFound = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Not found"));
		
		productFound.setDescription(productRequest.getDescription());
		productFound.setImageUrl(productRequest.getImageUrl());
		productFound.setPrice(productRequest.getPrice());
		
		productRepository.save(productFound);
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
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){

		Product productFound = productRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Not found"));
		
		productRepository.delete(productFound);
		return ResponseEntity.noContent().build();		
	}
	
}
