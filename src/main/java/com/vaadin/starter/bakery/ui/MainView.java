package com.vaadin.starter.bakery.ui;

import static com.vaadin.flow.i18n.I18NProvider.translate;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.internal.LocaleUtil;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.Version;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.starter.bakery.ui.views.HasConfirmation;
import com.vaadin.starter.bakery.ui.views.admin.products.ProductsView;
import com.vaadin.starter.bakery.ui.views.admin.users.UsersView;
import com.vaadin.starter.bakery.ui.views.dashboard.DashboardView;
import com.vaadin.starter.bakery.ui.views.storefront.StorefrontView;

import jakarta.annotation.PostConstruct;

/**
 * Main application layout that provides navigation and common UI elements
 * for the Bakery application. Includes menu tabs, confirmation dialogs,
 * and environment information display.
 */
public class MainView extends AppLayout {

    @Autowired
    private AccessAnnotationChecker accessChecker;
    @Autowired
    private AuthenticationContext authenticationContext;
    private final ConfirmDialog confirmDialog = new ConfirmDialog();
    private Tabs menu;

    /**
     * Initializes the main view with navigation menu and common components.
     */
    @PostConstruct
    public void init() {
        confirmDialog.setCancelable(true);
        confirmDialog.setConfirmButtonTheme("raised tertiary error");
        confirmDialog.setCancelButtonTheme("raised tertiary");

        this.setDrawerOpened(false);
        Span appName = new Span(translate("app.title"));
        appName.addClassName("app-name");
        appName.addClassName("hide-on-mobile");

        menu = createMenuTabs();

        // Handle logout
        menu.addSelectedChangeListener(e -> {
            if (e.getSelectedTab() == null) {
                return;
            }

            e.getSelectedTab().getId().ifPresent(id -> {
                if ("logout-tab".equals(id)) {
                    authenticationContext.logout();
                }
            });
        });

        this.addToNavbar(appName);
        this.addToNavbar(true, menu);
        this.getElement().appendChild(confirmDialog.getElement());

        getElement().addEventListener("search-focus", e -> {
            getElement().getClassList().add("hide-navbar");
        });

        getElement().addEventListener("search-blur", e -> {
            getElement().getClassList().remove("hide-navbar");
        });

        showInfo();
    }

