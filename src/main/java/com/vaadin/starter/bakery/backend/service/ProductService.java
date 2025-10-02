package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.ProductRepository;

/**
 * Service class for managing Product entities.
 * Provides business logic for product operations including filtering, counting,
 * and handling data integrity constraints.
 */
@Service
public class ProductService implements FilterableCrudService<Product> {

    private final ProductRepository productRepository;

    /**
     * Constructs a ProductService with the specified ProductRepository.
     *
     * @param productRepository the repository used for product data access
     */
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Finds products matching the given filter with pagination support.
     *
     * @param filter an optional filter string to match against product names
     * @param pageable pagination information (page number, size, and sorting)
     * @return a page of matching products
     */
    @Override
    public Page<Product> findAnyMatching(Optional<String> filter, Pageable pageable) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return productRepository.findByNameLikeIgnoreCase(repositoryFilter, pageable);
        } else {
            return find(pageable);
        }
    }

    /**
     * Counts the number of products matching the given filter.
     *
     * @param filter an optional filter string to match against product names
     * @return the count of matching products
     */
    @Override
    public long countAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return productRepository.countByNameLikeIgnoreCase(repositoryFilter);
        } else {
            return count();
        }
    }

    /**
     * Finds all products with pagination support.
     *
     * @param pageable pagination information (page number, size, and sorting)
     * @return a page of all products
     */
    public Page<Product> find(Pageable pageable) {
        return productRepository.findBy(pageable);
    }

    /**
     * Gets the ProductRepository instance.
     *
     * @return the ProductRepository instance
     */
    @Override
    public JpaRepository<Product, Long> getRepository() {
        return productRepository;
    }

    /**
     * Creates a new product instance.
     *
     * @param currentUser the user creating the product
     * @return a new Product instance
     */
    @Override
    public Product createNew(User currentUser) {
        return new Product();
    }

    /**
     * Saves a product entity with user-friendly error handling for duplicate names.
     *
     * @param currentUser the user performing the save operation
     * @param entity the product to be saved
     * @return the saved product
     * @throws UserFriendlyDataException if a product with the same name already exists
     */
    @Override
    public Product save(User currentUser, Product entity) {
        try {
            return FilterableCrudService.super.save(currentUser, entity);
        } catch (DataIntegrityViolationException e) {
            throw new UserFriendlyDataException(
                    "There is already a product with that name. Please select a unique name for the product.");
        }

    }

}