package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Product;
import org.example.exception.BusinessException;
import org.example.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
	private final ProductRepository productRepository;

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Product getProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new BusinessException("Product not found with id: " + id));
	}

	@Transactional
	public Product createProduct(Product product) {
		return productRepository.save(product);
	}

	@Transactional
	public Product updateProduct(Long id, Product product) {
		Product existingProduct = getProductById(id);

		// Update the existing product with new values
		existingProduct.setName(product.getName());
		existingProduct.setDescription(product.getDescription());
		existingProduct.setPrice(product.getPrice());
		existingProduct.setQuantityInStock(product.getQuantityInStock());

		return productRepository.save(existingProduct);
	}

	@Transactional
	public void deleteProduct(Long id) {
		if (!productRepository.existsById(id)) {
			throw new BusinessException("Product not found with id: " + id);
		}
		productRepository.deleteById(id);
	}
}