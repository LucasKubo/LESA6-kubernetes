
importScripts('https://www.gstatic.com/firebasejs/9.2.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.2.0/firebase-messaging-compat.js');
// Initialize the Firebase app in the service worker by passing in
// your app's Firebase config object.
// https://firebase.google.com/docs/web/setup#config-object
firebase.initializeApp({
    apiKey: "AIzaSyBjpN-mkpDpaKOPJITaAGaYK6Y-Owv4a_c",
    authDomain: "meu-remedio-353002.firebaseapp.com",
    projectId: "meu-remedio-353002",
    storageBucket: "meu-remedio-353002.appspot.com",
    messagingSenderId: "980419248726",
    appId: "1:980419248726:web:63d12fb6b805c270ad31f7",
    measurementId: "G-E7DHR6TZ0B"
});
// Retrieve an instance of Firebase Messaging so that it can handle background
// messages.

const messaging = firebase.messaging();
const channel = new BroadcastChannel('sw-messages');
// If you would like to customize notifications that are received in the
// background (Web app is closed or not in browser focus) then you should
// implement this optional method.
// Keep in mind that FCM will still show notification messages automatically
// and you should use data messages for custom notifications.
// For more info see:
// https://firebase.google.com/docs/cloud-messaging/concept-options
messaging.onBackgroundMessage(function(payload) {
    console.log('[firebase-messaging-sw.js] Received background message ', payload);
    //Customize notification here
    const notificationTitle = payload.data.title;

    const notificationOptions = {
        body: payload.data.body,
        icon: payload.data.icon,
        badge: payload.data.badge,
        click_action: "https://meuremedioapp.herokuapp.com"
    };

    self.registration.showNotification(notificationTitle,
        notificationOptions);
    console.log("notificacao recebida");
});

function handleClick (event) {
    event.notification.close();
    // Open the url you set on notification.data
    clients.openWindow("https://meuremedioapp.herokuapp.com/")
  }

  self.addEventListener('notificationclick', handleClick);