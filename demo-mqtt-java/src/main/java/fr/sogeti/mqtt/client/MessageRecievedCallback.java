package fr.sogeti.mqtt.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.sogeti.dao.DAO;
import fr.sogeti.domain.Book;
import fr.sogeti.domain.Element;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MessageRecievedCallback<T extends Element> implements MqttCallback {

    private ElementsMqtt<T> elementsMqtt;
    private DAO<T> dao;
    private Class<T> clazz;
    private Gson gson = new Gson();
    private final Logger LOG = Logger.getLogger(MessageRecievedCallback.class.getName());
    // Paths to the different topics under publishing.
    private final String getPublishingPath;
    private final String getallPublishingPath;
    // Paths to the different topics under delivery.
    private final String getDeliveryPath;
    private final String getallDeliveryPath;

    public MessageRecievedCallback(ElementsMqtt<T> elementsMqtt, Class<T> clazz, DAO<T> dao) {

        this.elementsMqtt = elementsMqtt;
        this.clazz = clazz;
        this.dao = dao;
        dao.init();
        getPublishingPath = elementsMqtt.getPublishTopic() + "/GET/.+";
        getallPublishingPath = elementsMqtt.getPublishTopic() + "/GETALL";
        getallDeliveryPath = elementsMqtt.getDeliverTopic() + "/GETALL";
        getDeliveryPath = elementsMqtt.getDeliverTopic() + "/GET/.+";
    }

    @Override
    public void connectionLost(Throwable arg0) {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken arg0) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        LOG.log(Level.INFO, "Message sent on: {0}", topic);
        LOG.log(Level.INFO, "Message: {0}", new String(message.getPayload()));

        if (topic.matches(getPublishingPath)) {

            handleGet(extractId(topic));

        } else if (topic.equals(getallPublishingPath)) {

            handleGetAll();

        }

    }

    /**
     * Handles messages coming on the GET subtopic by sending back the requested
     * item.
     * 
     * @param id
     */
    public void handleGet(int id) {

	try {
	    System.out.println(gson.toJson(dao.get(id)));
	    elementsMqtt.sendMessage(gson.toJson(dao.get(id)), 2, getDeliveryPath);

	} catch (MqttException e) {

	    LOG.log(Level.SEVERE, "{0}", e.getMessage());
	}

	LOG.log(Level.INFO, "REQUEST GET HAST BEEN HANDLED.");

    }

    /**
     * Handles messages coming on the GETALL subtopic by sending back the
     * requested items.
     */
    public void handleGetAll() {

        Type listType = new TypeToken<List<Book>>() {}.getType();
        try {
            elementsMqtt.sendMessage(gson.toJson(dao.getAll(), listType), 2, getallDeliveryPath);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        LOG.log(Level.INFO, "REQUEST GETALL HAST BEEN HANDLED.");

    }

    /**
     * Extracts the id from the the topic.
     * 
     * @param topic
     * @return
     */

    private int extractId(String topic) {

	String[] splitedTopic = topic.split("/");

	return Integer.parseInt(splitedTopic[splitedTopic.length - 1]);

    }
}
