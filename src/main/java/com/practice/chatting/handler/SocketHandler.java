package com.practice.chatting.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.chatting.dto.ChatSendMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class SocketHandler extends TextWebSocketHandler {

    //웹소켓 세션을 담아둘 맵
    HashMap<String, WebSocketSession> sessionMap = new HashMap<>();

    //jackson lib의 경우 spring-boot-web에 기본적으로 포함되어 있다.
    private final ObjectMapper objectMapper;

    //메시지 발송
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        ChatSendMessageDto chatSendMessageDto = objectMapper.readValue(message.getPayload(), ChatSendMessageDto.class);

        for(String key : sessionMap.keySet()) {
            WebSocketSession wss = sessionMap.get(key);

            wss.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatSendMessageDto)));
        }
    }

    //소켓 연결
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessionMap.put(session.getId(), session);

        ChatSendMessageDto chatSendMessageDto = new ChatSendMessageDto("established", session.getId(),"");

        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatSendMessageDto));

        session.sendMessage(textMessage);
    }

    //소켓 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionMap.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }
}
