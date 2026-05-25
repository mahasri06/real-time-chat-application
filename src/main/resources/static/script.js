const API_BASE = "/api";

let currentUser = JSON.parse(localStorage.getItem("chatUser"));
let activeRoom = null;

const authPanel = document.getElementById("authPanel");
const roomPanel = document.getElementById("roomPanel");
const statusMessage = document.getElementById("statusMessage");
const currentUserLabel = document.getElementById("currentUser");
const roomList = document.getElementById("roomList");
const messages = document.getElementById("messages");
const activeRoomName = document.getElementById("activeRoomName");
const memberCount = document.getElementById("memberCount");
const messageInput = document.getElementById("messageInput");
const sendButton = document.getElementById("sendButton");

document.getElementById("loginTab").addEventListener("click", () => switchAuthTab("login"));
document.getElementById("registerTab").addEventListener("click", () => switchAuthTab("register"));
document.getElementById("loginForm").addEventListener("submit", login);
document.getElementById("registerForm").addEventListener("submit", register);
document.getElementById("createRoomForm").addEventListener("submit", createRoom);
document.getElementById("messageForm").addEventListener("submit", sendMessage);
document.getElementById("refreshRooms").addEventListener("click", loadRooms);
document.getElementById("logoutButton").addEventListener("click", logout);

function switchAuthTab(tab) {
    document.getElementById("loginTab").classList.toggle("active", tab === "login");
    document.getElementById("registerTab").classList.toggle("active", tab === "register");
    document.getElementById("loginForm").classList.toggle("hidden", tab !== "login");
    document.getElementById("registerForm").classList.toggle("hidden", tab !== "register");
}

async function register(event) {
    event.preventDefault();

    const body = {
        fullName: document.getElementById("fullName").value,
        username: document.getElementById("registerUsername").value,
        email: document.getElementById("email").value,
        password: document.getElementById("registerPassword").value
    };

    const data = await request("/auth/register", "POST", body);
    if (data) {
        saveUser(data);
    }
}

async function login(event) {
    event.preventDefault();

    const body = {
        username: document.getElementById("loginUsername").value,
        password: document.getElementById("loginPassword").value
    };

    const data = await request("/auth/login", "POST", body);
    if (data) {
        saveUser(data);
    }
}

function saveUser(user) {
    currentUser = user;
    localStorage.setItem("chatUser", JSON.stringify(user));
    statusMessage.textContent = user.message;
    updateAuthState();
    loadRooms();
}

function logout() {
    currentUser = null;
    activeRoom = null;
    localStorage.removeItem("chatUser");
    updateAuthState();
    renderEmptyChat();
}

function updateAuthState() {
    const loggedIn = Boolean(currentUser);
    authPanel.classList.toggle("hidden", loggedIn);
    roomPanel.classList.toggle("hidden", !loggedIn);
    currentUserLabel.textContent = loggedIn ? currentUser.username : "";
}

async function createRoom(event) {
    event.preventDefault();

    const roomNameInput = document.getElementById("roomName");
    const data = await request("/rooms", "POST", {
        name: roomNameInput.value,
        description: "Room created from the ChatFlow UI",
        createdByUserId: currentUser.userId
    });

    if (data) {
        roomNameInput.value = "";
        await loadRooms();
        selectRoom(data);
    }
}

async function loadRooms() {
    if (!currentUser) {
        return;
    }

    const rooms = await request("/rooms", "GET");
    if (!rooms) {
        return;
    }

    roomList.innerHTML = "";
    rooms.forEach((room) => {
        const button = document.createElement("button");
        button.className = `room-item ${activeRoom?.id === room.id ? "active" : ""}`;
        button.textContent = room.name;

        const meta = document.createElement("span");
        meta.textContent = `${room.memberCount} members`;
        button.appendChild(meta);

        button.addEventListener("click", () => selectRoom(room));
        roomList.appendChild(button);
    });
}

async function selectRoom(room) {
    activeRoom = room;
    activeRoomName.textContent = room.name;
    memberCount.textContent = `${room.memberCount} members`;
    messageInput.disabled = false;
    sendButton.disabled = false;

    const joinedRoom = await request(`/rooms/${room.id}/join`, "POST", { userId: currentUser.userId });
    if (joinedRoom) {
        activeRoom = joinedRoom;
        memberCount.textContent = `${joinedRoom.memberCount} members`;
    }
    await loadRooms();
    await loadMessages();
}

async function loadMessages() {
    if (!activeRoom) {
        return;
    }

    const history = await request(`/rooms/${activeRoom.id}/messages`, "GET");
    if (!history) {
        return;
    }

    messages.innerHTML = "";
    if (history.length === 0) {
        messages.innerHTML = `<div class="empty-state"><h3>No messages yet</h3><p>Be the first one to say hello.</p></div>`;
        return;
    }

    history.forEach(renderMessage);
    messages.scrollTop = messages.scrollHeight;
}

async function sendMessage(event) {
    event.preventDefault();

    const content = messageInput.value.trim();
    if (!content || !activeRoom) {
        return;
    }

    const data = await request("/messages", "POST", {
        senderId: currentUser.userId,
        roomId: activeRoom.id,
        content
    });

    if (data) {
        messageInput.value = "";
        await loadMessages();
    }
}

function renderMessage(message) {
    const article = document.createElement("article");
    article.className = `message ${message.senderId === currentUser.userId ? "own" : ""}`;

    const sender = document.createElement("strong");
    sender.textContent = message.senderUsername;

    const content = document.createElement("p");
    content.textContent = message.content;

    const time = document.createElement("time");
    time.textContent = new Date(message.sentAt).toLocaleString();

    article.appendChild(sender);
    article.appendChild(content);
    article.appendChild(time);
    messages.appendChild(article);
}

function renderEmptyChat() {
    activeRoomName.textContent = "Select a room";
    memberCount.textContent = "0 members";
    messageInput.disabled = true;
    sendButton.disabled = true;
    messages.innerHTML = `<div class="empty-state"><h3>Welcome to ChatFlow</h3><p>Login, create or join a room, and start sending messages.</p></div>`;
}

async function request(path, method, body) {
    try {
        const options = {
            method,
            headers: { "Content-Type": "application/json" }
        };

        if (body) {
            options.body = JSON.stringify(body);
        }

        const response = await fetch(`${API_BASE}${path}`, options);
        const data = await response.json();

        if (!response.ok) {
            statusMessage.textContent = data.message || "Something went wrong.";
            return null;
        }

        statusMessage.textContent = data.message || "";
        return data;
    } catch (error) {
        statusMessage.textContent = "Could not connect to the backend.";
        return null;
    }
}

updateAuthState();
if (currentUser) {
    loadRooms();
}

setInterval(() => {
    if (currentUser && activeRoom) {
        loadMessages();
    }
}, 2500);
