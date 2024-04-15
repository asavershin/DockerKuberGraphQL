package asavershin.note.controllers;

import asavershin.note.dto.OperationDTO;
import asavershin.note.entities.OperationType;
import asavershin.note.mappers.OperationMapper;
import asavershin.note.services.OperationService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/operation")
public class OperationController {
    private final OperationService operationService;
    private final OperationMapper operationMapper;

    @GetMapping("/{type}")
    public List<OperationDTO> getOperationsByType(
            @PathVariable @Pattern(regexp = "READ|WRITE|UPDATE|DELETE") String type
    ){
        return operationMapper.toDto(operationService.getOperationByType(OperationType.valueOf(type)));
    }
}
