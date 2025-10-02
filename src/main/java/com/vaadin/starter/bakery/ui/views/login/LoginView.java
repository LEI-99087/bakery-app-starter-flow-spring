package com.vaadin.starter.bakery.ui.views.login;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import static com.vaadin.flow.i18n.I18NProvider.translate;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.starter.bakery.app.security.SecurityUtils;
import com.vaadin.starter.bakery.ui.views.storefront.StorefrontView;

/**
 * Login view for the Bakery application.
 * Provides a login overlay with internationalization support and automatic redirection
 * for already authenticated users.
 */
@Route
@PageTitle("###Bakery###")
public class LoginView extends LoginOverlay
        implements AfterNavigationObserver, BeforeEnterObserver {

    /**
     * Constructs a new LoginView with configured internationalization and form settings.
     */
    public LoginView() {
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle(translate("app.title"));
        i18n.getHeader().setDescription(
                "admin@vaadin.com + admin\n" + "barista@vaadin.com + barista");
        i18n.setAdditionalInformation(null);
        i18n.setForm(new LoginI18n.Form());
        i18n.getForm().setSubmit(translate("signin"));
        i18n.getForm().setTitle(translate("login"));
        i18n.getForm().setUsername(translate("email"));
        i18n.getForm().setPassword(translate("password"));
        setI18n(i18n);
        setForgotPasswordButtonVisible(false);
        setAction("login");
    }

    /**
     * Handles navigation events to check if user is already logged in.
     * If user is logged in, redirects to StorefrontView, otherwise shows login overlay.
     *
     * @param event the before navigation event
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (SecurityUtils.isUserLoggedIn()) {
            event.forwardTo(StorefrontView.class);
        } else {
            setOpened(true);
        }
    }

    /**
     * Handles after navigation events to check for login errors.
     * Sets error state if the URL contains an error parameter.
     *
     * @param event the after navigation event
     */
    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        setError(
                event.getLocation().getQueryParameters().getParameters().containsKey(
                        "error"));
    }

}