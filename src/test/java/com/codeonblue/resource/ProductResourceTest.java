package com.codeonblue.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductResource.class)
public class ProductResourceTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductRepository productRepositoryMock;
	
	@Test
	public void testShouldGetAllCategories() throws Exception {
		Product product = createProduct();
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

	private Product createProduct() {
		Random random = new Random();
		int id = random.nextInt(999);
		
		Product product = new Product();
		product.setId(new Long(id));
		product.setDescription("Product description");
		product.setImageUrl("Product image url");
		product.setPrice(new BigDecimal("10.0"));
		return product;
	}
	
}
