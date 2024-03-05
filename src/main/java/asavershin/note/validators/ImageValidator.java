package asavershin.note.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ImageValidator implements ConstraintValidator<ImageType, MultipartFile> {

    @Override
    public void initialize(ImageType constraintAnnotation) {

    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {

        if(multipartFile == null || multipartFile.isEmpty()){
            return false;
        }

        boolean result = true;

        String contentType = multipartFile.getContentType();
        if (contentType == null || !isSupportedContentType(contentType)) {
            result = false;
        }

        return result;
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("image/png")
                || contentType.equals("image/jpg");
    }
}

