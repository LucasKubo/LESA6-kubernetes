'use strict';

import firebase from "firebase/compat";

importScripts("https://www.gstatic.com/firebasejs/8.2.4/firebase-app.js");
importScripts("https://www.gstatic.com/firebasejs/8.2.4/firebase-messaging.js");

const FIREBASE_CONFIG = {
    apiKey: "AIzaSyBjpN-mkpDpaKOPJITaAGaYK6Y-Owv4a_c",
    authDomain: "meu-remedio-353002.firebaseapp.com",
    projectId: "meu-remedio-353002",
    storageBucket: "meu-remedio-353002.appspot.com",
    messagingSenderId: "980419248726",
    appId: "1:980419248726:web:63d12fb6b805c270ad31f7",
    measurementId: "G-E7DHR6TZ0B"
};

// Initialize the firebase in the service worker.
firebase.initializeApp(FIREBASE_CONFIG);
const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
    console.log('[firebase-messaging-sw.js] Received background message ', payload);
    const notificationTitle = payload.notification.title;
    const notificationOptions = {
        body: payload.notification.body,
    };
    return self.registration.showNotification(notificationTitle,
        notificationOptions);
});
self.addEventListener('notificationclick', event => {
    console.log(event)
});

// self.addEventListener('push', function (event) {
//     var data = event.data.json();
//
//     const title = data.Title;
//     data.Data.actions = data.Actions;
//     const options = {
//         body: data.Message,
//         data: data.Data
//     };
//     event.waitUntil(self.registration.showNotification(title, options));
// });
//
// self.addEventListener('notificationclick', function (event) {});
//
// self.addEventListener('notificationclose', function (event) {});