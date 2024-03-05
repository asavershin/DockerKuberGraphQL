package asavershin.note.services.iml;

import asavershin.note.entities.OperationEntity;
import asavershin.note.entities.OperationType;
import asavershin.note.repositories.OperationRepository;
import asavershin.note.services.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationServiceIml implements OperationService {
    private final OperationRepository operationRepository;
    @Override
    public void logOperation(OperationEntity operation) {
        operationRepository.save(operation);
    }

    @Override
    public List<OperationEntity> getOperationByType(OperationType type) {
        return operationRepository.findAllByType(type);
    }
}
