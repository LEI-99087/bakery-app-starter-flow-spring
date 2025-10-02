package com.vaadin.starter.bakery.ui.views.storefront;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import jakarta.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.data.entity.util.EntityUtil;
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.components.SearchBar;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;
import com.vaadin.starter.bakery.ui.views.EntityView;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderDetails;
import com.vaadin.starter.bakery.ui.views.orderedit.OrderEditor;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.EDIT_SEGMENT;
import static com.vaadin.starter.bakery.ui.utils.BakeryConst.ORDER_ID;

/**
 * Storefront view for managing orders in the bakery application.
 * Provides a grid view of orders with search, filtering, and order management capabilities.
 * Supports both view and edit modes for orders.
 */
@Tag("storefront-view")
@JsModule("./src/views/storefront/storefront-view.js")
@Route(value = BakeryConst.PAGE_STOREFRONT_ORDER_TEMPLATE, layout = MainView.class)
@RouteAlias(value = BakeryConst.PAGE_STOREFRONT_ORDER_EDIT_TEMPLATE, layout = MainView.class)
@RouteAlias(value = BakeryConst.PAGE_ROOT, layout = MainView.class)
@PageTitle(BakeryConst.TITLE_STOREFRONT)
@PermitAll
public class StorefrontView extends LitTemplate
        implements HasLogger, BeforeEnterObserver, EntityView<Order> {

    @Id("search")
    private SearchBar searchBar;

    @Id("grid")
    private Grid<Order> grid;

    @Id("dialog")
    private Dialog dialog;

    private ConfirmDialog confirmation;

    private final OrderEditor orderEditor;

    private final OrderDetails orderDetails = new OrderDetails();

    private final OrderPresenter presenter;

    /**
     * Constructs a new StorefrontView with the specified dependencies.
     *
     * @param presenter the OrderPresenter for handling business logic
     * @param orderEditor the OrderEditor for editing orders
     */
    @Autowired
    public StorefrontView(OrderPresenter presenter, OrderEditor orderEditor) {
        this.presenter = presenter;
        this.orderEditor = orderEditor;

        searchBar.setActionText("New order");
        searchBar.setCheckboxText("Show past orders");
        searchBar.setPlaceHolder("Search");

        grid.setSelectionMode(Grid.SelectionMode.NONE);

        grid.addColumn(OrderCard.getTemplate()
                .withProperty("orderCard", OrderCard::create)
                .withProperty("header", order -> presenter.getHeaderByOrderId(order.getId()))
                .withFunction("cardClick",
                        order -> UI.getCurrent().navigate(BakeryConst.PAGE_STOREFRONT + "/" + order.getId())));

        getSearchBar().addFilterChangeListener(
                e -> presenter.filterChanged(getSearchBar().getFilter(), getSearchBar().isCheckboxChecked()));
        getSearchBar().addActionClickListener(e -> presenter.createNewOrder());

        presenter.init(this);

        dialog.addDialogCloseActionListener(e -> presenter.cancel());
    }

    /**
     * Gets the confirmation dialog used for user confirmations.
     *
     * @return the ConfirmDialog instance
     */
    @Override
    public ConfirmDialog getConfirmDialog() {
        return confirmation;
    }

    /**
     * Sets the confirmation dialog for user confirmations.
     *
     * @param confirmDialog the ConfirmDialog to set
     */
    @Override
    public void setConfirmDialog(ConfirmDialog confirmDialog) {
        this.confirmation = confirmDialog;
    }

    /**
     * Sets the opened state of the order dialog.
     *
     * @param opened true to open the dialog, false to close it
     */
    void setOpened(boolean opened) {
        dialog.setOpened(opened);
    }

    /**
     * Handles before navigation events to load orders based on URL parameters.
     *
     * @param event the before navigation event
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> orderId = event.getRouteParameters().getLong(ORDER_ID);
        if (orderId.isPresent()) {
            boolean isEditView = EDIT_SEGMENT.equals(getLastSegment(event));
            presenter.onNavigation(orderId.get(), isEditView);
        } else if (dialog.isOpened()) {
            presenter.closeSilently();
        }
    }

    /**
     * Navigates to the main storefront view.
     */
    void navigateToMainView() {
        getUI().ifPresent(ui -> ui.navigate(BakeryConst.PAGE_STOREFRONT));
    }

    /**
     * Checks if there are unsaved changes in the current order.
     *
     * @return true if there are unsaved changes, false otherwise
     */
    @Override
    public boolean isDirty() {
        return orderEditor.hasChanges() || orderDetails.isDirty();
    }

    /**
     * Writes form data to the order entity.
     *
     * @param entity the order entity to write data to
     * @throws ValidationException if validation fails during write operation
     */
    @Override
    public void write(Order entity) throws ValidationException {
        orderEditor.write(entity);
    }

    /**
     * Validates all form fields and returns a stream of fields with validation errors.
     *
     * @return a stream of fields that have validation errors
     */
    public Stream<HasValue<?, ?>> validate() {
        return orderEditor.validate();
    }

    /**
     * Gets the search bar component.
     *
     * @return the SearchBar instance
     */
    SearchBar getSearchBar() {
        return searchBar;
    }

    /**
     * Gets the order editor component.
     *
     * @return the OrderEditor instance
     */
    OrderEditor getOpenedOrderEditor() {
        return orderEditor;
    }

    /**
     * Gets the order details component.
     *
     * @return the OrderDetails instance
     */
    OrderDetails getOpenedOrderDetails() {
        return orderDetails;
    }

    /**
     * Gets the orders grid component.
     *
     * @return the Grid instance
     */
    Grid<Order> getGrid() {
        return grid;
    }

    /**
     * Clears the form and resets the dirty state.
     */
    @Override
    public void clear() {
        orderDetails.setDirty(false);
        orderEditor.clear();
    }

    /**
     * Sets the visibility of dialog elements based on editing mode.
     *
     * @param editing true to show editor, false to show details view
     */
    void setDialogElementsVisibility(boolean editing) {
        dialog.add(editing ? orderEditor : orderDetails);
        orderEditor.setVisible(editing);
        orderDetails.setVisible(!editing);
    }

    /**
     * Gets the entity name for display purposes.
     *
     * @return the entity name as a string
     */
    @Override
    public String getEntityName() {
        return EntityUtil.getName(Order.class);
    }

    /**
     * Gets the last segment of the current URL path.
     *
     * @param event the before navigation event
     * @return the last segment of the URL path
     */
    private String getLastSegment(BeforeEnterEvent event) {
        List<String> segments = event.getLocation().getSegments();
        return segments.get(segments.size() - 1);
    }
}