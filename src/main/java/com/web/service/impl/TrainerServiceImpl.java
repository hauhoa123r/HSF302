package com.web.service.impl;

import com.web.converter.TrainerConverter;
import com.web.model.response.TrainerResponse;
import com.web.repository.TrainerRepository;
import com.web.service.TrainerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TrainerServiceImpl implements TrainerService {
    private TrainerRepository trainerRepository;
    private TrainerConverter trainerConverter;

    @Autowired
    public TrainerServiceImpl(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Autowired
    public void setTrainerRepository(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Autowired
    public void setTrainerConverter(TrainerConverter trainerConverter) {
        this.trainerConverter = trainerConverter;
    }

    @Override
    public Long countTrainers() {
        return trainerRepository.count();
    }

    @Override
    public List<TrainerResponse> getTrainers() {
        return trainerRepository.findAllByStatus("ACTIVE")
                .stream()
                .map(trainerConverter::toResponse)
                .toList();
    }
}
