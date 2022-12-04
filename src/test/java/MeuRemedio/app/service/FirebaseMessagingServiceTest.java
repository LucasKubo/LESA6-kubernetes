package MeuRemedio.app.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FirebaseMessagingServiceTest {

    @InjectMocks
    FirebaseMessagingService firebaseMessagingService;

    @Mock
    private FirebaseMessaging firebaseMessaging;

    @Test
    public void deveEnviarNotificacaoFirebase() throws FirebaseMessagingException {
        Assertions.assertDoesNotThrow(() -> firebaseMessagingService.sendNotification("titulo", "corpo", "token"));
    }
}