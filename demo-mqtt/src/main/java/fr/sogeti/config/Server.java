package fr.sogeti.config;

/**
 *
 * @author fduneau
 */
public class Server {
    private String address;
    private String protocol;
    private int port;

    public Server(String address, String protocol, int port) {
        this.address = address;
        this.protocol = protocol;
        this.port = port;
    }

    public Server(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    public String getUri(){
        if(protocol == null){
            return address+":"+port;
        }else{
            return protocol+"://"+address+":"+port;
        }
    }
}
