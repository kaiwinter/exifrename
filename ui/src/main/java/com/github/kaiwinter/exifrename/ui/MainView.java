package com.github.kaiwinter.exifrename.ui;

import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import com.github.kaiwinter.exifrename.ui.log.StaticOutputStreamAppender;
import com.github.kaiwinter.exifrename.ui.log.TextAreaOutputStream;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValueBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

/**
 * The main view.
 */
public final class MainView implements FxmlView<MainViewModel>, Initializable {

    @FXML
    private TextField txtDirectoryPath;

    @FXML
    private TableView<TableModel> tvFiles;

    @FXML
    private TableColumn<TableModel, String> tcolOriginalName;

    @FXML
    private TableColumn<TableModel, Date> tcolExifDate;

    @FXML
    private TableColumn<TableModel, String> tcolNewName;

    @FXML
    private Button btnRename;

    @FXML
    private TextArea log;

    @InjectViewModel
    private MainViewModel viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tcolOriginalName.setCellValueFactory(value -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return value.getValue().getOriginalName();
            }
        });

        tcolExifDate.setCellValueFactory(value -> new ObservableValueBase<Date>() {
            @Override
            public Date getValue() {
                return value.getValue().getExifOriginalDate();
            }
        });

        tcolNewName.setCellValueFactory(value -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return value.getValue().getNewName();
            }
        });

        viewModel.filesProperty().bind(tvFiles.itemsProperty());
        btnRename.disableProperty().bind(Bindings.size(viewModel.filesProperty()).isEqualTo(0));

        OutputStream os = new TextAreaOutputStream(log);
        StaticOutputStreamAppender.setStaticOutputStream(os);
    }

    @FXML
    void chooseDirectoyOnAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        if (!txtDirectoryPath.getText().isEmpty()) {
            directoryChooser.setInitialDirectory(new File(txtDirectoryPath.getText()));
        }
        File selectedDirectory = directoryChooser.showDialog(btnRename.getScene().getWindow());
        if (selectedDirectory != null) {
            viewModel.filesProperty().clear();
            log.clear();
            txtDirectoryPath.setText(selectedDirectory.getAbsolutePath());
            viewModel.pathChoosen(selectedDirectory);
        }
    }

    @FXML
    void renameOnAction(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Rename");
        alert.setHeaderText("Rename all files?");
        alert.setContentText(
            "Are you sure to rename the shown files? This can't be undone and the original file names get lost!");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            viewModel.rename();
        }
    }
}
