package asavershin.note.repositories;

import asavershin.note.entities.OperationEntity;
import asavershin.note.entities.OperationType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends MongoRepository<OperationEntity, String> {
    List<OperationEntity> findAllByType(OperationType type);
}
