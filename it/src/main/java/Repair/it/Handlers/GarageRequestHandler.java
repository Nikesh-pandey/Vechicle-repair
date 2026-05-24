//package Repair.it.Handlers;
//
//
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//import Repair.it.Entity.User;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class GarageRequestHandler extends TextWebSocketHandler {
//
//    private static final Map<String, WebSocketSession> onlineOperators =
//            new ConcurrentHashMap<>();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        User operator = (User) SecurityContextHolder
//                .getContext()
//                .getAuthentication()
//                .getPrincipal();
//
//        String operatorId = operator.getId().toString(); // or operator.getUsername()
//
//        onlineOperators.put(operatorId, session);
//
//        System.out.println("Operator " + operatorId + " is now ONLINE");
//        System.out.println("Total online operators: " + onlineOperators.size());
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        // Will implement later
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session,
//                                      org.springframework.web.socket.CloseStatus status) throws Exception {
//        // Will implement later
//    }
//}