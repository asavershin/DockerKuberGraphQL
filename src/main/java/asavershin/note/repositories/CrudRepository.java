package asavershin.note.repositories;


import java.util.Optional;

public interface CrudRepository<T, K> {

    T save(T t);

    T update(T t);

    Optional<T> findById(K id);

    Boolean deleteById(K id);
}