    /**
     * Handles navigation events and updates the UI state accordingly.
     */
    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        confirmDialog.setOpened(false);
        if (getContent() instanceof HasConfirmation) {
            ((HasConfirmation) getContent()).setConfirmDialog(confirmDialog);
        }
        RouteConfiguration configuration = RouteConfiguration.forSessionScope();
        if (configuration.isRouteRegistered(this.getContent().getClass())) {
            String target = configuration.getUrl(this.getContent().getClass());
            Optional<Component> tabToSelect = menu.getChildren().filter(tab -> {
                Component child = tab.getChildren().findFirst().get();
                return child instanceof RouterLink && ((RouterLink) child).getHref().equals(target);
            }).findFirst();
            tabToSelect.ifPresent(tab -> menu.setSelectedTab((Tab) tab));
        } else {
            menu.setSelectedTab(null);
        }
    }

    /**
     * Creates the main menu tabs for navigation.
     *
     * @return the configured Tabs component
     */
    private Tabs createMenuTabs() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        tabs.add(getAvailableTabs());
        return tabs;
    }

    /**
     * Gets the available tabs based on user access permissions.
     *
     * @return array of Tab components for navigation
     */
    private Tab[] getAvailableTabs() {
        final List<Tab> tabs = new ArrayList<>(4);
        tabs.add(createTab(VaadinIcon.EDIT, translate("storefront"), StorefrontView.class));
        tabs.add(createTab(VaadinIcon.CLOCK, translate("dashboard"), DashboardView.class));
        if (accessChecker.hasAccess(UsersView.class,
                VaadinServletRequest.getCurrent().getHttpServletRequest())) {
            tabs.add(createTab(VaadinIcon.USER, translate("users"), UsersView.class));
        }
        if (accessChecker.hasAccess(ProductsView.class,
                VaadinServletRequest.getCurrent().getHttpServletRequest())) {
            tabs.add(createTab(VaadinIcon.CALENDAR, translate("products"), ProductsView.class));
        }
        final String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
        final Tab logoutTab = createTab(createLogoutLink(contextPath));
        logoutTab.setId("logout-tab");
        tabs.add(logoutTab);
        return tabs.toArray(new Tab[tabs.size()]);
    }

    /**
     * Shows environment information and version details in a notification.
     * Only shown when SHOW_INFO environment variable is set or when debugging.
     */
    private void showInfo() {

        String version = "Platform: " + Version.getFullVersion();
        String theme = version.startsWith("24.7") ? "" : "dark";
        UI.getCurrent().getElement().setAttribute("theme", theme);

        String showInfo = System.getenv("SHOW_INFO");
        if ((showInfo == null || "false".equals(showInfo))
                && !ManagementFactory.getRuntimeMXBean().getInputArguments()
                .toString().contains("jwdp")) {
            return;
        }

        List<String> items = new ArrayList<>(List.of(
                "com.vaadin.flow.server.VaadinService",
                "com.vaadin.controlcenter.starter.actuate.endpoint.VaadinActuatorEndpoint",
                "org.springframework.data.domain.Pageable"));
        if (showInfo != null) {
            for (String s : showInfo.split("[, ]+")) {
                if (s.contains(".")) {
                    items.add(s);
                }
            }
        }

        String header = "header: " + VaadinService.getCurrentRequest()
                .getHeader("Accept-Language");
        String locales = "available: " + LocaleUtil.getI18NProvider().get()
                .getProvidedLocales().stream().map(l -> l.toString())
                .collect(Collectors.joining(","));
        String locale = "selected: " + getLocale().toString();

        VerticalLayout versions = new VerticalLayout(new Div(version), new Div(header),
                new Div(locales), new Div(locale));
        versions.setSpacing(false);

        for (String string : items) {
            try {
                String jarPath = Class.forName(string).getProtectionDomain()
                        .getCodeSource().getLocation().getPath();
                String s = jarPath
                        .replaceFirst(".*/([^/]+)\\.jar(!?/.*)?", "$1");
                versions.add(new Div("· " + s));
            } catch (Exception ignore) {
            }
        }
        Notification n = new Notification(versions);
        n.setPosition(Position.TOP_END);
        n.open();
    }

    /**
     * Creates a navigation tab with icon and title.
     *
     * @param icon the VaadinIcon to display
     * @param title the tab title
     * @param viewClass the view class to navigate to
     * @return the created Tab component
     */
    private static Tab createTab(VaadinIcon icon, String title, Class<? extends Component> viewClass) {
        return createTab(populateLink(new RouterLink("", viewClass), icon, title));
    }

    /**
     * Creates a tab with the specified content.
     *
     * @param content the content to add to the tab
     * @return the created Tab component
     */
    private static Tab createTab(Component content) {
        final Tab tab = new Tab();
        tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
        tab.add(content);
        return tab;
    }

    /**
     * Creates a logout link.
     *
     * @param contextPath the application context path
     * @return the created Anchor component for logout
     */
    private static Anchor createLogoutLink(String contextPath) {
        final Anchor a = populateLink(new Anchor(), VaadinIcon.ARROW_RIGHT, translate("logout"));
        return a;
    }

    /**
     * Populates a link with icon and title.
     *
     * @param <T> the component type
     * @param a the component to populate
     * @param icon the VaadinIcon to add
     * @param title the title text to add
     * @return the populated component
     */
    private static <T extends HasComponents> T populateLink(T a, VaadinIcon icon, String title) {
        a.add(icon.create());
        a.add(title);
        return a;
    }
}