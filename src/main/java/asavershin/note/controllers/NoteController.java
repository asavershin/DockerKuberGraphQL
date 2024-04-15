package asavershin.note.controllers;


import asavershin.note.dto.NoteDTO;
import asavershin.note.entities.UserEntity;
import asavershin.note.exceptions.EntityNotFoundException;
import asavershin.note.mappers.NoteMapper;
import asavershin.note.services.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
@Slf4j
public class NoteController {

    private final NoteService noteService;
    private final NoteMapper noteMapper;

    @PostMapping
    @MutationMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteDTO createNote(@Argument @Valid @RequestBody NoteDTO noteInput, @AuthenticationPrincipal UserEntity user) {
        return noteMapper
                .toDto(noteService
                        .createEntity(noteMapper.toEntity(noteInput),user.getUserId()));
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
                                  @Argument @Valid @RequestBody NoteDTO noteInput,
                                  @AuthenticationPrincipal UserEntity user
    ) {
        return noteMapper
                .toDto(noteService
                        .updateEntity(noteMapper.toEntity(noteId, noteInput), user.getUserId()));
    }

    @DeleteMapping("/{noteId}")
    @MutationMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@Argument @PathVariable Long noteId, @AuthenticationPrincipal UserEntity user) {
        noteService.deleteEntityByUser(noteId, user.getUserId());
    }

    @GetMapping("/findAll")
    @QueryMapping
    public List<NoteDTO> findAllNotes(@Argument Long pageNumber, @Argument Long pageSize) {
        return noteMapper.toDto(noteService.findAll(pageNumber, pageSize));
    }

}
