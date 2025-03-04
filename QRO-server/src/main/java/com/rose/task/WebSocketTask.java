package com.rose.task;

import com.rose.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class WebSocketTask {
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 通过WebSocket每隔5秒向客户端发送消息
     */
    @Scheduled(cron = "0 0 * * * ?  ")
    public void sendMessageToClient() {
        String message = "这是来自服务端的消息：" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now());
        String jsonMessage = "{\"message\": \"" + message + "\"}"; // 将消息包装成 JSON 格式
        webSocketServer.sendToAllClient(jsonMessage);
    }
}
