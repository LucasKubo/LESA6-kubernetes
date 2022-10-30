package MeuRemedio.app.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    //TODO usar Note como parametro
    public void sendNotification(String title, String body, String token) throws FirebaseMessagingException {

//        Notification notification = Notification
//                .builder()
//                .setTitle(title)
//                .setBody(body)
//                .build();

        Map<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("body", body);


        Message message = Message
                .builder()
                .setToken(token)
                .putAllData(map)
                .build();
        firebaseMessaging.send(message);
    }
}
