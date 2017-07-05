package com.github.kaiwinter.exifrename.ui;

import java.util.Date;

/**
 * The model which is shown in the table of the view.
 */
public final class TableModel {

    private String originalName;
    private Date exifOriginalDate;
    private String newName;

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Date getExifOriginalDate() {
        return exifOriginalDate;
    }

    public void setExifOriginalDate(Date exifOriginalDate) {
        this.exifOriginalDate = exifOriginalDate;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

}
