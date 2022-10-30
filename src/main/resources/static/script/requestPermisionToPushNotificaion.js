import { initializeApp } from 'https://www.gstatic.com/firebasejs/9.11.0/firebase-app.js';
import { getMessaging, getToken, onMessage } from 'https://www.gstatic.com/firebasejs/9.11.0/firebase-messaging.js';

const firebaseConfig = {
    apiKey: "AIzaSyBjpN-mkpDpaKOPJITaAGaYK6Y-Owv4a_c",
    authDomain: "meu-remedio-353002.firebaseapp.com",
    projectId: "meu-remedio-353002",
    storageBucket: "meu-remedio-353002.appspot.com",
    messagingSenderId: "980419248726",
    appId: "1:980419248726:web:63d12fb6b805c270ad31f7",
    measurementId: "G-E7DHR6TZ0B"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);

// Initialize Firebase Cloud Messaging and get a reference to the service
const messaging = getMessaging(app);

getToken(messaging, { vapidKey: 'BGQ114U_v6SYU0cFpx3bzDcNcDIy40jbd60PhYG6v5Qqj5nNXvaPvJECT4k-pr_bgcizaU-X9Eqt3BcNm-Qs9S4' })
    .then((currentToken) => {
        if (currentToken) {
            console.log(currentToken);
            sendTokenToServer(currentToken);
        } else {
            // Show permission request UI
            console.log('No registration token available. Request permission to generate one.');
            // ...
        }
    }).catch((err) => {
    console.log('An error occurred while retrieving token. ', err);
    // ...
});

onMessage(messaging, (payload) => {
    console.log('Message received', payload);
    alert(payload.notification.body)
//    const notification = new Notification(payload.title, {
//        body: payload.body
//      });
    // navigator.serviceWorker.getRegistration('/firebase-cloud-messaging-push-scope').then(registration => {
    //     registration.showNotification(
    //         payload.notification.title,
    //         payload.notification.body
    //     )
});
//TODO set this for Heroku path
const URL_RECIEVE_TOKEN = "https://meuremedioapp.herokuapp.com/getNotificationToken"

function sendTokenToServer(token){
    let xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "POST", URL_RECIEVE_TOKEN, true );
    xmlHttp.setRequestHeader("Content-Type","application/json");
    xmlHttp.send(token);
}
