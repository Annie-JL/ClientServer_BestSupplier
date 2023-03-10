package server;

public class Main {
    public static final int PORT_WORK = 9001;
    public static void main(String[] args) {
        Server server = new Server(PORT_WORK);
        new Thread(server).start();
    }
}
