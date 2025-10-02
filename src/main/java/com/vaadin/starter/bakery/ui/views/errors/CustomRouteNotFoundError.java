package com.vaadin.starter.bakery.ui.views.errors;

import jakarta.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RouteNotFoundError;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.starter.bakery.ui.MainView;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

/**
 * Custom 404 error page that extends the default RouteNotFoundError.
 * Provides a user-friendly error message with a link to the front page.
 */
@ParentLayout(MainView.class)
@PageTitle(BakeryConst.TITLE_NOT_FOUND)
public class CustomRouteNotFoundError extends RouteNotFoundError {

    /**
     * Constructs a new CustomRouteNotFoundError with a custom error message and navigation link.
     */
    public CustomRouteNotFoundError() {
        RouterLink link = Component.from(
                ElementFactory.createRouterLink("", "Go to the front page."),
                RouterLink.class);
        getElement().appendChild(new Text("Oops you hit a 404. ").getElement(), link.getElement());
    }

    /**
     * Sets the error parameter and returns the appropriate HTTP status code.
     *
     * @param event the before navigation event
     * @param parameter the error parameter containing the not found exception
     * @return HTTP 404 status code
     */
    @Override
    public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
        return HttpServletResponse.SC_NOT_FOUND;
    }
}