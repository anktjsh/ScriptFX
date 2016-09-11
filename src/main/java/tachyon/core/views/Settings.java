/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tachyon.core.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import com.gluonhq.charm.glisten.visual.Theme;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import scriptfx.core.FileUtils;
import tachyon.core.Tachyon;

/**
 *
 * @author Aniket
 */
public class Settings extends View {

    public static final ObjectProperty<Theme> theme = new SimpleObjectProperty<>();
    public static final ObjectProperty<Font> fontSize = new SimpleObjectProperty<>();
    private static Swatch swatch;

    static {
        loadAll();
    }

    public static Theme getCurrentTheme() {
        if (theme.get() == null) {
            theme.set(Theme.LIGHT);
        }
        return theme.get();
    }

    public static Font getCurrentFont() {
        if (fontSize.get() == null) {
            fontSize.set(Font.getDefault());
        }
        return fontSize.get();
    }

    private static void loadAll() {
        File f = FileUtils.newFile("year.txt");
        if (f.exists()) {
            try {
                Scanner in = new Scanner(f);
                if (in.hasNextLine()) {
                    String s = in.nextLine();
                    if (s.equals(Theme.DARK.name())) {
                        theme.set(Theme.DARK);
                    } else if (s.equals(Theme.LIGHT.name())) {
                        theme.set(Theme.LIGHT);
                    }
                }
                if (in.hasNextLine()) {
                    String s = in.nextLine();
                    fontSize.set(new Font(Double.parseDouble(s)));
                }
                if (in.hasNextLine()) {
                    String s = in.nextLine();
                    for (Swatch sw : Swatch.values()) {
                        if (sw.name().equals(s)) {
                            swatch = sw;
                        }
                    }
                    if (swatch == null) {
                        swatch = Swatch.RED;
                    }
                }
            } catch (FileNotFoundException ex) {
            }
        }
    }
    private final VBox box;
    private final ListView<Theme> themes;
    private final ListView<Swatch> swatches;
    private final Slider size;
    private final TextField show;

    public Settings(String string) {
        super(string);
        setCenter(box = new VBox());
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(5));
        box.getChildren().add(new Label("Theme"));
        box.getChildren().addAll(themes = new ListView<>(), new Label("Font Size"), size = new Slider(5, 50, getCurrentFont().getSize()),
                show = new TextField(), new Label("Color Scheme"), swatches = new ListView<>());
        swatches.getItems().addAll(Swatch.values());
        themes.getItems().addAll(Theme.values());
        show.setEditable(false);
        size.setBlockIncrement(1);
        size.setSnapToTicks(true);
        size.setShowTickLabels(true);
        size.setShowTickMarks(true);
        size.setMinorTickCount(5);
        size.setMajorTickUnit(10);
        size.valueProperty().addListener((ob, older, newer) -> {
            if (newer != null) {
                Font f = new Font((newer.doubleValue()));
                fontSize.set(f);
                File fa = FileUtils.newFile("year.txt");
                FileUtils.write(fa, FXCollections.observableArrayList(getCurrentTheme().name(), fontSize.get().getSize() + "", getCurrentSwatch().name()));
                show.setText(getCurrentFont().getSize() + "");
            }
        });
        show.setText(getCurrentFont().getSize() + "");
        themes.getSelectionModel().selectedItemProperty().addListener((ob, older, newer) -> {
            if (newer != null) {
                if (getCurrentTheme() != themes.getSelectionModel().getSelectedItem()) {
                    theme.set(themes.getSelectionModel().getSelectedItem());
                    theme.get().assignTo(Tachyon.application);
                    File f = FileUtils.newFile("year.txt");
                    FileUtils.write(f, FXCollections.observableArrayList(getCurrentTheme().name(), fontSize.get().getSize() + "", getCurrentSwatch().name()));
                }
            }
        });
        swatches.getSelectionModel().selectedItemProperty().addListener((ob, older, newer) -> {
            if (newer != null) {
                swatch = newer;
                swatch.assignTo(Tachyon.application);
                File f = FileUtils.newFile("year.txt");
                FileUtils.write(f, FXCollections.observableArrayList(getCurrentTheme().name(), fontSize.get().getSize() + "", getCurrentSwatch().name()));
            }
        });
        swatches.getSelectionModel().select(getCurrentSwatch());
        themes.getSelectionModel().select(getCurrentTheme());
        box.getChildren().add(new Label("ScriptFx v1.0.0 Created by Aniket Joshi"));
    }

    @Override
    protected void updateAppBar(AppBar appBar) {
        appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Tachyon.MENU_LAYER)));
        appBar.setTitleText("Settings");
    }

    public static Swatch getCurrentSwatch() {
        if (swatch == null) {
            swatch = Swatch.RED;
        }
        return swatch;
    }

}
