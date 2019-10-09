package app.miniprogram.socket.rate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author :wkh.
 * @date :2019/10/1.
 */
@ServerEndpoint("/rate-socket")
@Component
@Slf4j
public class WebSocketRate {

    private Session session;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。
     */
    private static CopyOnWriteArraySet<WebSocketRate> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        log.info("【websocket消息】 有新的连接，总数{}", webSocketSet.size());
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        log.info("【websocket消息】 连接断开，总数{}", webSocketSet.size());
    }

    /**
     * 接收客户端消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】 收到客户端发来的消息：{}", message);
    }

    @OnError
    public void onError(Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendAll(String message) {
        log.info(message);
        for (WebSocketRate item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException ignored) {
            }
        }
    }

    public static int size() {
        return webSocketSet.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WebSocketRate)) {
            return false;
        }
        WebSocketRate that = (WebSocketRate) o;
        return Objects.equals(session, that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session);
    }
}
