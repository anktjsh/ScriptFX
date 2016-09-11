package scriptfx.core;

import com.gluonhq.charm.down.common.PlatformFactory;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author Aniket
 */
public class LaunchWindow extends BorderPane {

    private final Project project;
    private final Browser browser;

    public LaunchWindow(Project pro, String code) {
        project = pro;
        setCenter(browser = new Browser());
        if (code == null) {
            browser.getEngine().load(project.getHtmlUri());
        } else {
            browser.getEngine().loadContent(code);
        }
    }
    
    public ReadOnlyStringProperty titleProperty() {
        return browser.getEngine().titleProperty();
    }

    public WebEngine getEngine() {
        return browser.getEngine();
    }

    public static class Browser extends BorderPane {

        private final WebView web;

        public Browser() {
            web = new WebView();
            setCenter(web);
            web.getEngine().setPromptHandler((param) -> {
                GlistenInputDialog dialog = new GlistenInputDialog(param.getDefaultValue());
                dialog.setTitle(param.getMessage());
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    return result.get();
                }
                return "null";
            });
            web.getEngine().setOnAlert((e) -> {
                com.gluonhq.charm.glisten.control.Alert al = new com.gluonhq.charm.glisten.control.Alert(Alert.AlertType.INFORMATION);
                al.setTitleText("Alert");
                al.setContentText(e.getData());
                al.showAndWait();
            });
            web.getEngine().setConfirmHandler((String msg) -> {
                com.gluonhq.charm.glisten.control.Alert alert = new com.gluonhq.charm.glisten.control.Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitleText("Confirmation");
                alert.setContentText(msg);
                Optional<ButtonType> result = alert.showAndWait();
                return result.get() == ButtonType.OK;
            });
            web.getEngine().setCreatePopupHandler((pro) -> {
                WebView wb = new WebView();
                wb.getEngine().locationProperty().addListener((ob, older, newer) -> {
                    if (newer!=null) {
                        if (!newer.isEmpty()) {
                            if (!newer.equals("about:blank")) {
                                try {
                                    PlatformFactory.getPlatform().launchExternalBrowser(newer);
                                } catch (IOException | URISyntaxException ex) {
                                }
                                web.getEngine().load("");
                            }
                        }
                    }
                });
                return wb.getEngine();
            });
            
        }

        public WebEngine getEngine() {
            return web.getEngine();
        }
    }
}
