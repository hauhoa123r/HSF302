package com.web.service;

import com.web.model.response.TrainerResponse;

import java.util.List;

public interface TrainerService {
    Long countTrainers();

    List<TrainerResponse> getTrainers();
}
