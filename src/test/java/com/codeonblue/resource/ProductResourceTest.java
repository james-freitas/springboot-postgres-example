package com.codeonblue.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.codeonblue.domain.Product;
import com.codeonblue.repository.ProductRepository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductResource.class)
public class ProductResourceTest {
	
	private final Product product = createProduct();	
	private final Optional<Product> optionalProduct = Optional.of(product);
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductRepository productRepositoryMock;
	
	@Test
	public void testShouldGetAllCategories() throws Exception {
		List<Product> productList = new ArrayList<Product>();
		productList.add(product);
						
		given(productRepositoryMock.findAll()).willReturn(productList);
		
		mockMvc.perform(get("/products")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(productList.get(0).getId().intValue())))
				.andExpect(jsonPath("$[0].description", is(productList.get(0).getDescription())))
				.andExpect(jsonPath("$[0].imageUrl", is(productList.get(0).getImageUrl())))
				.andExpect(jsonPath("$[0].price", is(productList.get(0).getPrice().doubleValue())));
				
		verify(productRepositoryMock, times(1)).findAll();
		verifyNoMoreInteractions(productRepositoryMock);			
	}
	
    @Test
    public void testShouldGetProductByIdSuccess() throws Exception {

        given(productRepositoryMock.findById(1L)).willReturn(optionalProduct);
        Product productFound = optionalProduct.get();

        mockMvc.perform(get("/products/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(productFound.getId().intValue())))
				.andExpect(jsonPath("$.description", is(productFound.getDescription())))
				.andExpect(jsonPath("$.imageUrl", is(productFound.getImageUrl())))
				.andExpect(jsonPath("$.price", is(productFound.getPrice().doubleValue())));

        verify(productRepositoryMock, times(1)).findById(product.getId());
        verifyNoMoreInteractions(productRepositoryMock);
    }
    
    @Test
    public void testShouldDeleteProductByIdSuccess() throws Exception {
        Long idToDelete = product.getId();
        doNothing().when(productRepositoryMock).delete(product);
        given(productRepositoryMock.findById(idToDelete)).willReturn(optionalProduct);

        mockMvc.perform(delete("/products/{id}", idToDelete))
                .andExpect(status().isNoContent());

        verify(productRepositoryMock, times(1)).findById(idToDelete);
        verify(productRepositoryMock, times(1)).delete(product);
        verifyNoMoreInteractions(productRepositoryMock);
    }
	
	private Product createProduct() {	
		Product product = new Product();
		product.setId(1L);
		product.setDescription("Product description");
		product.setImageUrl("Product image url");
		product.setPrice(new BigDecimal("10.0"));
		return product;
	}
	
}
