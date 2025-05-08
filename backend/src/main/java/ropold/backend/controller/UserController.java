package ropold.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import ropold.backend.exception.AccessDeniedException;
import ropold.backend.model.PieceImageEnum;
import ropold.backend.model.PieceImageModel;
import ropold.backend.service.AppUserService;
import ropold.backend.service.PieceImageService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final PieceImageService pieceImageService;
    private final AppUserService appUserService;

    @GetMapping(value = "/me", produces = "text/plain")
    public String getMe() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("/me/details")
    public Map<String, Object> getUserDetails(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) {
            return Map.of("message", "User not authenticated");
        }
        return user.getAttributes();
    }

    @GetMapping("/favorites")
    public List<PieceImageModel> getUserFavorites(@AuthenticationPrincipal OAuth2User authentication) {
        List<String> favoritePieceImageIds = appUserService.getUserFavoritePieceImages(authentication.getName());
        return pieceImageService.getPieceImagesByIds(favoritePieceImageIds);
    }

    @GetMapping("/me/my-piece-images/{githubId}")
    public List<PieceImageModel> getPieceImagesForGithubUser(@PathVariable String githubId) {
        return pieceImageService.getPieceImagesForGithubUser(githubId);
    }

    @PostMapping("/favorites/{pieceImageId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPieceImageToFavorites(@PathVariable String pieceImageId, @AuthenticationPrincipal OAuth2User authentication) {
        String authenticatedUserId = authentication.getName();
        appUserService.addPieceImageToFavoritePieceImages(authenticatedUserId, pieceImageId);
    }

    @DeleteMapping("/favorites/{pieceImageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePieceImageFromFavorites(@PathVariable String pieceImageId, @AuthenticationPrincipal OAuth2User authentication) {
        String authenticatedUserId = authentication.getName();
        appUserService.removePieceImageFromFavoritePieceImages(authenticatedUserId, pieceImageId);
    }

    @PutMapping("/{id}/toggle-active")
    public PieceImageModel togglePieceImageActive(@PathVariable String id, @AuthenticationPrincipal OAuth2User authentication) {
        String authenticatedUserId = authentication.getName();

        PieceImageModel pieceImageModel = pieceImageService.getPieceImageById(id);
        if(!pieceImageModel.githubId().equals(authenticatedUserId)) {
            throw new AccessDeniedException("You are not allowed to change the active status of this piece image.");
        }
        return pieceImageService.togglePieceImageActive(id);
    }

    @GetMapping("/custom-piece-image")
    public Map<PieceImageEnum, String> getCustomPieceImages(@AuthenticationPrincipal OAuth2User authentication) {
        String authenticatedUserId = authentication.getName();
        return appUserService.getCustomPieceImages(authenticatedUserId);
    }

    @PostMapping("/custom-piece-image")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCustomPieceImage(@RequestBody Map<PieceImageEnum,String> customPieceImages, @AuthenticationPrincipal OAuth2User authentication) {
        String authenticatedUserId = authentication.getName();
        appUserService.saveCustomPieceImages(customPieceImages, authenticatedUserId);
    }
}
