/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tachyon.core.views;

import com.gluonhq.charm.glisten.control.Alert;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import scriptfx.core.Project;
import scriptfx.core.Script;
/**
 *
 * @author Aniket
 */
public class Editor extends ScriptTab {

    private final TextArea area;

    public Editor(Script sc, Project pro) {
        super(sc, pro);
        area = new TextArea();
        area.setWrapText(true);
        if (Settings.fontSize.get() != null) {
            area.setStyle("-fx-font-size:" + Settings.fontSize.get().getSize() + ";");
        }
        Settings.fontSize.addListener((ob, older, newer) -> {
            if (newer != null) {
                area.setStyle("-fx-font-size:" + newer.getSize() + ";");
            }
        });
        bindMouseListeners();
        getCenter().setCenter((area));
        readFromScript();
        setOnCloseRequest((e) -> {
            if (canSave()) {
                Alert al = new Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                al.setContentText("Would you like to save before closing?");
                Optional<ButtonType> show = al.showAndWait();
                if (show.isPresent()) {
                    if (show.get() == ButtonType.OK) {
                        save();
                    } else if (show.get() == ButtonType.CANCEL) {
                        e.consume();
                    }
                }
            }
        });
    }

    public void replace() {
        if (getCenter().getTop() == null) {
            VBox total = new VBox();
            total.setStyle("-fx-background-fill:gray;");
            BorderPane main = new BorderPane(total);
            HBox top = new HBox(15);
            HBox bottom = new HBox(5);
            top.setStyle("-fx-background-fill:gray;");
            bottom.setStyle("-fx-background-fill:gray;");
            total.getChildren().addAll(top, bottom);
            bottom.setPadding(new Insets(5, 10, 5, 10));
            top.setPadding(new Insets(5, 10, 5, 10));
            TextField fi, replace;
            Button prev, next, rep, reAll, close;
            top.getChildren().addAll(fi = new TextField(),
                    prev = new Button("Previous"),
                    next = new Button("Next"));
            bottom.getChildren().addAll(replace = new TextField(),
                    rep = new Button("Replace"),
                    reAll = new Button("Replace All"));
            fi.setOnAction((ea) -> {
                if (area.getSelection().getLength() == 0) {
                    String a = fi.getText();
                    int index = area.getText().indexOf(a);
                    if (index != -1) {
                        area.selectRange(index, index + a.length());
                    }
                } else {
                    next.fire();
                }
            });
            replace.setOnAction((es) -> {
                rep.fire();
            });
            prev.setOnAction((efd) -> {
                int start = area.getSelection().getStart();
                String a = area.getText().substring(0, start);
                int index = a.lastIndexOf(fi.getText());
                if (index != -1) {
                    area.selectRange(index, index + fi.getText().length());
                }
            });
            next.setOnAction((sdfsdfsd) -> {
                int end = area.getSelection().getEnd();
                String a = area.getText().substring(end);
                int index = a.indexOf(fi.getText());
                if (index != -1) {
                    index += end;
                    area.selectRange(index, index + fi.getText().length());
                } else {
                    area.selectRange(0, 0);
                    Alert al = new Alert(javafx.scene.control.Alert.AlertType.WARNING, "No more instances found!");
                    al.showAndWait();
                }
            });
            rep.setOnAction((sdfsdfsd) -> {
                String a = fi.getText();
                String b = replace.getText();
                if (area.getText().contains(a)) {
                    int index = area.getText().indexOf(a);
                    area.replaceText(index, index + a.length(), b);
                }
            });
            reAll.setOnAction((efsf) -> {
                String a = fi.getText();
                String b = replace.getText();
                int index = 0;
                while (area.getText().substring(index).contains(a)) {
                    index += area.getText().substring(index).indexOf(a);
                    area.replaceText(index, index + a.length(), b);
                    index += b.length();
                }
            });
            getCenter().setTop(main);
            fi.requestFocus();
        } else {
            getCenter().setTop(null);
        }
    }

    private void bindMouseListeners() {
        area.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                area.deleteText(area.getSelection());
                int n = area.getCaretPosition();
                if (n != 0) {
                    String spl[] = area.getText().split("\n");
                    int count = 0;
                    for (String spl1 : spl) {
                        count += spl1.length() + 1;
                        if (n <= count) {
                            String tabs = "\n" + getTabText(spl1);
                            area.insertText(n, tabs);
                            area.positionCaret(n + tabs.length());
                            e.consume();
                            break;
                        }
                    }
                }
            }
            if (e.getCode() == KeyCode.TAB) {
                int n = area.getCaretPosition();
                String spl[] = area.getText().split("\n");
                int count = 0;
                for (String spl1 : spl) {
                    count += spl1.length() + 1;
                    if (n <= count) {
                        String b = area.getText().substring(n);
                        area.insertText(n, "    ");
                        area.positionCaret(n + 4);
                        e.consume();
                        break;
                    }
                }
            }
        });
    }

    private String getTabText(String s) {
        int count = 0;
        for (int x = 0; x < s.length(); x += 4) {
            if (s.length() > x + 3) {
                if (s.substring(x, x + 4).equals("    ")) {
                    count++;
                }
            } else {
                break;
            }
        }
        String ret = "";
        String temp = s.trim();
        if (temp.endsWith(")") || temp.endsWith("{")) {
            count++;
        }
        for (int x = 0; x < count; x++) {
            ret += "    ";
        }
        return ret;
    }

    private void readFromScript() {
        String read = getScript().getCurrentCode();
        area.appendText(read);
    }

    public TextArea getCodeArea() {
        return area;
    }

    public void save() {
        if (getScript().canSave(area.getText())) {
            getScript().save(area.getText());
        }
    }

    public boolean canSave() {
        return getScript().canSave(area.getText());
    }

    public void undo() {
        area.undo();
    }

    public void redo() {
        area.redo();
    }

    public void cut() {
        area.cut();
    }

    public void copy() {
        area.copy();
    }

    public void paste() {
        area.paste();
    }

    public void close() {
        if (Platform.isFxApplicationThread()) {
            Event.fireEvent(this, new Event(Tab.TAB_CLOSE_REQUEST_EVENT));
            getTabPane().getTabs().remove(this);
        } else {
            Platform.runLater(() -> {
                Event.fireEvent(this, new Event(Tab.TAB_CLOSE_REQUEST_EVENT));
                getTabPane().getTabs().remove(this);
            });
        }
    }

}
