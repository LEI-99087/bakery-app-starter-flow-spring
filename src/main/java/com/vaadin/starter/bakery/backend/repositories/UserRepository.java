package com.vaadin.starter.bakery.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.vaadin.starter.bakery.backend.data.entity.User;


/**
 * Repositório de acesso a dados para a entidade {@link User}.
 *
 * <p>Esta interface estende {@link JpaRepository}, herdando os métodos
 * CRUD básicos, e define consultas personalizadas relacionadas com
 * a entidade {@code User}.
 */

public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Procura um utilizador pelo seu email, sem diferenciar maiúsculas
	 * de minúsculas.
	 *
	 * @param email email do utilizador a procurar
	 * @return o {@link User} correspondente, ou {@code null} se não existir
	 */

	User findByEmailIgnoreCase(String email);

	/**
	 * Obtém uma página de utilizadores.
	 *
	 * @param pageable objeto {@link Pageable} que define página, tamanho e ordenação
	 * @return uma {@link Page} contendo utilizadores de acordo com a paginação
	 */

	Page<User> findBy(Pageable pageable);

	/**
	 * Procura utilizadores cujo email, primeiro nome, último nome ou papel (role)
	 * correspondam a um padrão, sem diferenciar maiúsculas de minúsculas.
	 *
	 * @param emailLike padrão de email a procurar (ex.: "%@gmail.com%")
	 * @param firstNameLike padrão do primeiro nome (ex.: "%Ana%")
	 * @param lastNameLike padrão do último nome (ex.: "%Silva%")
	 * @param roleLike padrão do papel/função (ex.: "%ADMIN%")
	 * @param pageable objeto {@link Pageable} que define página, tamanho e ordenação
	 * @return uma {@link Page} contendo utilizadores encontrados
	 */

	Page<User> findByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
			String emailLike, String firstNameLike, String lastNameLike, String roleLike, Pageable pageable);

	/**
	 * Conta o número de utilizadores cujo email, primeiro nome, último nome
	 * ou papel (role) correspondam a um padrão, sem diferenciar maiúsculas
	 * de minúsculas.
	 *
	 * @param emailLike padrão de email a procurar
	 * @param firstNameLike padrão do primeiro nome
	 * @param lastNameLike padrão do último nome
	 * @param roleLike padrão do papel/função
	 * @return número total de utilizadores que correspondem ao critério
	 */
	long countByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
			String emailLike, String firstNameLike, String lastNameLike, String roleLike);
}
