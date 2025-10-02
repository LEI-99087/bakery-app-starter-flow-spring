package com.vaadin.starter.bakery.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.UserRepository;

/**
 * Service class for managing User entities.
 * Provides business logic for user operations including filtering, counting,
 * and enforcing business rules for user modification and deletion.
 */
@Service
public class UserService implements FilterableCrudService<User> {

    /** Error message displayed when attempting to modify a locked user. */
    public static final String MODIFY_LOCKED_USER_NOT_PERMITTED = "User has been locked and cannot be modified or deleted";

    /** Error message displayed when attempting to delete one's own account. */
    private static final String DELETING_SELF_NOT_PERMITTED = "You cannot delete your own account";

    private final UserRepository userRepository;

    /**
     * Constructs a UserService with the specified UserRepository.
     *
     * @param userRepository the repository used for user data access
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds users matching the given filter with pagination support.
     * Searches in email, first name, last name, and role fields.
     *
     * @param filter an optional filter string to match against user fields
     * @param pageable pagination information (page number, size, and sorting)
     * @return a page of matching users
     */
    public Page<User> findAnyMatching(Optional<String> filter, Pageable pageable) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return getRepository()
                    .findByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
                            repositoryFilter, repositoryFilter, repositoryFilter, repositoryFilter, pageable);
        } else {
            return find(pageable);
        }
    }

    /**
     * Counts the number of users matching the given filter.
     * Searches in email, first name, last name, and role fields.
     *
     * @param filter an optional filter string to match against user fields
     * @return the count of matching users
     */
    @Override
    public long countAnyMatching(Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return userRepository.countByEmailLikeIgnoreCaseOrFirstNameLikeIgnoreCaseOrLastNameLikeIgnoreCaseOrRoleLikeIgnoreCase(
                    repositoryFilter, repositoryFilter, repositoryFilter, repositoryFilter);
        } else {
            return count();
        }
    }

    /**
     * Gets the UserRepository instance.
     *
     * @return the UserRepository instance
     */
    @Override
    public UserRepository getRepository() {
        return userRepository;
    }

    /**
     * Finds all users with pagination support.
     *
     * @param pageable pagination information (page number, size, and sorting)
     * @return a page of all users
     */
    public Page<User> find(Pageable pageable) {
        return getRepository().findBy(pageable);
    }

    /**
     * Saves a user entity with business rule validation.
     * Prevents modification of locked users.
     *
     * @param currentUser the user performing the save operation
     * @param entity the user to be saved
     * @return the saved user
     * @throws UserFriendlyDataException if attempting to modify a locked user
     */
    @Override
    public User save(User currentUser, User entity) {
        throwIfUserLocked(entity);
        return getRepository().saveAndFlush(entity);
    }

    /**
     * Deletes a user with business rule validation.
     * Prevents users from deleting their own account and prevents deletion of locked users.
     *
     * @param currentUser the user performing the delete operation
     * @param userToDelete the user to be deleted
     * @throws UserFriendlyDataException if attempting to delete own account or a locked user
     */
    @Override
    @Transactional
    public void delete(User currentUser, User userToDelete) {
        throwIfDeletingSelf(currentUser, userToDelete);
        throwIfUserLocked(userToDelete);
        FilterableCrudService.super.delete(currentUser, userToDelete);
    }

    /**
     * Throws an exception if a user attempts to delete their own account.
     *
     * @param currentUser the currently authenticated user
     * @param user the user to be deleted
     * @throws UserFriendlyDataException if the user attempts to delete their own account
     */
    private void throwIfDeletingSelf(User currentUser, User user) {
        if (currentUser.equals(user)) {
            throw new UserFriendlyDataException(DELETING_SELF_NOT_PERMITTED);
        }
    }

    /**
     * Throws an exception if attempting to modify a locked user.
     *
     * @param entity the user entity to check
     * @throws UserFriendlyDataException if the user is locked
     */
    private void throwIfUserLocked(User entity) {
        if (entity != null && entity.isLocked()) {
            throw new UserFriendlyDataException(MODIFY_LOCKED_USER_NOT_PERMITTED);
        }
    }

    /**
     * Creates a new user instance.
     *
     * @param currentUser the user creating the new user
     * @return a new User instance
     */
    @Override
    public User createNew(User currentUser) {
        return new User();
    }

}