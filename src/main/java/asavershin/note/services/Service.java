package asavershin.note.services;

import asavershin.note.exceptions.EntityNotFoundException;
import asavershin.note.exceptions.FileException;

public interface Service<T, K> {
    T createEntity(T entity);

    T getEntityById(K id);

    void deleteEntityById(K id);

    T updateEntity(T entity);
}
