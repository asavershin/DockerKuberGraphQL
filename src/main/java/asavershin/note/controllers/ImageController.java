package asavershin.note.controllers;

import asavershin.note.dto.ImageByteResponse;
import asavershin.note.dto.ImageDTO;
import asavershin.note.exceptions.EntityNotFoundException;
import asavershin.note.exceptions.FileException;
import asavershin.note.services.ImageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/{noteId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadImage(
            @PathVariable final Long noteId,
            @ModelAttribute final ImageDTO image ){
        imageService.uploadFile(noteId, image.getImage());
    }

    @GetMapping("/links")
    public List<ImageByteResponse> downloadImages(@RequestBody List<String> links){
        return imageService.downloadImages(links);
    }

    @DeleteMapping("{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long imageId){
        imageService.deleteById(imageId);
    }
}
