package ropold.backend.model;

public record PieceImageModel(
        String id,
        String name,
        PieceImageEnum pieceImageEnum,
        String description,
        boolean isActive,
        String githubId,
        String imageUrl
) {
}
