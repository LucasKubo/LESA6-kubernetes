package MeuRemedio.app.service;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    //TODO usar Note como parametro
    public void sendNotification(String title, String body, String token) throws FirebaseMessagingException {

        AndroidNotification notification = AndroidNotification
                .builder()
                .setTitle(title)
                .setBody(body)
                .setIcon("https://i.imgur.com/dU2UDc4.png")
                .build();

        Message message = Message
                .builder()
                .setToken(token)
                .setAndroidConfig(AndroidConfig.builder().setNotification(notification).build())
//                .putData("title", title)
//                .putData("body", body)
//                .putData("icon", "https://i.imgur.com/dU2UDc4.png")
//                .putData("badge", "https://i.imgur.com/czN0rck.png")
                .build();
        firebaseMessaging.send(message);
    }
}
