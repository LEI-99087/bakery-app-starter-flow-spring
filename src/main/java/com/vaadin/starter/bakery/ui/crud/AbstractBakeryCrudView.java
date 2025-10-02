package com.vaadin.starter.bakery.ui.crud;

import java.util.function.Consumer;

import com.vaadin.flow.component.crud.Crud;
import com.vaadin.flow.component.crud.CrudEditor;
import com.vaadin.flow.component.crud.CrudI18n;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.starter.bakery.app.security.CurrentUser;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.data.entity.util.EntityUtil;
import com.vaadin.starter.bakery.backend.service.FilterableCrudService;
import com.vaadin.starter.bakery.ui.components.SearchBar;
import com.vaadin.starter.bakery.ui.utils.TemplateUtil;
import com.vaadin.starter.bakery.ui.views.HasNotifications;

/**
 * Abstract base class for CRUD (Create, Read, Update, Delete) views in the Bakery application.
 * Provides common functionality for entity management including filtering, editing, and navigation.
 *
 * @param <E> the type of entity managed by this view, must extend AbstractEntity
 */
public abstract class AbstractBakeryCrudView<E extends AbstractEntity> extends VerticalLayout
        implements HasUrlParameter<Long>, HasNotifications {

    private static final String DISCARD_MESSAGE = "There are unsaved modifications to the %s. Discard changes?";
    private static final String DELETE_MESSAGE = "Are you sure you want to delete the selected %s? This action cannot be undone.";

    private final CrudEntityPresenter<E> entityPresenter;

    private final Crud<E> crud;

    /**
     * Gets the base page path for navigation.
     *
     * @return the base page path as a String
     */
    protected abstract String getBasePage();

    /**
     * Configures the grid columns and properties for the entity type.
     *
     * @param grid the grid to configure
     */
    protected abstract void setupGrid(Grid<E> grid);

    /**
     * Creates a new instance of the entity.
     *
     * @return a new entity instance
     */
    protected abstract E createItem();

    /**
     * Constructs an AbstractBakeryCrudView with the specified dependencies.
     *
     * @param beanType the class type of the entity
     * @param service the service for entity operations
     * @param grid the grid component for displaying entities
     * @param editor the editor component for creating/editing entities
     * @param currentUser the currently authenticated user
     */
    public AbstractBakeryCrudView(Class<E> beanType, FilterableCrudService<E> service,
                                  Grid<E> grid, CrudEditor<E> editor, CurrentUser currentUser) {
        setHeightFull();
        setPadding(false);
        setSpacing(false);

        crud = new Crud<>(beanType, grid, editor);

        grid.setSelectionMode(Grid.SelectionMode.NONE);

        CrudI18n crudI18n = CrudI18n.createDefault();
        String entityName = EntityUtil.getName(beanType);
        crudI18n.setEditItem("Edit " + entityName);
        crudI18n.setEditLabel("Edit " + entityName);
        crudI18n.setCancel("entityName");
        crudI18n.getConfirm().getCancel().setContent(String.format(DISCARD_MESSAGE, entityName));
        crudI18n.getConfirm().getDelete().setContent(String.format(DELETE_MESSAGE, entityName));
        crudI18n.setDeleteItem("Delete");
        crud.setI18n(crudI18n);
        crud.setToolbarVisible(false);
        crud.setHeightFull();

        CrudEntityDataProvider<E> dataProvider = new CrudEntityDataProvider<>(service);
        grid.setDataProvider(dataProvider);
        setupGrid(grid);
        Crud.addEditColumn(grid);

        entityPresenter = new CrudEntityPresenter<>(service, currentUser, this);

        SearchBar searchBar = new SearchBar();
        searchBar.setActionText("New " + entityName);
        searchBar.setPlaceHolder("Search");
        searchBar.addFilterChangeListener(e -> dataProvider.setFilter(searchBar.getFilter()));
        searchBar.getActionButton().getElement().setAttribute("new-button", true);
        searchBar.addActionClickListener(e -> {
            crud.edit(createItem(), Crud.EditMode.NEW_ITEM);
        });

        setupCrudEventListeners(entityPresenter);

        add(searchBar, crud);
    }

    /**
     * Sets up event listeners for CRUD operations.
     *
     * @param entityPresenter the presenter handling entity operations
     */
    private void setupCrudEventListeners(CrudEntityPresenter<E> entityPresenter) {
        Consumer<E> onSuccess = entity -> navigateToEntity(null);
        Consumer<E> onFail = entity -> {
            throw new RuntimeException("The operation could not be performed.");
        };

        crud.addEditListener(e ->
                entityPresenter.loadEntity(e.getItem().getId(),
                        entity -> navigateToEntity(entity.getId().toString())));

        crud.addCancelListener(e -> navigateToEntity(null));

        crud.addSaveListener(e ->
                entityPresenter.save(e.getItem(), onSuccess, onFail));

        crud.addDeleteListener(e ->
                entityPresenter.delete(e.getItem(), onSuccess, onFail));
    }

    /**
     * Navigates to the entity view with the specified ID.
     * If id is null, navigates to the base page without entity selection.
     *
     * @param id the entity ID to navigate to, or null for base page
     */
    protected void navigateToEntity(String id) {
        getUI().ifPresent(ui -> ui.navigate(TemplateUtil.generateLocation(getBasePage(), id)));
    }

    /**
     * Handles URL parameters for entity selection and editing.
     * If an ID is provided, loads and edits the corresponding entity.
     * If no ID is provided, closes any open editor.
     *
     * @param event the before navigation event
     * @param id the optional entity ID from the URL parameter
     */
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long id) {
        if (id != null) {
            E item = crud.getEditor().getItem();
            if (item != null && id.equals(item.getId())) {
                return;
            }
            entityPresenter.loadEntity(id, entity -> crud.edit(entity, Crud.EditMode.EXISTING_ITEM));
        } else {
            crud.setOpened(false);
        }
    }
}