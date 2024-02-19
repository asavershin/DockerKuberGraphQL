package asavershin.note.controllers;


import asavershin.note.dto.NoteDTO;
import asavershin.note.exceptions.EntityNotFoundException;
import asavershin.note.mappers.NoteMapper;
import asavershin.note.services.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final NoteMapper noteMapper;

    @PostMapping
    @MutationMapping
    public NoteDTO createNote(@Argument @Valid @RequestBody NoteDTO noteInput){
        return noteMapper
                .toDto(noteService
                        .createEntity(noteMapper.toEntity(noteInput)));
    }

    @GetMapping("/{noteId}")
    @QueryMapping
    public NoteDTO getNoteById(@Argument @PathVariable Long noteId) throws EntityNotFoundException {
        return noteMapper
                .toDto(noteService.getEntityById(noteId));
    }

    @PutMapping("/{noteId}")
    @MutationMapping
    public NoteDTO updateNoteById(@Argument @PathVariable Long noteId,
                                  @Argument @Valid @RequestBody NoteDTO noteInput) throws EntityNotFoundException {
        return noteMapper
                .toDto(noteService
                        .updateEntity(noteMapper.toEntity(noteId, noteInput)));
    }

    @DeleteMapping("/{noteId}")
    @MutationMapping
    public void deleteNote(@Argument @PathVariable Long noteId) throws EntityNotFoundException {
        noteService.deleteEntityById(noteId);
    }

    @GetMapping("/findAll")
    @QueryMapping
    public List<NoteDTO> findAllNotes(@Argument Long pageNumber, @Argument Long pageSize){
        return noteMapper.toDto(noteService.findAll(pageNumber, pageSize));
    }

}
