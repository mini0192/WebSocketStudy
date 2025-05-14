package com.test;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketManager {

    private final SimpMessagingTemplate messagingTemplate;

    // /app/sendMessageType1 경로에 메시지 수신
    // 동적으로 /topic/test1에 메시지 전송
    @MessageMapping("/sendMessageType1")
    public void sendMessage1(GetDto dto) {
        System.out.println("[sendMessageType1] " + dto.getMessage());
        System.out.println("Send To [/topic/test1]");
        String topic = "/topic" + "/test1";
        messagingTemplate.convertAndSend(topic, dto);
    }

    // /app/sendMessageType2 경로에 메시지 수신
    // @SendTo 방식으로 /topic/test2에 메시지 전송
    @MessageMapping("/sendMessageType2")
    @SendTo("/topic/test2")
    public GetDto sendMessage2(GetDto dto) {
        System.out.println("[sendMessageType2] " + dto.getMessage());
        System.out.println("Send To [/topic/test2]");
        return dto;
    }
}
