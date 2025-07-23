package com.web.api;

import com.web.model.dto.TrainerDTO;
import com.web.model.response.TrainerResponse;
import com.web.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class TrainerAPI {
    private TrainerService trainerService;

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping("/api/admin/trainer/page/{pageIndex}")
    public Map<String, Object> getAllTrainersForAdmin(@PathVariable int pageIndex, @ModelAttribute TrainerDTO trainerDTO) {
        Page<TrainerResponse> trainerResponsePage = trainerService.getTrainers(pageIndex, 6, trainerDTO);
        return Map.of(
                "totalPages", trainerResponsePage.getTotalPages(),
                "currentPages", trainerResponsePage.getTotalElements(),
                "items", trainerResponsePage.getContent()
        );
    }

    @PostMapping("/api/admin/trainer")
    public void addTrainer(@RequestBody TrainerDTO trainerDTO) {
        trainerService.addTrainer(trainerDTO);
    }

    @PutMapping("/api/admin/trainer/{id}")
    public void updateTrainer(@PathVariable Long id, @RequestBody TrainerDTO trainerDTO) {
        trainerService.updateTrainer(id, trainerDTO);
    }
}
