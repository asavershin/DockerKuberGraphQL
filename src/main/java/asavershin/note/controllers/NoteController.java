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
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public NoteDTO createNote(@Argument @Valid @RequestBody NoteDTO noteInput){
        return noteMapper
                .toDto(noteService
                        .createEntity(noteMapper.toEntity(noteInput)));
    }

    @GetMapping("/{noteId}")
    @QueryMapping
    public NoteDTO getNoteById(@Argument @PathVariable Long noteId) {
        return noteMapper
                .toDto(noteService.getEntityById(noteId));
    }

    @PutMapping("/{noteId}")
    @MutationMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public NoteDTO updateNoteById(@Argument @PathVariable Long noteId,
                                  @Argument @Valid @RequestBody NoteDTO noteInput){
        return noteMapper
                .toDto(noteService
                        .updateEntity(noteMapper.toEntity(noteId, noteInput)));
    }

    @DeleteMapping("/{noteId}")
    @MutationMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@Argument @PathVariable Long noteId) {
        noteService.deleteEntityById(noteId);
    }

    @GetMapping("/findAll")
    @QueryMapping
    public List<NoteDTO> findAllNotes(@Argument Long pageNumber, @Argument Long pageSize){
        return noteMapper.toDto(noteService.findAll(pageNumber, pageSize));
    }

}
