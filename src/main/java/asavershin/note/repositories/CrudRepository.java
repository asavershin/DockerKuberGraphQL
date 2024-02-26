package asavershin.note.repositories;


import asavershin.note.exceptions.EntityNotFoundException;

import java.util.Optional;

public interface CrudRepository<T, K> {

    T insert(T t);

    T update(T t);

    Optional<T> findById(K id);

    Boolean deleteById(K id);
}
