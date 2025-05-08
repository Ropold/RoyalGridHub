package ropold.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ropold.backend.exception.PieceImageNotFoundException;
import ropold.backend.model.PieceImageModel;
import ropold.backend.repository.PieceImageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PieceImageService {

    private final IdService idService;
    private final PieceImageRepository pieceImageRepository;
    private final CloudinaryService cloudinaryService;


    public List<PieceImageModel> getAllPieceImages() {
    return pieceImageRepository.findAll();
    }

    public List<PieceImageModel> getActivePieceImages() {
        return pieceImageRepository.findAll().stream()
                .filter(PieceImageModel::isActive)
                .toList();
    }

    public PieceImageModel getPieceImageById(String id) {
        return pieceImageRepository.findById(id).orElseThrow(() -> new PieceImageNotFoundException("No Piece Image found with id: " + id));
    }

    public PieceImageModel addPieceImage(PieceImageModel pieceImageModel) {
        PieceImageModel newPieceImageModel = new PieceImageModel(
                idService.generateRandomId(),
                pieceImageModel.name(),
                pieceImageModel.pieceImageEnum(),
                pieceImageModel.description(),
                pieceImageModel.isActive(),
                pieceImageModel.githubId(),
                pieceImageModel.imageUrl()
        );
        return pieceImageRepository.save(newPieceImageModel);
    }

    public PieceImageModel updatePieceImage(String id, PieceImageModel pieceImageModel) {
        if(pieceImageRepository.existsById(id)){
            PieceImageModel updatedPieceImageModel = new PieceImageModel(
                    id,
                    pieceImageModel.name(),
                    pieceImageModel.pieceImageEnum(),
                    pieceImageModel.description(),
                    pieceImageModel.isActive(),
                    pieceImageModel.githubId(),
                    pieceImageModel.imageUrl()
            );
            return pieceImageRepository.save(updatedPieceImageModel);
        }
        throw new PieceImageNotFoundException("No PieceImage found with id: " + id);
    }

    public void deletePieceImage(String id) {
        PieceImageModel pieceImageModel = pieceImageRepository.findById(id).orElseThrow(() -> new PieceImageNotFoundException("No PieceImage found with id: " + id));

        if(pieceImageModel.imageUrl() != null) {
            cloudinaryService.deleteImage(pieceImageModel.imageUrl());
        }

        pieceImageRepository.deleteById(id);
    }

    public List<PieceImageModel> getPieceImagesByIds(List<String> favoritePieceImageIds) {
        return pieceImageRepository.findAllById(favoritePieceImageIds);
    }

    public PieceImageModel togglePieceImageActive(String id) {
        PieceImageModel pieceImageModel = pieceImageRepository.findById(id).orElseThrow(() -> new PieceImageNotFoundException("No PieceImage found with id: " + id));

        PieceImageModel updatedPieceImageModel = new PieceImageModel(
                pieceImageModel.id(),
                pieceImageModel.name(),
                pieceImageModel.pieceImageEnum(),
                pieceImageModel.description(),
                !pieceImageModel.isActive(),
                pieceImageModel.githubId(),
                pieceImageModel.imageUrl()
        );
        return pieceImageRepository.save(updatedPieceImageModel);
    }

    public List<PieceImageModel> getPieceImagesForGithubUser(String githubId) {
        return pieceImageRepository.findAll().stream()
                .filter(pieceImageModel -> pieceImageModel.githubId().equals(githubId))
                .toList();
    }
}
