package com.vaadin.starter.bakery.ui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

import static com.vaadin.starter.bakery.ui.utils.BakeryConst.VIEWPORT;

/**
 * Application shell configuration for the Bakery application.
 * Configures viewport settings, theme, and Progressive Web App (PWA) properties.
 */
@Viewport(VIEWPORT)
@Theme(value = "bakery", variant = "dark")
@PWA(name = "Bakery App Starter", shortName = "###Bakery###",
        startPath = "login",
        backgroundColor = "#227aef", themeColor = "#227aef",
        offlinePath = "offline-page.html",
        offlineResources = {"images/offline-login-banner.jpg"})
public class AppShell implements AppShellConfigurator {
}