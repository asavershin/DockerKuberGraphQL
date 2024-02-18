package asavershin.note.services;

import asavershin.note.exceptions.EntityNotFoundException;

public interface Service<T, K> {
    T createEntity(T entity);

    T getEntityById(K id) throws EntityNotFoundException;

    void deleteEntityById(K id) throws EntityNotFoundException;

    T updateEntity(T entity) throws EntityNotFoundException;
}
