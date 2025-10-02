package com.vaadin.starter.bakery.ui.views.storefront;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.starter.bakery.app.security.CurrentUser;
import com.vaadin.starter.bakery.backend.data.entity.Order;
import com.vaadin.starter.bakery.backend.service.OrderService;
import com.vaadin.starter.bakery.ui.crud.EntityPresenter;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersGridDataProvider;
import com.vaadin.starter.bakery.ui.dataproviders.OrdersGridDataProvider.OrderFilter;
import com.vaadin.starter.bakery.ui.views.storefront.beans.OrderCardHeader;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.PAGE_STOREFRONT_ORDER_EDIT;

/**
 * Presenter class for handling business logic and user interactions in the Storefront view.
 * Manages order operations including creation, editing, filtering, and navigation.
 */
@SpringComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrderPresenter {

    private OrderCardHeaderGenerator headersGenerator;
    private StorefrontView view;

    private final EntityPresenter<Order, StorefrontView> entityPresenter;
    private final OrdersGridDataProvider dataProvider;
    private final CurrentUser currentUser;
    private final OrderService orderService;

    /**
     * Constructs a new OrderPresenter with the specified dependencies.
     *
     * @param orderService the service for order operations
     * @param dataProvider the data provider for the orders grid
     * @param entityPresenter the entity presenter for CRUD operations
     * @param currentUser the currently authenticated user
     */
    @Autowired
    OrderPresenter(OrderService orderService, OrdersGridDataProvider dataProvider,
                   EntityPresenter<Order, StorefrontView> entityPresenter, CurrentUser currentUser) {
        this.orderService = orderService;
        this.entityPresenter = entityPresenter;
        this.dataProvider = dataProvider;
        this.currentUser = currentUser;
        headersGenerator = new OrderCardHeaderGenerator();
        headersGenerator.resetHeaderChain(false);
        dataProvider.setPageObserver(p -> headersGenerator.ordersRead(p.getContent()));
    }

    /**
     * Initializes the presenter with the storefront view and sets up event listeners.
     *
     * @param view the StorefrontView to manage
     */
    void init(StorefrontView view) {
        this.entityPresenter.setView(view);
        this.view = view;
        view.getGrid().setDataProvider(dataProvider);
        view.getOpenedOrderEditor().setCurrentUser(currentUser.getUser());
        view.getOpenedOrderEditor().addCancelListener(e -> cancel());
        view.getOpenedOrderEditor().addReviewListener(e -> review());
        view.getOpenedOrderDetails().addSaveListenter(e -> save());
        view.getOpenedOrderDetails().addCancelListener(e -> cancel());
        view.getOpenedOrderDetails().addBackListener(e -> back());
        view.getOpenedOrderDetails().addEditListener(e -> edit());
        view.getOpenedOrderDetails().addCommentListener(e -> addComment(e.getMessage()));
    }

    /**
     * Gets the header for a specific order ID.
     *
     * @param id the order ID
     * @return the OrderCardHeader for the order
     */
    OrderCardHeader getHeaderByOrderId(Long id) {
        return headersGenerator.get(id);
    }

    /**
     * Handles filter changes in the storefront view.
     *
     * @param filter the text filter to apply
     * @param showPrevious whether to show previous orders
     */
    public void filterChanged(String filter, boolean showPrevious) {
        headersGenerator.resetHeaderChain(showPrevious);
        dataProvider.setFilter(new OrderFilter(filter, showPrevious));
    }

    /**
     * Handles navigation to a specific order.
     *
     * @param id the order ID to navigate to
     * @param edit true if navigating to edit mode, false for view mode
     */
    void onNavigation(Long id, boolean edit) {
        entityPresenter.loadEntity(id, e -> open(e, edit));
    }

    /**
     * Creates a new order and opens it in edit mode.
     */
    void createNewOrder() {
        open(entityPresenter.createNew(), true);
    }

    /**
     * Cancels the current operation with confirmation if there are unsaved changes.
     */
    void cancel() {
        entityPresenter.cancel(this::close, () -> view.setOpened(true));
    }

    /**
     * Closes the current order dialog without confirmation.
     */
    void closeSilently() {
        entityPresenter.close();
        view.setOpened(false);
    }

    /**
     * Navigates to the order edit view.
     */
    void edit() {
        UI.getCurrent()
                .navigate(String.format(PAGE_STOREFRONT_ORDER_EDIT,
                        entityPresenter.getEntity().getId()));
    }

    /**
     * Navigates back from details view to the main storefront view.
     */
    void back() {
        view.setDialogElementsVisibility(true);
    }

    /**
     * Reviews the current order by validating form fields and switching to read-only mode.
     */
    void review() {
        // Using collect instead of findFirst to assure all streams are
        // traversed, and every validation updates its view
        List<HasValue<?, ?>> fields = view.validate().collect(Collectors.toList());
        if (fields.isEmpty()) {
            if (entityPresenter.writeEntity()) {
                view.setDialogElementsVisibility(false);
                view.getOpenedOrderDetails().display(entityPresenter.getEntity(), true);
            }
        } else if (fields.get(0) instanceof Focusable) {
            ((Focusable<?>) fields.get(0)).focus();
        }
    }

    /**
     * Saves the current order and updates the view accordingly.
     */
    void save() {
        entityPresenter.save(e -> {
            if (entityPresenter.isNew()) {
                view.showCreatedNotification();
                dataProvider.refreshAll();
            } else {
                view.showUpdatedNotification();
                dataProvider.refreshItem(e);
            }
            close();
        });

    }

    /**
     * Adds a comment to the current order.
     *
     * @param comment the comment text to add
     */
    void addComment(String comment) {
        if (entityPresenter.executeUpdate(e -> orderService.addComment(currentUser.getUser(), e, comment))) {
            // You can only add comments when in view mode, so reopening in that state.
            open(entityPresenter.getEntity(), false);
        }
    }

    /**
     * Opens an order in either edit or view mode.
     *
     * @param order the order to open
     * @param edit true for edit mode, false for view mode
     */
    private void open(Order order, boolean edit) {
        view.setDialogElementsVisibility(edit);
        view.setOpened(true);

        if (edit) {
            view.getOpenedOrderEditor().read(order, entityPresenter.isNew());
        } else {
            view.getOpenedOrderDetails().display(order, false);
        }
    }

    /**
     * Closes the current order dialog and navigates to the main view.
     */
    private void close() {
        view.getOpenedOrderEditor().close();
        view.setOpened(false);
        view.navigateToMainView();
        entityPresenter.close();
    }
}