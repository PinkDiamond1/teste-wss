package br.com.zpay.testewss;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlphapointWS {

    private static final String ALPHAPOINT_ENDPOINT = "wss://apifoxbitprodlb.alphapoint.com/WSGateway";

    private static final String USER = "";

    private static final String PASSWORD = "";

    private WebSocketSession session;

    @Autowired
    private AlphapointWebSocketHandler sessionHandler;

    @PostConstruct
    public void initialize() throws InterruptedException, ExecutionException {
        WebSocketClient client = new StandardWebSocketClient();
        session = client.doHandshake(sessionHandler, ALPHAPOINT_ENDPOINT).get();
    }

    public void send() throws IOException {
        JSONObject payload = new JSONObject();
        payload.put("OMSId", 1);
        payload.put("Symbol", "BTCBRL");

        execute(payload, "SubscribeLevel1");
    }

    public void authenticate() throws IOException {
        JSONObject payload = new JSONObject();
        payload.put("Username", USER);
        payload.put("Password", PASSWORD);

        execute(payload, "WebAuthenticateUser");
    }

    private void execute(JSONObject payload, String function) throws IOException {
        JSONObject request = AlphapointWS.buildMessageFrame(0, 0, function, payload);
        log.info("Sending: " + request.toString());
        session.sendMessage(new TextMessage(request.toString()));
    }

    @PreDestroy
    public void shutdown() throws IOException {
        session.close();
        log.info("send:END");
    }

    public static JSONObject buildMessageFrame(int callType, long sequence, String functionName, JSONObject payload) {
        JSONObject messageFrame = new JSONObject();
        messageFrame.put("m", callType);
        messageFrame.put("i", sequence);
        messageFrame.put("n", functionName);
        messageFrame.put("o", payload.toString());
        return messageFrame;
    }

}
