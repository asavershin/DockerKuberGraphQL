package asavershin.note.controllers;

import asavershin.note.dto.ImageByteRequest;
import asavershin.note.dto.ImageByteResponse;
import asavershin.note.services.ImageService;
import asavershin.note.validators.ImageType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Validated
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/{noteId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadImage(
            @PathVariable final Long noteId,
            @Validated @RequestPart("image") @ImageType final MultipartFile image ){
        imageService.uploadFile(noteId, image);
    }

    @GetMapping("/links")
    public List<ImageByteResponse> downloadImages(@RequestBody ImageByteRequest request){
        return imageService.downloadImages(request.getLinks());
    }

    @DeleteMapping("{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long imageId){
        imageService.deleteById(imageId);
    }
}
