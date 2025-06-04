package com.e_messenger.code.config.websocket;

import com.e_messenger.code.service.impl.ActiveStatusService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketEventListener {
    ActiveStatusService statusService;

    @EventListener
    public void listenWebSocketConnectedEvent(SessionConnectedEvent event){
        String userId = event.getUser().getName();
        statusService.setActiveStatus(userId, true);
    }

    @EventListener
    public void listenWebSocketDisconnectEvent(SessionDisconnectEvent event){
        String userId = event.getUser().getName();
        statusService.setActiveStatus(userId, false);
    }
}
