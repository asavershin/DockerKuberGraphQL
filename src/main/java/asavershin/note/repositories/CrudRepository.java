package asavershin.note.repositories;


import asavershin.note.exceptions.EntityNotFoundException;

import java.util.Optional;

public interface CrudRepository<T, K> {

    T insert(T t);

    T update(T t) throws EntityNotFoundException;

    Optional<T> findById(K id) throws EntityNotFoundException;

    Boolean delete(K id);
}
