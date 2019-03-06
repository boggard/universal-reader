# universal-reader

## Description

The library intended to read or process content from files with different formats (like csv, txt, xls, xlsx) 
via one general interface.

## Install

### Gradle

````
compile('com.github.boggard:universal-reader:1.0.0')
````

### Maven

````
<dependency>
    <groupId>com.github.boggard</groupId>
    <artifactId>universal-reader</artifactId>
    <version>2.0.0</version>
</dependency>
````

## Getting started

### Entry point

To process file with data you need to call static method

````
UniversalReader.processRecords(FileSource fileSource, ContentHandler<R> contentsHandler, ReaderConfiguration configuration)
````

About each parameter below.

### FileSource

Interface for wrapping the to be processed.

Example for **java.io.File**:

````
public class SystemFileSource implements FileSource {
    
    private final File file;

    public SystemFileSource(File file) {
        this.file = file;
    }

    @Override
    public String getFileName() {
        return file.getName();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }
}
````

### ContentHandler

To handle content of file you need to implements interface ContentHandler which contains events of reading process.
Simple example of implementation to read list of records from file:

````
ContentHandler<Integer> contentHandler = new ContentHandler<Integer>() {
                private int recordsCount = 0;
                private String[] currentRecord;

                @Override
                public void startRecord() {
                    currentRecord = new String[2];
                }

                @Override
                public void startField(int index, String field) {
                    switch(index) {
                        case 0: //first field of record
                            currentRecord[0] = field;
                            break;
                        case 1: //second field of record
                            currentRecord[1] = field;
                            break;
                    }
                }

                @Override
                public void endRecord() {
                    //do something with record, save for example
                    save(currentRecord);
                    recordsCount++;
                }

                @Override
                public Integer getResult() {
                    return recordsCount;
                }
            };
````

As you see the interface is a generic and you can specify the result type you want.

### ReaderConfiguration

Class for content reading utility configurations like records or fields separator symbol.

You can use factory constructors

* to create default configuration:
````
ReaderConfiguration.defaultReaderConfiguration();
````

* to create default configuration for files with header **(start line index = 1 instead of 0)**:
````
ReaderConfiguration.withHeaderDefaultConfiguration();
````

* to create custom configuration:
````
ReaderConfiguration.customConfiguration("\n", "\t", 2);
````



