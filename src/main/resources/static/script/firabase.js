
import { initializeApp } from "firebase/app";
import { getAnalytics } from "firebase/analytics";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
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
const analytics = getAnalytics(app);