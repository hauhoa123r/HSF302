package com.web.service.impl;

import java.sql.Timestamp;
import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.converter.MessageConverter;
import com.web.entity.MessageEntity;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.MessageDTO;
import com.web.model.response.MessageResponse;
import com.web.repository.MessageRepository;
import com.web.service.MessageService;
import com.web.utils.CheckFieldObject;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private MessageConverter messageConverter;
    private CheckFieldObject checkFieldObject;

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Autowired
    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Autowired
    public void setCheckFieldObject(CheckFieldObject checkFieldObject) {
        this.checkFieldObject = checkFieldObject;
    }

    @Override
    public MessageResponse sendMessage(MessageDTO messageDTO) {
        checkFieldObject.check(MessageDTO.class, messageDTO);

        if (messageDTO.getId() != null && messageRepository.existsById(messageDTO.getId())) {
            throw new EntityAlreadyExistException(MessageEntity.class);
        }

        MessageEntity messageEntity = messageConverter.toEntity(messageDTO);
        messageEntity.setSentAt(new Timestamp(System.currentTimeMillis()));

        messageEntity = messageRepository.save(messageEntity);

        return messageConverter.toResponse(messageEntity);
    }

    @Override
    public List<MessageResponse> getMessagesByBoxChatId(Long boxChatId) {
        return messageRepository.findByBoxChatEntityId(boxChatId).stream().map(messageConverter::toResponse).toList();
    }

    @Override
    public void deleteMessage(Long id) {
        if (!messageRepository.existsById(id)) {
            throw new EntityNotFoundException(MessageEntity.class);
        }

        messageRepository.deleteById(id);
    }
}
