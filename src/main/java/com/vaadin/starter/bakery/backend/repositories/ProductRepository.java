package com.vaadin.starter.bakery.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.starter.bakery.backend.data.entity.Product;

/**
 * Repositório de acesso a dados para a entidade {@link Product}.
 *
 * <p>Esta interface herda de {@link JpaRepository}, fornecendo métodos
 * CRUD básicos e acrescenta consultas personalizadas para
 * a entidade {@code Product}.
 */

public interface ProductRepository extends JpaRepository<Product, Long> {

	/**
     * Obtém uma página de produtos.
     *
     * @param page objeto {@link Pageable} que define página, tamanho e ordenação
     * @return uma {@link Page} contendo produtos de acordo com a paginação
     */

	Page<Product> findBy(Pageable page);

	/**
     * Procura produtos cujo nome corresponda a um padrão, sem diferenciar maiúsculas
     * de minúsculas.
     *
     * @param name padrão do nome a procurar (pode incluir wildcards SQL, ex. "%bolo%")
     * @param page objeto {@link Pageable} que define página, tamanho e ordenação
     * @return uma {@link Page} contendo os produtos encontrados
     */

	Page<Product> findByNameLikeIgnoreCase(String name, Pageable page);

	 /**
     * Conta o número de produtos cujo nome corresponda a um padrão, sem diferenciar
     * maiúsculas de minúsculas.
     *
     * @param name padrão do nome a procurar (pode incluir wildcards SQL, ex. "%pão%")
     * @return número total de produtos que correspondem ao padrão
     */

	int countByNameLikeIgnoreCase(String name);

}
