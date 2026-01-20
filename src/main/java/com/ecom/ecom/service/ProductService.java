package com.ecom.ecom.service;

import com.ecom.ecom.model.Product;
import com.ecom.ecom.model.Role;
import com.ecom.ecom.model.User;
import com.ecom.ecom.repository.ProductRepository;
import com.ecom.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }


    public Product createProduct(Product product, String adminEmail) {
        validateAdmin(adminEmail);
        if(productRepository.existsByName(product.getName())){
            throw new RuntimeException("Product name already exists");
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Integer id, Product updated, String adminEmail) {
        validateAdmin(adminEmail);
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (productRepository.existsByName(product.getName())) {
            throw new RuntimeException("Product name already exists");
        }
        product.setName(updated.getName());
        product.setDescription(updated.getDescription());
        product.setPrice(updated.getPrice());
        product.setImageUrl(updated.getImageUrl());
        product.setStock(updated.getStock());

        return productRepository.save(product);
    }

    // ADMIN
    public void deleteProduct(Integer id, String adminEmail) {
        validateAdmin(adminEmail);
        productRepository.deleteById(id);
    }

    private void validateAdmin(String email) {
        User admin = userRepository.findByEmail(email);

        if (admin == null || admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("Admin access required");
        }
    }
}

