package ropold.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ropold.backend.model.PieceImageModel;
import ropold.backend.model.PieceImageModelDto;
import ropold.backend.service.CloudinaryService;
import ropold.backend.service.PieceImageService;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/piece-image")
@RequiredArgsConstructor
public class PieceImageController {

    private final PieceImageService pieceImageService;
    private final CloudinaryService cloudinaryService;

    @GetMapping
    public List<PieceImageModel> getAllPieceImages() {
        return pieceImageService.getAllPieceImages();
    }

    @GetMapping("/active")
    public List<PieceImageModel> getActivePieceImages() {
        return pieceImageService.getActivePieceImages();
    }

    @GetMapping("/{id}")
    public PieceImageModel getPieceImageById(String id) {
        PieceImageModel pieceImage = pieceImageService.getPieceImageById(id);
        if (pieceImage == null) {
            throw new PieceImageNotFoundException("No PieceImage found with id: " + id);
        }
        return pieceImage;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public PieceImageModel addPieceImage(
            @RequestPart("pieceImageModelDto") @Valid PieceImageModelDto animalModelDto,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal OAuth2User authentication) throws IOException {

        String authenticatedUserId = authentication.getName();
        if (!authenticatedUserId.equals(animalModelDto.githubId())) {
            throw new AccessDeniedException("You do not have permission to add this animal.");
        }

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = cloudinaryService.uploadImage(image);
        }

        return pieceImageService.addPieceImage(
                new PieceImageModel(
                        null,
                        pieceImageModelDto.name(),
                        pieceImageModelDto.pieceImageEnum(),
                        pieceImageModelDto.description(),
                        pieceImageModelDto.isActive(),
                        pieceImageModelDto.githubId(),
                        imageUrl
                ));
    }


}
