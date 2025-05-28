package com.web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "box_chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BoxChatEntity {
    @Column(name = "box_chat_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @OneToMany(mappedBy = "boxChatEntity")
    private Set<MessageEntity> messageEntities;
}
