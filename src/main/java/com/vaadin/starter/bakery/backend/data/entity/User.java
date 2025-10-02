package com.vaadin.starter.bakery.backend.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;

/**
 * Represents a user entity in the bakery system.
 * <p>
 * Stores user information including authentication details, personal information,
 * role-based permissions, and account status.
 * </p>
 */
@Entity(name="UserInfo")
public class User extends AbstractEntity {

    @NotEmpty
    @Email
    @Size(max = 255)
    @Column(unique = true)
    private String email;

    @NotNull
    @Size(min = 4, max = 255)
    private String passwordHash;

    @NotBlank
    @Size(max = 255)
    private String firstName;

    @NotBlank
    @Size(max = 255)
    private String lastName;

    @NotBlank
    @Size(max = 255)
    private String role;

    private boolean locked = false;

    /**
     * Prepares data before persisting or updating by converting email to lowercase.
     * This ensures case-insensitive email handling for authentication.
     */
    @PrePersist
    @PreUpdate
    private void prepareData(){
        this.email = email == null ? null : email.toLowerCase();
    }

    /**
     * An empty constructor is needed for all beans.
     */
    public User() {
        // An empty constructor is needed for all beans
    }

    /**
     * Gets the hashed password for this user.
     *
     * @return the password hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets the hashed password for this user.
     *
     * @param passwordHash the password hash to set
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Gets the first name of the user.
     *
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the role of the user.
     *
     * @return the user's role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the user's email address (in lowercase)
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     * The email will be converted to lowercase during persistence.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Checks if the user account is locked.
     *
     * @return true if the account is locked, false otherwise
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Sets the locked status of the user account.
     *
     * @param locked true to lock the account, false to unlock
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Compares this user to another object for equality.
     * Two users are considered equal if they have the same ID, version,
     * email, first name, last name, role, and locked status.
     *
     * @param o the reference object with which to compare
     * @return true if this user is the same as the object argument, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        User that = (User) o;
        return locked == that.locked &&
                Objects.equals(email, that.email) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(role, that.role);
    }

    /**
     * Calculates the hash code based on the superclass hash code, email,
     * first name, last name, role, and locked status.
     *
     * @return a hash code value for this user
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, firstName, lastName, role, locked);
    }
}