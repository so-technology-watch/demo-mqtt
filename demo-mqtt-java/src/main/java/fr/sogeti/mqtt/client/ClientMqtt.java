/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.sogeti.mqtt.client;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author fduneau
 */
public class ClientMqtt {

    private String brokerUrl;
    private final MemoryPersistence persistence = new MemoryPersistence();
    private static final Logger LOG = Logger.getLogger(ClientMqtt.class.getName());
    private final MqttClient client;

    public ClientMqtt(String brokerUrl, String clientId) throws MqttException {
        this.brokerUrl = brokerUrl;
    	this.client = new MqttClient(brokerUrl, clientId, persistence);
    }

    public void sendMessage(String message, int qos, String topic) throws MqttException {
        MqttMessage msg = new MqttMessage(message.getBytes());
        msg.setQos(qos);
        client.publish(topic, msg);
        LOG.log(Level.INFO, "A message has been sent");
    }

    public void connect() {
        try {
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);
            if (!client.isConnected()) {
                client.connect();
                if (LOG.isLoggable(Level.INFO)) {
                    LOG.log(Level.INFO, "Client connected {0}", client.getClientId());
                    LOG.log(Level.INFO, "Connected to {0}", brokerUrl);
                }
            }
        } catch (MqttException e) {
            if (LOG.isLoggable(Level.SEVERE)) {
                LOG.log(Level.SEVERE, "Unable to connect to the broker {0}", e.getMessage());
            }
        }
    }

    public void disconnect() {
        try {
            if (client.isConnected()) {
            client.disconnect();
            if (LOG.isLoggable(Level.INFO)) {
                LOG.log(Level.INFO, "Client disconnected : {0}", client.getClientId());
            }
            }
        } catch (MqttException e) {
            if (LOG.isLoggable(Level.SEVERE)) {
            LOG.log(Level.SEVERE, "Failed to stop client {0}", e.getMessage());
            }
        }
    }

    public void subscribe(String topic) {
        try {
            client.subscribe(topic);
        } catch (MqttException e) {
            LOG.log(Level.INFO, "Failed subsribe client: {0}", e.getMessage());

        }
    }

    public void unsubscribe(String topic) {
        try {
            client.unsubscribe(topic);
        } catch (MqttException e) {
            LOG.log(Level.INFO, "Failed subsribe client: {0}", e.getMessage());
        }
    }

    public void setCallBack(MqttCallback callback) {
        client.setCallback(callback);
    }

    public MqttClient getClient() {
        return client;
    }

}
