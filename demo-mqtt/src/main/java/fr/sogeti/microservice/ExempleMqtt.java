package fr.sogeti.microservice;

import com.google.gson.Gson;
import fr.sogeti.microservice.api.mqtt.IMqttAccess;
import fr.sogeti.microservice.api.mqtt.MqttAccess;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 *
 * @author fduneau
 * T the type object the objejct manipulated by this verticle
 */
public class ExempleMqtt<T> {
    public static final String JSON_MIME = "application/json";
    private static final Logger LOG = Logger.getLogger(ExempleMqtt.class.getName());

    private IMqttAccess<T> accessMqtt;
    private final Class<T> clazz;
    public final String route;
    private final int port;

    public ExempleMqtt(String url, String route, int port, String clientId, Class<T> clazz) throws MqttException{
        this.route = route;
        this.port = port;
        this.clazz = clazz;
        accessMqtt = new MqttAccess<>(url, route, clientId);
    }
    
    public void getExemple(String idStr) {
        Gson gson = new Gson();

        if(idStr == null){
            // if there is no id, getAll()
            accessMqtt.getAll( json -> {
                if(LOG.isLoggable(Level.INFO)){
                    LOG.log(Level.INFO, "message received for getall {0}", json);
                }
            } );
        }else{
            if(isInteger(idStr)){
                int id = Integer.parseInt(idStr);
                accessMqtt.get(id , json -> {
                    if(LOG.isLoggable(Level.INFO)){
                        LOG.log(Level.INFO, "message received getbyid {0}", json);
                    }
                } );
            }
        }
    }
    
    private boolean isInteger(String idStr){
        return idStr.matches("\\d+");
    }
}
