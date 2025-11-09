// app.js
let stompClient = null;
let username = null;
const API_BASE = "http://192.168.100.6:9091"; // backend Spring Boot
const socketEndpoint = API_BASE + "/ws-chat";
const socket = new SockJS(socketEndpoint);
const appDestination = "/app/chat.sendMessage";
const addUserDestination = "/app/chat.addUser";

const connectBtn = document.getElementById('connectBtn');
const logoutBtn = document.getElementById('logoutBtn');
const sendBtn = document.getElementById('sendBtn');
const clearBtn = document.getElementById('clearBtn');

connectBtn.addEventListener('click', connect);
sendBtn.addEventListener('click', sendMessage);
logoutBtn.addEventListener('click', disconnect);
clearBtn.addEventListener('click', clearChat);
document.getElementById('messageInput').addEventListener('keyup', (e)=> { if(e.key==='Enter') sendMessage(); });

function connect() {
  username = document.getElementById('username').value.trim();
  if(!username) return alert('Ingresa un nombre');

  const socket = new SockJS(socketEndpoint);
  stompClient = Stomp.over(socket);
  // opcional: quitar debug console
  stompClient.debug = null;

  stompClient.connect({}, function(frame) {
    document.getElementById('login-panel').classList.add('hidden');
    document.getElementById('chat-container').classList.remove('hidden');

    // suscribirse a mensajes públicos
    stompClient.subscribe('/topic/public', function(msg) {
      const payload = JSON.parse(msg.body);
      showMessage(payload);
      if(payload.type === 'JOIN' || payload.type === 'LEAVE') {
        refreshOnlineUsers();
      }
    });

    // suscribirse a lista de usuarios online
    stompClient.subscribe('/topic/onlineUsers', function(message){
      const users = JSON.parse(message.body);
      renderUsers(users);
    });

    // avisar al servidor que este usuario se unió
    const joinMsg = { type: 'JOIN', sender: username, content: '', timestamp: new Date().toISOString() };
    stompClient.send(addUserDestination, {}, JSON.stringify(joinMsg));
  }, function(error) {
    console.error('STOMP error', error);
    alert('Error al conectar: ' + error);
  });
}

function sendMessage() {
  const content = document.getElementById('messageInput').value.trim();
  if (!content || !stompClient) return;
  const chatMessage = {
    type: 'CHAT',
    sender: username,
    content: content,
    timestamp: new Date().toISOString()
  };
  stompClient.send(appDestination, {}, JSON.stringify(chatMessage));
  document.getElementById('messageInput').value = '';
}

function showMessage(payload) {
  const area = document.getElementById('messageArea');
  const div = document.createElement('div');
  div.className = 'message';

  const time = new Date(payload.timestamp).toLocaleTimeString();
  if (payload.type === 'JOIN') {
    div.innerHTML = `<em>${payload.sender} se ha conectado (${time})</em>`;
  } else if (payload.type === 'LEAVE') {
    div.innerHTML = `<em>${payload.sender} se ha desconectado (${time})</em>`;
  } else {
    div.innerHTML = `<strong>${payload.sender}</strong> <small>${time}</small><div>${payload.content}</div>`;
  }
  area.appendChild(div);
  area.scrollTop = area.scrollHeight;
}

function refreshOnlineUsers() {
  // pedir al backend la lista actual via REST
  fetch(API_BASE + '/api/chat/online')
    .then(res => res.json())
    .then(users => renderUsers(users))
    .catch(err => console.log('No se pudo cargar usuarios online', err));
}

function renderUsers(users) {
  const ul = document.getElementById('usersList');
  ul.innerHTML = '';
  if (!users) return;
  // users puede ser array o set, normalizar
  const arr = Array.isArray(users) ? users : Object.values(users || {});
  arr.forEach(u => {
    const li = document.createElement('li');
    li.textContent = u;
    ul.appendChild(li);
  });
}

function disconnect() {
  if (stompClient) {
    const leaveMsg = { type: 'LEAVE', sender: username, content: '', timestamp: new Date().toISOString() };
    stompClient.send(appDestination, {}, JSON.stringify(leaveMsg));
    stompClient.disconnect(() => {
      document.getElementById('chat-container').classList.add('hidden');
      document.getElementById('login-panel').classList.remove('hidden');
      username = null;
      stompClient = null;
      document.getElementById('messageArea').innerHTML = '';
      document.getElementById('usersList').innerHTML = '';
    });
  }
}

function clearChat() {
  document.getElementById('messageArea').innerHTML = '';
}
