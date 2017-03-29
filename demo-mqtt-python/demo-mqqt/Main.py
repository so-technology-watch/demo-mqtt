import paho.mqtt.client as mqtt

class Main:

	def __init__(self):
		self.client = mqtt.Client("client/python")
		self.client.on_connect = self.on_connect
		self.client.on_message = self.on_message
		self.connected = False
		print('connecting...')
		self.client.connect("10.226.159.205", 1883, 10)
		print('connected.')

	def on_connect(self, client, userdata, flags, rc):
		print("connect "+str(client))
		self.connected = True

	def on_message(self, client, userdata, msg):
		print("message "+str(msg.payload))
		
	def publish(self, topic, message):
		print('message envoyé "'+message+'" sur le topic : '+topic)
		self.client.publish("topic", message, 2, False)

	def subscribe(self, topic):
		self.client.subscribe(topic, 2)

	def loop(self):
		self.client.loop_forever()

	def test(self):
		self.client.subscribe(topic)
		self.client.publish(topic, "coucou 2")

if __name__ == '__main__':
	topic = "client/python"
	client = Main()

	client.test()
	client.loop()
