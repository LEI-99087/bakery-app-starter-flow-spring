package com.vaadin.starter.bakery.ui.views.admin.users;

import static com.vaadin.flow.i18n.I18NProvider.translate;

import jakarta.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.bakery.app.security.CurrentUser;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.service.UserService;
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.crud.AbstractBakeryCrudView;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

/**
 * View for managing users in the bakery application.
 * Provides CRUD operations for user entities with appropriate security restrictions.
 * Accessible only to users with ADMIN role.
 */
@Route(value = BakeryConst.PAGE_USERS, layout = MainView.class)
@PageTitle(BakeryConst.TITLE_USERS)
@RolesAllowed(Role.ADMIN)
public class UsersView extends AbstractBakeryCrudView<User> {

    /**
     * Constructs a new UsersView with the specified dependencies.
     *
     * @param service the UserService for user operations
     * @param currentUser the currently authenticated user
     * @param passwordEncoder the password encoder for securing user passwords
     */
    @Autowired
    public UsersView(UserService service, CurrentUser currentUser, PasswordEncoder passwordEncoder) {
        super(User.class, service, new Grid<>(), createForm(passwordEncoder), currentUser);
    }

    /**
     * Creates a new User instance.
     *
     * @return a new User instance
     */
    @Override
    protected User createItem() {
        return new User();
    }

    /**
     * Configures the grid columns for displaying users.
     *
     * @param grid the grid to configure
     */
    @Override
    public void setupGrid(Grid<User> grid) {
        grid.addColumn(User::getEmail).setWidth("270px").setHeader(translate("email")).setFlexGrow(5);
        grid.addColumn(u -> u.getFirstName() + " " + u.getLastName()).setHeader(translate("first.name"))
                .setWidth("200px").setFlexGrow(5);
        grid.addColumn(User::getRole).setHeader(translate("role")).setWidth("150px");
    }

    /**
     * Gets the base page path for user navigation.
     *
     * @return the users page path
     */
    @Override
    protected String getBasePage() {
        return BakeryConst.PAGE_USERS;
    }

    /**
     * Creates the form editor for user entities.
     *
     * @param passwordEncoder the password encoder for securing passwords
     * @return a BinderCrudEditor configured for user editing
     */
    private static BinderCrudEditor<User> createForm(PasswordEncoder passwordEncoder) {
        EmailField email = new EmailField(translate("email"));
        email.getElement().setAttribute("colspan", "2");
        TextField first = new TextField(translate("first.name"));
        TextField last = new TextField(translate("last.name"));
        PasswordField password = new PasswordField(translate("password"));
        password.getElement().setAttribute("colspan", "2");
        ComboBox<String> role = new ComboBox<>();
        role.getElement().setAttribute("colspan", "2");
        role.setLabel(translate("role"));

        FormLayout form = new FormLayout(email, first, last, password, role);

        BeanValidationBinder<User> binder = new BeanValidationBinder<>(User.class);

        ListDataProvider<String> roleProvider = DataProvider.ofItems(Role.getAllRoles());
        role.setItemLabelGenerator(s -> s != null ? s : "");
        role.setItems(roleProvider);

        binder.bind(first, "firstName");
        binder.bind(last, "lastName");
        binder.bind(email, "email");
        binder.bind(role, "role");

        binder.forField(password)
                .withValidator(pass -> pass.matches("^(|(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})$"),
                        "need 6 or more chars, mixing digits, lowercase and uppercase letters")
                .bind(user -> password.getEmptyValue(), (user, pass) -> {
                    if (!password.getEmptyValue().equals(pass)) {
                        user.setPasswordHash(passwordEncoder.encode(pass));
                    }
                });

        return new BinderCrudEditor<User>(binder, form);
    }
}