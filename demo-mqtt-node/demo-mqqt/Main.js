var mqtt = require('mqtt');
var client  = mqtt.connect('tcp://10.226.159.205:1883')

client.on('connect', function () {
	console.log('connected');
	client.subscribe('client/node');
	client.publish('client/node', 'Hello mqtt from node');
})

client.on('message', function (topic, message) {
	// message is Buffer 
	console.log('message received : ', message.toString());
	// close the client
})
