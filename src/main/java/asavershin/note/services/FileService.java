package asavershin.note.services;

import asavershin.note.exceptions.FileException;

import java.util.List;

public interface FileService<T, K> {
    T saveFile(K file);
    byte[] getFile(String link);
    void deleteFiles(List<String> files);
}
