package service;

import java.io.File;

public class FileService {

    public File getFile(String pathName) {
        return new File(pathName);
    }
}
