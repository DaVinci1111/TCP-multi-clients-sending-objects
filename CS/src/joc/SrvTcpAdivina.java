package joc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SrvTcpAdivina {
    private int port;
    private ExecutorService executorService;

    public SrvTcpAdivina(int port) {
        this.port = port;
        this.executorService = Executors.newFixedThreadPool(10); // Ajusta el número de hilos según sea necesario
    }

    public void listen() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.execute(new ThreadSevidorAdivina(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SrvTcpAdivina srv = new SrvTcpAdivina(5558);
        srv.listen();
    }
}
