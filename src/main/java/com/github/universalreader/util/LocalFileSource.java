package com.github.universalreader.util;

import com.github.universalreader.FileSource;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class LocalFileSource implements FileSource {

    private final File file;

    @Override
    public String getFileName() {
        return file.getName();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }
}
