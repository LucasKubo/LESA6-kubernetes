package MeuRemedio.app.service;

import com.google.firebase.messaging.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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
                .build();

        Message message = Message
                .builder()
                .setToken(token)
                //.setNotification(notification)
                .putData("title", title)
                .putData("body", body)
                .putData("icon", "https://i.imgur.com/dU2UDc4.png")
                .putData("badge", "https://i.imgur.com/czN0rck.png")
                .putData("click_action", "https://meuremedioapp.herokuapp.com")
                .build();
        firebaseMessaging.send(message);
    }
}
