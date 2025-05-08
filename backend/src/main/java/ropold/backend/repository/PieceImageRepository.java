package ropold.backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ropold.backend.model.PieceImageModel;

public interface PieceImageRepository extends MongoRepository<PieceImageModel, String> {
}
