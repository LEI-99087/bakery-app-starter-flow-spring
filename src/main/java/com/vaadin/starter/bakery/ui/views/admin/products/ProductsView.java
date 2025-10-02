package com.vaadin.starter.bakery.ui.views.admin.products;

import java.util.Currency;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.crud.BinderCrudEditor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import static com.vaadin.flow.i18n.I18NProvider.translate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.bakery.app.security.CurrentUser;
import com.vaadin.starter.bakery.backend.data.Role;
import com.vaadin.starter.bakery.backend.data.entity.Product;
import com.vaadin.starter.bakery.backend.service.ProductService;
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.crud.AbstractBakeryCrudView;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.utils.converters.CurrencyFormatter;

import jakarta.annotation.security.RolesAllowed;

/**
 * View for managing products in the bakery application.
 * Provides CRUD operations for product entities with appropriate security restrictions.
 * Accessible only to users with ADMIN role.
 */
@Route(value = BakeryConst.PAGE_PRODUCTS, layout = MainView.class)
@PageTitle(BakeryConst.TITLE_PRODUCTS)
@RolesAllowed(Role.ADMIN)
public class ProductsView extends AbstractBakeryCrudView<Product> {

    private CurrencyFormatter currencyFormatter = new CurrencyFormatter();

    /**
     * Constructs a new ProductsView with the specified dependencies.
     *
     * @param service the ProductService for product operations
     * @param currentUser the currently authenticated user
     */
    @Autowired
    public ProductsView(ProductService service, CurrentUser currentUser) {
        super(Product.class, service, new Grid<>(), createForm(), currentUser);
    }

    /**
     * Creates a new Product instance.
     *
     * @return a new Product instance
     */
    @Override
    protected Product createItem() {
        return new Product();
    }

    /**
     * Configures the grid columns for displaying products.
     *
     * @param grid the grid to configure
     */
    @Override
    protected void setupGrid(Grid<Product> grid) {
        grid.addColumn(Product::getName).setHeader(translate("product.name")).setFlexGrow(10);
        grid.addColumn(p -> currencyFormatter.encode(p.getPrice())).setHeader(translate("unit.price"));
    }

    /**
     * Gets the base page path for product navigation.
     *
     * @return the products page path
     */
    @Override
    protected String getBasePage() {
        return BakeryConst.PAGE_PRODUCTS;
    }

    /**
     * Creates the form editor for product entities.
     *
     * @return a BinderCrudEditor configured for product editing
     */
    private static BinderCrudEditor<Product> createForm() {
        TextField name = new TextField(translate("product.name"));
        name.getElement().setAttribute("colspan", "2");
        TextField price = new TextField(translate("unit.price"));
        price.getElement().setAttribute("colspan", "2");

        FormLayout form = new FormLayout(name, price);

        BeanValidationBinder<Product> binder = new BeanValidationBinder<>(Product.class);

        binder.bind(name, "name");

        binder.forField(price).withConverter(new PriceConverter()).bind("price");
        price.setPattern("\\d+(\\.\\d?\\d?)?$");

        String currencySymbol = Currency.getInstance(BakeryConst.APP_LOCALE).getSymbol();
        price.setPrefixComponent(new Span(currencySymbol));

        return new BinderCrudEditor<>(binder, form);
    }

}