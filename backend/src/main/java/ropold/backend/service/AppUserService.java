package ropold.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ropold.backend.model.AppUser;
import ropold.backend.model.PieceImageEnum;
import ropold.backend.repository.AppUserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUser getUserById(String userId) {
        return appUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<String> getUserFavoritePieceImages(String userId) {
        AppUser user = getUserById(userId);
        return user.favoritePieceImages();
    }

    public void addPieceImageToFavoritePieceImages(String userId, String pieceImageId) {
        AppUser user = getUserById(userId);

        if (!user.favoritePieceImages().contains(pieceImageId)) {
            user.favoritePieceImages().add(pieceImageId);
            appUserRepository.save(user);
        }
    }

    public void removePieceImageFromFavoritePieceImages(String userId, String pieceImageId) {
        AppUser user = getUserById(userId);
        if (user.favoritePieceImages().contains(pieceImageId)) {
            user.favoritePieceImages().remove(pieceImageId);
            appUserRepository.save(user);
        }
    }

    public Map<PieceImageEnum, String> getCustomPieceImages(String userId) {
        AppUser user = getUserById(userId);
        return user.customPieceImages();
    }

    public void saveCustomPieceImages(Map<PieceImageEnum, String> customPieceImages, String userId) {
        AppUser user = getUserById(userId);
        Map<PieceImageEnum,String> updatedMapping = new HashMap<>(user.customPieceImages());
        updatedMapping.clear(); //??
        updatedMapping.putAll(customPieceImages);

        AppUser updatedUser = new AppUser(
                user.id(),
                user.username(),
                user.name(),
                user.avatarUrl(),
                user.githubUrl(),
                user.favoritePieceImages(),
                updatedMapping
        );
        appUserRepository.save(updatedUser);
    }

}
