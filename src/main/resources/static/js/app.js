let stompClient = null;
let accessToken = "";

function log(msg) {
    console.log(msg);
}

function login() {
    const identifier = document.getElementById("identifier").value;
    const password = document.getElementById("password").value;

    fetch("http://localhost:8080/e-messenger/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ identifier, password })
    })
        .then(res => res.json())
        .then(data => {
            accessToken = "Bearer " + data.result.accessToken;
            log("Logged in. Token: " + accessToken);
        })
        .catch(err => log("Login error: " + err));
}

function connect() {
    const socket = new SockJS("http://localhost:8080/e-messenger/ws");
    stompClient = Stomp.over(socket);

    const headers = {
        Authorization: accessToken
    };

    stompClient.connect(headers, function (frame) {
        log("Connected: " + frame);
        stompClient.subscribe("/user/queue/messages", function (message) {
            log("Received!");
            message = JSON.parse(message.body);
            console.log(message);
            showMessage(message.senderName + ": " + message.content);
        });
    }, function (error) {
        log("Connect error: " + error);
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect(() => log("Disconnected"));
    }
}

function sendMessage() {
    const type = document.querySelector("input[name='messageType']:checked").value;

    if (type === "TEXT") {
        const text = document.getElementById("messageInput").value;
        const payload = {
            content: text,
            type: "TEXT"
        };
        stompClient.send("/chat/1-2/send-message", {}, JSON.stringify(payload));
        showMessage("You: " + text);
    } else {
        const file = document.getElementById("fileInput").files[0];
        if (!file) {
            alert("Please choose a file.");
            return;
        }

        const reader = new FileReader();
        reader.onload = () => {
            const base64 = reader.result;
            const payload = {
                content: base64,
                type: type // "IMAGE" hoặc "AUDIO"
            };
            stompClient.send("/chat/1-2/send-message", {}, JSON.stringify(payload));
        };
        reader.readAsDataURL(file);
    }
}

function showMessage(message) {
    const ul = document.getElementById("messages");
    const li = document.createElement("li");
    li.appendChild(document.createTextNode(message));
    ul.appendChild(li);
}
