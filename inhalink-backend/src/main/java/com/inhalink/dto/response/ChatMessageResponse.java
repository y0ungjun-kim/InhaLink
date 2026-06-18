package com.inhalink.dto.response;

import com.inhalink.domain.ChatMessage;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChatMessageResponse {
    private final Long id;
    private final String senderStudentId;
    private final String senderName;
    private final String content;
    private final LocalDateTime sentAt;

    public ChatMessageResponse(ChatMessage msg) {
        this.id = msg.getId();
        this.senderStudentId = msg.getSender().getStudentId();
        this.senderName = msg.getSender().getName();
        this.content = msg.getContent();
        this.sentAt = msg.getCreatedAt();
    }
}
