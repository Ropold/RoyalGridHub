package ropold.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ropold.backend.repository.PieceImageRepository;

@Service
@RequiredArgsConstructor
public class PieceImageService {

    private final IdService idService;
    private final PieceImageRepository pieceImageRepository;

}
