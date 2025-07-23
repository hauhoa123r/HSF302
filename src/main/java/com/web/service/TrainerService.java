package com.web.service;

import com.web.model.dto.TrainerDTO;
import com.web.model.response.TrainerResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TrainerService {
    Long countTrainers();

    TrainerResponse getTrainerById(Long id);

    List<TrainerResponse> getTrainers();

    Page<TrainerResponse> getTrainers(int page, int size, TrainerDTO trainerDTO);

    List<String> getAllSpecializations();

    void addTrainer(TrainerDTO trainerDTO);

    void updateTrainer(Long id, TrainerDTO trainerDTO);

    void deleteTrainer(Long id);
}
