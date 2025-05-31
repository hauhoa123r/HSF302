package com.web.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MessageEntity {
    @Column(name = "message_id")
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "box_chat_id")
    private BoxChatEntity boxChatEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private MemberEntity memberEntity;

    @Column(name = "content")
    private String content;

    @Column(name = "sent_at")
    private Timestamp sentAt;
}
