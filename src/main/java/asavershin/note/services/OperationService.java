package asavershin.note.services;

import asavershin.note.entities.OperationEntity;
import asavershin.note.entities.OperationType;

import java.util.List;

public interface OperationService {
    public void logOperation(OperationEntity operation);
    public List<OperationEntity> getOperationByType(OperationType type);
}
