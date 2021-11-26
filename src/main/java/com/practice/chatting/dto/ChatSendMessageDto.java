package com.practice.chatting.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatSendMessageDto {
    private String type;
    private String sessionId;
    private String content;

    @Builder
    public ChatSendMessageDto(String type, String sessionId, String content) {
        this.type = type;
        this.sessionId = sessionId;
        this.content = content;
    }
}
