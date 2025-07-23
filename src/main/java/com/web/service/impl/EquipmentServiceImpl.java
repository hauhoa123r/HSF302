package com.web.service.impl;

import com.web.converter.EquipmentConverter;
import com.web.entity.ClassEntity;
import com.web.entity.EquipmentEntity;
import com.web.entity.SupplierEntity;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.EquipmentDTO;
import com.web.model.response.EquipmentResponse;
import com.web.repository.EquipmentRepository;
import com.web.repository.SupplierRepository;
import com.web.service.EquipmentService;
import com.web.utils.CheckFieldObject;
import com.web.utils.MergeObjectUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    private SupplierRepository supplierRepository;
    private EquipmentConverter equipmentConverter;
    private EquipmentRepository equipmentRepository;
    private MergeObjectUtils mergeObjectUtils;
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
    public void setMergeEntity(MergeObjectUtils mergeObjectUtils) {
        this.mergeObjectUtils = mergeObjectUtils;
    }

    @Autowired
    public void setCheckFieldObject(CheckFieldObject checkFieldObject) {
        this.checkFieldObject = checkFieldObject;
    }

    @Override
    public Page<EquipmentDTO> getAllEquipments(String name, Pageable pageable) {
        Page<EquipmentEntity> page;
        if (name != null && !name.isBlank()) {
            page = equipmentRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            page = equipmentRepository.findAll(pageable);
        }
        return page.map(equipmentConverter::toDTO);
    }

    @Override
    public String addEquipment(EquipmentDTO equipmentDTO) {
        SupplierEntity supplier = supplierRepository.findByEmail(equipmentDTO.getEmail())
                .orElseGet(() -> {
                    SupplierEntity newSupplier = equipmentConverter.toSupplierEntity(equipmentDTO);
                    return supplierRepository.save(newSupplier);
                });
        EquipmentEntity equipment = equipmentConverter.toEntity(equipmentDTO, supplier);
        equipmentRepository.save(equipment);
        return "Add Successfully";
    }

    @Override
    public String deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
        return "Deleted Successfully";
    }

//    @Override
//    public EquipmentResponse getEquipmentById(Long id) {
//        Optional<EquipmentEntity> equipmentEntityOptional = equipmentRepository.findById(id);
//
//        return equipmentConverter.toResponse(
//                equipmentEntityOptional.orElseThrow(() -> new EntityNotFoundException(EquipmentEntity.class)));
//    }
//
//    @Override
//    public List<EquipmentResponse> getAllEquipments() {
//        List<EquipmentEntity> equipmentEntities = equipmentRepository.findAll();
//
//        return equipmentEntities.stream().map(equipmentConverter::toResponse).toList();
//    }
//
//    @Override
//    public List<EquipmentResponse> getEquipmentsByName(String name) {
//        List<EquipmentEntity> equipmentEntities = equipmentRepository
//                .findByNameContainingIgnoreCase(name);
//
//        if (equipmentEntities.isEmpty()) {
//            throw new EntityNotFoundException(EquipmentEntity.class);
//        }
//
//        return equipmentEntities.stream().map(equipmentConverter::toResponse).toList();
//    }
//
//    @Override
//    public EquipmentResponse createEquipment(EquipmentDTO equipmentDTO) {
//        checkFieldObject.check(EquipmentDTO.class, equipmentDTO, "name");
//
//        if (equipmentDTO.getId() != null && equipmentRepository.existsById(equipmentDTO.getId())) {
//            throw new EntityAlreadyExistException(EquipmentEntity.class);
//        }
//
//        EquipmentEntity equipmentEntity = equipmentConverter.toEntity(equipmentDTO);
//        equipmentEntity = equipmentRepository.save(equipmentEntity);
//
//        return equipmentConverter.toResponse(equipmentEntity);
//    }
//
//    @Override
//    public EquipmentResponse updateEquipment(Long id, EquipmentDTO equipmentDTO) {
//        EquipmentEntity oldEquipmentEntity = equipmentRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException(EquipmentEntity.class));
//        EquipmentEntity newEquipmentEntity = equipmentConverter.toEntity(equipmentDTO);
//        mergeObjectUtils.mergeNonNullFields(newEquipmentEntity, oldEquipmentEntity);
//        newEquipmentEntity = equipmentRepository.save(oldEquipmentEntity);
//
//        return equipmentConverter.toResponse(newEquipmentEntity);
//    }
//
//    @Override
//    public void deleteEquipment(Long id) {
//        if (!equipmentRepository.existsById(id)) {
//            throw new EntityNotFoundException(EquipmentEntity.class);
//        }
//
//        equipmentRepository.deleteById(id);
//    }
}
