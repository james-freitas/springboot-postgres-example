package com.codeonblue.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.codeonblue.service.ProductServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductResource.class)
public class ProductResourceTest {
	
	private final Product product = createProduct();
	private final Product productToChange = createChangedProduct();
	private final Optional<Product> optionalProduct = Optional.of(product);
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductServiceImpl productServiceMock;
	
    @MockBean
    private ProductRepository productRepositoryMock;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void testShouldGetAllCategories() throws Exception {
		List<Product> productList = new ArrayList<Product>();
		productList.add(product);
						
		given(productServiceMock.listAll()).willReturn(productList);
		
		mockMvc.perform(get("/products")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(productList.get(0).getId().intValue())))
				.andExpect(jsonPath("$[0].description", is(productList.get(0).getDescription())))
				.andExpect(jsonPath("$[0].imageUrl", is(productList.get(0).getImageUrl())))
				.andExpect(jsonPath("$[0].price", is(productList.get(0).getPrice().doubleValue())));
				
		verify(productServiceMock, times(1)).listAll();
		verifyNoMoreInteractions(productServiceMock);			
	}
	
	@Test
    public void testShouldGetProductByIdSuccess() throws Exception {
        given(productServiceMock.getById(1L)).willReturn(product);
        Product productFound = optionalProduct.get();

        mockMvc.perform(get("/products/{id}", product.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(productFound.getId().intValue())))
				.andExpect(jsonPath("$.description", is(productFound.getDescription())))
				.andExpect(jsonPath("$.imageUrl", is(productFound.getImageUrl())))
				.andExpect(jsonPath("$.price", is(productFound.getPrice().doubleValue())));

        verify(productServiceMock, times(1)).getById(product.getId());
        verifyNoMoreInteractions(productServiceMock);
    }
    
    @Test
    public void testShouldDeleteProductByIdSuccess() throws Exception {
        Long idToDelete = product.getId();
        doNothing().when(productServiceMock).delete(idToDelete);

        mockMvc.perform(delete("/products/{id}", idToDelete))
                .andExpect(status().isNoContent());

        verify(productServiceMock, times(1)).delete(idToDelete);
        verifyNoMoreInteractions(productServiceMock);
    }
	
	private Product createProduct() {	
		Product product = new Product();
		product.setId(1L);
		product.setDescription("Product description");
		product.setImageUrl("Product image url");
		product.setPrice(new BigDecimal("10.0"));
		return product;
	}
	
    private Product createChangedProduct() {
		Product product = new Product();
		product.setId(1L);
		product.setDescription("Product description - changed");
		product.setImageUrl("Product image url - changed");
		product.setPrice(new BigDecimal("15.90"));
		return product;
	}
	
    @Test
    public void testShouldUpdateProductSuccess() throws Exception {
        
        when(productServiceMock.getById(product.getId())).thenReturn(product);
        when(productServiceMock.save(productToChange)).thenReturn(productToChange);

        mockMvc.perform(
                put("/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        				.content(objectMapper.writeValueAsString(productToChange)))
                        .andExpect(status().isNoContent());

        verify(productServiceMock, times(1)).getById(productToChange.getId());
    }
    
    @Test
    public void testShouldCreateProductSuccess() throws Exception {
		Product productToInsert = new Product();
		productToInsert.setDescription("Product description");
		productToInsert.setImageUrl("Product image url");
		productToInsert.setPrice(new BigDecimal("10.0"));
		
		Product productInserted = new Product();
		productInserted.setId(1L);
		productInserted.setDescription("Product description");
		productInserted.setImageUrl("Product image url");
		productInserted.setPrice(new BigDecimal("10.0"));
    	    	
        when(productServiceMock.save(productToInsert)).thenReturn(productInserted);

        mockMvc.perform(post("/products")
                        .contentType(TestUtil.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsBytes(productToInsert))
        )
                .andExpect(status().isCreated());
    }
}
