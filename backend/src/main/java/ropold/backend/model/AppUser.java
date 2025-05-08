package ropold.backend.model;

import java.util.List;
import java.util.Map;

public record AppUser(
        String id,
        String username,
        String name,
        String avatarUrl,
        String githubUrl,
        List<String> favoritePieceImages,
        Map<PieceImageEnum, String> customPieceImages
) {
}
