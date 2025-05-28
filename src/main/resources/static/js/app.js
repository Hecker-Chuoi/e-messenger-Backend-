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

        stompClient.subscribe("/user/messages", function (message) {
            // tin nhắn trong cuộc hội thoại
            log("Received!");
            message = JSON.parse(message.body);
            console.log(message);
            showMessage(message.actorName + ": " + message.content);
        });

        stompClient.subscribe("/user/conversations", function (message) {
            // những thay đổi, cập nhật trong cuộc hội thoại
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

async function sendMessage() {
    const type = document.querySelector("input[name='messageType']:checked").value;

    if (type === "TEXT") {
        const text = document.getElementById("messageInput").value;
        const payload = {
            text: text
        };
        stompClient.send("/chat/1-2/send-text", {}, JSON.stringify(payload));
    } else {
        const file = document.getElementById("fileInput").files[0];
        if (!file) {
            alert("Please choose a file.");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await fetch("http://localhost:8080/e-messenger/files", {
                method: "POST",
                headers: {
                    "Authorization": accessToken
                },
                body: formData
            });

            if (!response.ok) {
                alert("Upload failed");
                return;
            }

            const abc = await response.text();

            stompClient.send(
                "/chat/1-2/send-media",
                {},
                JSON.stringify({
                    mediaType: type.toUpperCase(),
                    uploadedUrl: abc
                })
            );
        } catch (err) {
            console.error("Lỗi xảy ra trong fetch:", err);
            alert("Có lỗi khi upload");
        }
    }
}

function showMessage(message) {
    const ul = document.getElementById("messages");
    const li = document.createElement("li");
    li.appendChild(document.createTextNode(message));
    ul.appendChild(li);
}
