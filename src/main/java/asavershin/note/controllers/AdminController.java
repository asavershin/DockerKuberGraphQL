package asavershin.note.controllers;

import asavershin.note.services.NoteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Tag(name = "admin", description = "Работадля админа")
public class AdminController {
    private final NoteService noteService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    //Just test
    public String hello() {
        return "HELLO ADMIN";
    }

    @DeleteMapping("/note/{noteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNoteById(@PathVariable Long noteId){
        noteService.deleteEntityByAdmin(noteId);
    }
}
