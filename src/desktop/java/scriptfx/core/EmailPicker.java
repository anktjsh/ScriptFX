/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scriptfx.core;

/**
 *
 * @author Aniket
 */
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import mail.extension.core.EmailFactory;
import mail.extension.core.Status;
import tachyon.core.Tachyon;

/**
 *
 * @author Aniket
 */
public class EmailPicker {

    private final Stage stage;
    private final String subject;
    private final File file;

    public EmailPicker(Window w, String sub, File f) {
        subject = sub;
        file = f;
        stage = new Stage();
        stage.initOwner(w);
        stage.setTitle("Email");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image(Tachyon.class.getResourceAsStream("/icon.png")));
        stage.setResizable(false);
        stage.setScene(new Scene(new EmailPane()));
    }

    public void showAndWait() {
        stage.showAndWait();
    }

    private class EmailPane extends BorderPane {

        private final BorderPane top, bottom, center;

        private final Button cancel, send;
        private final TextArea message;
        private final VBox box, creds;
        private final TextField username, to;
        private final PasswordField password;
        private final HBox options, attachments;
        private final Button gmail, hotmail, outlook, yahoo;
        private final Label extension;

        public EmailPane() {
            setPadding(new Insets(5, 10, 5, 10));
            top = new BorderPane();
            top.setPadding(getPadding());
            bottom = new BorderPane();
            bottom.setPadding(getPadding());
            center = new BorderPane();
            center.setPadding(getPadding());
            setTop(top);
            setBottom(bottom);
            setCenter(center);

            bottom.setLeft(cancel = new Button("Cancel"));
            bottom.setRight(send = new Button("Send"));
            cancel.setOnAction((e) -> {
                ((Stage) getScene().getWindow()).close();
            });
            send.setDisable(true);
            center.setCenter(message = new TextArea());
            message.setDisable(true);

            box = new VBox(5);
            box.setPadding(getPadding());
            box.setAlignment(Pos.CENTER_LEFT);
            box.getChildren().addAll(
                    new HBox(new Label("To : "), to = new TextField()),
                    new Label("Subject : " + subject),
                    new ScrollPane(attachments = new HBox(5)));
            center.setTop(box);

            creds = new VBox(5);
            creds.setPadding(getPadding());
            creds.getChildren().addAll(
                    new HBox(5,
                            new Label("Username : "),
                            username = new TextField(),
                            extension = new Label("")),
                    new HBox(5,
                            new Label("Password : "),
                            password = new PasswordField()));
            creds.setAlignment(Pos.CENTER);
            username.setDisable(true);
            to.setDisable(true);
            password.setDisable(true);
            top.setBottom(creds);

            top.setTop(options = new HBox(15));
            options.setAlignment(Pos.CENTER);
            options.setPadding(getPadding());
            options.getChildren().addAll(gmail = new Button("Gmail"),
                    hotmail = new Button("Hotmail"),
                    outlook = new Button("Outlook"),
                    yahoo = new Button("Yahoo"));
            gmail.setOnAction((e) -> {
                extension.setText("@gmail.com");
                gmail.setDisable(true);
                hotmail.setDisable(true);
                send.setDisable(false);
                message.setDisable(false);
                username.setDisable(false);
                to.setDisable(false);
                password.setDisable(false);
                outlook.setDisable(true);
                yahoo.setDisable(true);
            });
            hotmail.setOnAction((e) -> {
                extension.setText("@hotmail.com");
                gmail.setDisable(true);
                hotmail.setDisable(true);
                send.setDisable(false);
                message.setDisable(false);
                username.setDisable(false);
                to.setDisable(false);
                password.setDisable(false);
                outlook.setDisable(true);
                yahoo.setDisable(true);
            });
            outlook.setOnAction((E) -> {
                extension.setText("@outlook.com");
                gmail.setDisable(true);
                hotmail.setDisable(true);
                send.setDisable(false);
                message.setDisable(false);
                username.setDisable(false);
                to.setDisable(false);
                password.setDisable(false);
                outlook.setDisable(true);
                yahoo.setDisable(true);
            });
            yahoo.setOnAction((E) -> {
                extension.setText("@yahoo.com");
                gmail.setDisable(true);
                hotmail.setDisable(true);
                send.setDisable(false);
                message.setDisable(false);
                username.setDisable(false);
                to.setDisable(false);
                password.setDisable(false);
                outlook.setDisable(true);
                yahoo.setDisable(true);
            });
            attachments.getChildren().add(new Label(file.getAbsolutePath()));
            send.setOnAction((e) -> {
                ArrayList<String> al = new ArrayList<>();
                for (javafx.scene.Node n : attachments.getChildren()) {
                    if (n instanceof Label) {
                        al.add(((Label) n).getText());
                    }
                }
                Status sent = EmailFactory.newEmailFactory().setCredentials(
                        extension.getText().contains("gmail") ? EmailFactory.GMAIL
                        : extension.getText().contains("hotmail") ? EmailFactory.HOTMAIL
                        : extension.getText().contains("outlook") ? EmailFactory.OUTLOOK
                        : EmailFactory.YAHOO,
                        username.getText() + extension.getText(),
                        password.getText())
                        .addRecipient(to.getText())
                        .setSubject(subject)
                        .setMessage(message.getText())
                        .setAttachments(al)
                        .construct().send();
                if (!sent.isSuccess()) {
                    if (sent.getMessage().contains("Username and Password not accepted")) {
                        error(sent.getMessage());
                    } else if (sent.getMessage().contains("https://accounts.google.com/")) {
                        error("You must allow Tachyon to access your gmail account");
                        try {
                            java.awt.Desktop.getDesktop().browse(URI.create("https://www.google.com/settings/security/lesssecureapps"));
                        } catch (IOException ex) {
                        }
                    } else {
                        error(sent.getMessage());
                    }
                } else {
                    Alert ala = new Alert(AlertType.INFORMATION);
                    ala.initOwner(stage);
                    ala.setTitle("Email Sent");
                    ala.setHeaderText("Email Sent");
                    ala.showAndWait();
                    stage.close();
                }

            });
        }

    }

    private void error(String message) {
        Alert ala = new Alert(AlertType.ERROR);
        ala.initOwner(stage);
        ala.setTitle("Error Sending");
        ala.setHeaderText(message);
        ala.showAndWait();
    }
}
