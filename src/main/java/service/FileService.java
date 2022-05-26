package service;

import lombok.Getter;

import java.io.File;

public class FileService {

    @Getter
    private File file;

    public void addPathName(String pathName) {
        file = new File(pathName);
    }
}
