package com.github.kaiwinter.exifrename.ui.log;

import java.io.IOException;
import java.io.OutputStream;

import javafx.scene.control.TextArea;

/**
 * Writes the output data to a {@link TextArea}.
 */
public final class TextAreaOutputStream extends OutputStream {

    private final TextArea textArea;

    /**
     * Constructs a ne {@link TextAreaOutputStream}.
     *
     * @param textArea
     *            the {@link TextArea} to which the data will be written
     */
    public TextAreaOutputStream(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        textArea.appendText(String.valueOf((char) b));
    }
}