package fr.sogeti

import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence
import org.eclipse.paho.client.mqttv3.MqttMessage

class Publisher(url : String) {

  println("publisher created")
  
  private final val perisistence = new MqttDefaultFilePersistence("/tmp")
  private final val client : MqttClient = new MqttClient(url, MqttClient.generateClientId, perisistence)
  
  def connect : Unit = {
    println("connecting client...")
    client.connect
    println("client connected.")
  }
  
  def disconnect : Unit = {
    client.disconnect
  }
  
  def publish(topicName : String, message : String) : Unit = {
    val topic = client.getTopic(topicName)
    val msg = new MqttMessage(message.getBytes("UTF-8"))
    topic.publish(msg)
    println("message : %s sent on topic : %s".format(topicName, message)) 
  }
}