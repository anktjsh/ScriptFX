package tachyon.core;

import com.gluonhq.charm.down.common.Platform;
import com.gluonhq.charm.down.common.PlatformFactory;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.layout.layer.SidePopupView;
import com.gluonhq.charm.glisten.license.License;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tachyon.core.views.EnvironmentView;
import tachyon.core.views.Launcher;
import tachyon.core.views.ProjectManagerView;
import tachyon.core.views.Samples;
import tachyon.core.views.ScriptWriter;
import tachyon.core.views.Settings;

@License(key = "")
public class Tachyon extends MobileApplication {

    public static final String PRIMARY_VIEW = HOME_VIEW;
    public static final String SECONDARY_VIEW = "FileManager View";
    public static final String MENU_LAYER = "Side Menu";
    public static final String LAUNCHER = "Launcher";
    public static final String SETTINGS = "Settings";
    public static final String SAMPLES = "Samples";

    public static NavigationDrawer drawer;
    public static Item primaryItem, secondaryItem, settings, samp;

    @Override
    public void init() {
        addViewFactory(PRIMARY_VIEW, () -> new EnvironmentView(PRIMARY_VIEW));
        addViewFactory(SECONDARY_VIEW, () -> new ProjectManagerView(SECONDARY_VIEW));
        addViewFactory(LAUNCHER, () -> new Launcher(LAUNCHER));
        addViewFactory(SETTINGS, () -> new Settings(SETTINGS));
        addViewFactory(SAMPLES, () -> new Samples(SAMPLES));

        drawer = new NavigationDrawer();

        NavigationDrawer.Header header = new NavigationDrawer.Header("ScriptFx",
                "Web Development IDE",
                new Avatar(21, new Image(Tachyon.class.getResourceAsStream("/icon.png"))));
        drawer.setHeader(header);

        primaryItem = new Item("Environment", MaterialDesignIcon.HOME.graphic());
        secondaryItem = new Item("ProjectManager", MaterialDesignIcon.DASHBOARD.graphic());
        settings = new Item("Settings", MaterialDesignIcon.SETTINGS.graphic());
        samp = new Item("Samples", MaterialDesignIcon.ATTACHMENT.graphic());
        drawer.getItems().addAll(primaryItem, secondaryItem, samp, settings);

        drawer.selectedItemProperty().addListener((obs, oldItem, newItem) -> {
            hideLayer(MENU_LAYER);
            switchView(newItem.equals(primaryItem) ? PRIMARY_VIEW : newItem.equals(secondaryItem) ? SECONDARY_VIEW : newItem.equals(samp) ? SAMPLES : SETTINGS);
        });

        addLayerFactory(MENU_LAYER, () -> new SidePopupView(drawer));
    }

    public static Scene application;

    @Override
    public void postInit(Scene scene) {
        application = scene;
        Settings.getCurrentTheme().assignTo(scene);
        Settings.getCurrentSwatch().assignTo(scene);

        scene.getStylesheets().add(Tachyon.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(Tachyon.class.getResourceAsStream("/icon.png")));
        Stage st = ((Stage) scene.getWindow());
        PlatformFactory.getPlatform().setOnLifecycleEvent((Platform.LifecycleEvent param) -> {
            if (param == Platform.LifecycleEvent.PAUSE || param == Platform.LifecycleEvent.STOP) {
                ((ScriptWriter) ((EnvironmentView) getInstance().retrieveView(HOME_VIEW).get()).getCenter()).saveOpenProjectsInformation();
            }
            return null;
        });
        st.setOnHidden((e) -> {
            ((ScriptWriter) ((EnvironmentView) getInstance().retrieveView(HOME_VIEW).get()).getCenter()).saveOpenProjectsInformation();
        });
    }
}
