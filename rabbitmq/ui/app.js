const Stomp = require('stompjs');

function subscribeToEvents() {
    let ws = new WebSocket('ws://localhost:15674/ws');
    const client = Stomp.over(ws);
    client.connect('guest', 'guest', () => onConnect(client), onError, '/');
}

function onConnect(client) {
    console.log("connected");
    subscribe(client);
}

function subscribe(client) {
    subscribeToNamedQueue(client);
    subscribeToFanoutExchange(client);
    subscribeToDirectExchange(client);
    subscribeToTopicExchange(client);
}

function subscribeToNamedQueue(client) {
    client.subscribe("/queue/QUEUE_2", handle);
}

function subscribeToFanoutExchange(client) {
    client.subscribe("/exchange/FANOUT_EXCHANGE", handle);
}

function subscribeToDirectExchange(client) {
    let routingKey = "BLACK";
    client.subscribe("/exchange/DIRECT_EXCHANGE/" + routingKey, handle);
}

function subscribeToTopicExchange(client) {
    let routingKey = "lazy.*.*";
    client.subscribe("/exchange/TOPIC_EXCHANGE/" + routingKey, handle);
}

function onError() {
    console.log('error');
}

function handle(event) {
    console.log(event)
}

subscribeToEvents();