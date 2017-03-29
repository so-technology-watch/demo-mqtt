package fr.sogeti



object Main extends App {
    val brokerUrl = "tcp://10.226.159.205:1883"
    val topic = "client/scala"
    val publisher = new Publisher(brokerUrl)
    publisher.connect
    
    publisher.publish(topic, "coucou from scala")
    
    val subscriber = new Subscriber(brokerUrl)
    subscriber.connect
    subscriber.subscribe(topic)
}