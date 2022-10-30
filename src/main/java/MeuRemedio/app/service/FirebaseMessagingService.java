package MeuRemedio.app.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    //TODO usar Note como parametro
    public void sendNotification(String title, String body, String token) throws FirebaseMessagingException {

        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(body)
                .setImage("https://i.imgur.com/dU2UDc4.png")
                .build();

        Message message = Message
                .builder()
                .setToken(token)
                .setNotification(notification)
//              .putAllData(note.getData())
                .build();
        firebaseMessaging.send(message);
    }
}
