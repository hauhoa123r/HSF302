package com.web.service.impl;

import com.web.converter.MessageConverter;
import com.web.entity.MessageEntity;
import com.web.exception.mapping.ErrorMappingException;
import com.web.exception.sql.EntityAlreadyExistException;
import com.web.exception.sql.EntityNotFoundException;
import com.web.model.dto.MessageDTO;
import com.web.model.response.MessageResponse;
import com.web.repository.MessageRepository;
import com.web.service.MessageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private MessageConverter messageConverter;

    @Autowired
    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Autowired
    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Override
    public MessageResponse sendMessage(MessageDTO messageDTO) {

        if (messageDTO.getId() != null && messageRepository.findById(messageDTO.getId()).isPresent()) {
            throw new EntityAlreadyExistException(MessageEntity.class);
        }

        Optional<MessageEntity> messageEntityOptional = Optional.ofNullable(messageConverter.toEntity(messageDTO));
        if (messageEntityOptional.isEmpty()) {
            throw new ErrorMappingException(MessageDTO.class, MessageEntity.class);
        }

        MessageEntity messageEntity = messageEntityOptional.get();
        messageEntity.setSentAt(new Timestamp(System.currentTimeMillis()));

        messageEntity = messageRepository.save(messageEntity);

        return messageConverter.toResponse(messageEntity);
    }

    @Override
    public List<MessageResponse> getMessagesByBoxChatId(MessageDTO messageDTO) {
        Optional<MessageEntity> messageEntityOptional = Optional.ofNullable(messageConverter.toEntity(messageDTO));
        if (messageEntityOptional.isEmpty()) {
            throw new ErrorMappingException(MessageDTO.class, MessageEntity.class);
        }

        return messageRepository.findByBoxChatId(messageEntityOptional.get().getBoxChat().getId()).stream().map(messageConverter::toResponse).toList();
    }

    @Override
    public void deleteMessage(MessageDTO messageDTO) {
        Optional<MessageEntity> messageEntityOptional = Optional.ofNullable(messageConverter.toEntity(messageDTO));
        if (messageEntityOptional.isEmpty()) {
            throw new ErrorMappingException(MessageDTO.class, MessageEntity.class);
        }

        if (!messageRepository.existsById(messageEntityOptional.get().getId())) {
            throw new EntityNotFoundException(MessageEntity.class);
        }

        messageRepository.delete(messageEntityOptional.get());
    }
}
