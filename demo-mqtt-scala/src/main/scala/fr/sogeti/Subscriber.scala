package fr.sogeti

import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken

class Subscriber(url : String) {
  private final val persistence = new MemoryPersistence
  private final val client : MqttClient = new MqttClient(url, MqttClient.generateClientId, persistence)
  
  def connect : Unit = {
    client.connect
  }
  
  def subscribe(topic : String) : Unit = {
    client.subscribe(topic)
  }
  
  private def callback = new MqttCallback {
    
    override def messageArrived(topic : String, message : MqttMessage): Unit = {
      println("Message arrived : %s".format( new String( message.getPayload) ))
    }
    
    override def connectionLost(cause : Throwable) : Unit = {
      println("Connction lost with error : %s".format(cause.getMessage))
    }
    
    override def deliveryComplete(token : IMqttDeliveryToken) : Unit = {
      println("delivery complete token : %s".format(token.getMessage))
    }
  }
  
  client.setCallback(callback)
}