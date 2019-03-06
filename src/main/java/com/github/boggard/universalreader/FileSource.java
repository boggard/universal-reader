package com.github.boggard.universalreader;

import java.io.IOException;
import java.io.InputStream;

public interface FileSource {

    String getFileName();

    InputStream getInputStream() throws IOException;
}
