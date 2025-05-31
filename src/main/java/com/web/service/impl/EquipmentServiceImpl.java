package com.web.service.impl;

import com.web.converter.EquipmentConverter;
import com.web.entity.EquipmentEntity;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.EquipmentDTO;
import com.web.model.response.EquipmentResponse;
import com.web.repository.EquipmentRepository;
import com.web.service.EquipmentService;
import com.web.utils.CheckFieldObject;
import com.web.utils.MergeEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EquipmentServiceImpl implements EquipmentService {

    private EquipmentConverter equipmentConverter;
    private EquipmentRepository equipmentRepository;
    private MergeEntity<EquipmentEntity> mergeEntity;
    private CheckFieldObject checkFieldObject;

    @Autowired
    public void setEquipmentConverter(EquipmentConverter equipmentConverter) {
        this.equipmentConverter = equipmentConverter;
    }

    @Autowired
    public void setEquipmentRepository(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Autowired
    public void setMergeEntity(MergeEntity<EquipmentEntity> mergeEntity) {
        this.mergeEntity = mergeEntity;
    }

    @Autowired
    public void setCheckFieldObject(CheckFieldObject checkFieldObject) {
        this.checkFieldObject = checkFieldObject;
    }

    @Override
    public EquipmentResponse getEquipmentById(EquipmentDTO equipmentDTO) {
        checkFieldObject.check(EquipmentDTO.class, equipmentDTO, "id");

        Optional<EquipmentEntity> equipmentEntityOptional = equipmentRepository.findById(equipmentDTO.getId());

        return equipmentConverter.toResponse(
                equipmentEntityOptional.orElseThrow(() -> new EntityNotFoundException(EquipmentEntity.class)));
    }

    @Override
    public List<EquipmentResponse> getAllEquipments() {
        List<EquipmentEntity> equipmentEntities = equipmentRepository.findAll();

        return equipmentEntities.stream().map(equipmentConverter::toResponse).toList();
    }

    @Override
    public List<EquipmentResponse> getEquipmentsByName(EquipmentDTO equipmentDTO) {
        checkFieldObject.check(EquipmentDTO.class, equipmentDTO, "name");

        List<EquipmentEntity> equipmentEntities = equipmentRepository
                .findByNameContainingIgnoreCase(equipmentDTO.getName());

        if (equipmentEntities.isEmpty()) {
            throw new EntityNotFoundException(EquipmentEntity.class);
        }

        return equipmentEntities.stream().map(equipmentConverter::toResponse).toList();
    }

    @Override
    public EquipmentResponse createEquipment(EquipmentDTO equipmentDTO) {
        checkFieldObject.check(EquipmentDTO.class, equipmentDTO, "name");

        if (equipmentDTO.getId() != null && equipmentRepository.existsById(equipmentDTO.getId())) {
            throw new EntityAlreadyExistException(EquipmentEntity.class);
        }

        EquipmentEntity equipmentEntity = equipmentConverter.toEntity(equipmentDTO);
        equipmentEntity = equipmentRepository.save(equipmentEntity);

        return equipmentConverter.toResponse(equipmentEntity);
    }

    @Override
    public EquipmentResponse updateEquipment(EquipmentDTO equipmentDTO) {
        checkFieldObject.check(EquipmentDTO.class, equipmentDTO, "id");

        EquipmentEntity oldEquipmentEntity = equipmentRepository.findById(equipmentDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException(EquipmentEntity.class));
        EquipmentEntity newEquipmentEntity = equipmentConverter.toEntity(equipmentDTO);
        EquipmentEntity mergedEquipmentEntity = mergeEntity.merge(newEquipmentEntity, oldEquipmentEntity);
        newEquipmentEntity = equipmentRepository.save(mergedEquipmentEntity);

        return equipmentConverter.toResponse(newEquipmentEntity);
    }

    @Override
    public void deleteEquipment(EquipmentDTO equipmentDTO) {
        checkFieldObject.check(EquipmentDTO.class, equipmentDTO, "id");

        if (!equipmentRepository.existsById(equipmentDTO.getId())) {
            throw new EntityNotFoundException(EquipmentEntity.class);
        }

        equipmentRepository.deleteById(equipmentDTO.getId());
    }
}
