importScripts(
  "https://www.gstatic.com/firebasejs/10.7.1/firebase-app-compat.js",
);
importScripts(
  "https://www.gstatic.com/firebasejs/10.7.1/firebase-messaging-compat.js",
);

const payload = {
  apiKey: "AIzaSyDEIZ0fWEtnHWSS8md2Z045nUODVp_SyEA",
  authDomain: "test-38252.firebaseapp.com",
  projectId: "test-38252",
  storageBucket: "test-38252.firebasestorage.app",
  messagingSenderId: "341750630566",
  appId: "1:341750630566:web:d980a7b07c3c105da8cc67",
};
firebase.initializeApp(payload);

const messaging = firebase.messaging();

console.log("Background message:", payload, messaging);

messaging.onBackgroundMessage((data) => {
  console.log("Notification:", data);
  self.registration.showNotification(data.notification.title, {
    body: data.notification.body,
    icon: "/icon.png",
  });
});
