package com.github.kaiwinter.exifrename.ui;

import java.io.File;
import java.util.Date;
import java.util.Set;

import com.github.kaiwinter.exifrename.type.RenameOperation;
import com.github.kaiwinter.exifrename.uc.RenameUc;

import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;

/**
 * The view model with the logic of the main view.
 */
public final class MainViewModel implements ViewModel {

    private RenameUc renameUc = new RenameUc();
    private final ListProperty<TableModel> files = new SimpleListProperty<>();
    private Set<RenameOperation> renameOperations;

    public ListProperty<TableModel> filesProperty() {
        return this.files;
    }

    public void pathChoosen(File selectedDirectory) {
        renameOperations = renameUc.createRenameOperationsForDirectory(selectedDirectory.toPath());

        for (RenameOperation renameOperation : renameOperations) {
            TableModel tm = new TableModel();
            tm.setOriginalName(renameOperation.getOldFilename().getFileName().toString());
            // FIXME KW:
            tm.setExifOriginalDate(new Date());
            tm.setNewName(renameOperation.getNewFilenamePath().getFileName().toString());

            files.add(tm);
        }
    }

    public void rename() {
        renameUc.executeRenameOperations(renameOperations);
    }
}
