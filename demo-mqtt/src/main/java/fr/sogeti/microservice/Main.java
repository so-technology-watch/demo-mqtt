package fr.sogeti.microservice;

import fr.sogeti.dao.BookDao;
import fr.sogeti.domain.Book;
import fr.sogeti.mqtt.client.ElementsMqtt;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author fduneau
 */
public class Main {
    public static void main(String[] args) throws MqttException {
        String brokerURL = args.length > 0 ? args[0] : "tcp://10.226.159.191:1883";
        
        clientTest(brokerURL);
        serverTest(brokerURL);
    }
    
    private static void serverTest(String brokerUrl) throws MqttException {
        ExempleMqtt<Book> books = new ExempleMqtt<>(brokerUrl, "/books", 8080, "clientID", Book.class);
        System.out.println("GET ALL");
        books.getExemple(null);
        System.out.println("GET id 1");
        books.getExemple("1");
    }
    
    private static void clientTest(String brokerURL) throws MqttException {
        ElementsMqtt<Book> elements = new ElementsMqtt<>(brokerURL, "microServiceMQTT", Book.class, new BookDao());
        elements.launch();
    }
}
