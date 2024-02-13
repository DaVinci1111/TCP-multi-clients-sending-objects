package joc;

import exemples.Llista;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

class ThreadSevidorAdivina implements Runnable {
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ThreadSevidorAdivina(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        this.in = new ObjectInputStream(clientSocket.getInputStream());
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            // Receive the object from the client
            Llista llista = (Llista) in.readObject();

            // Process the received object and print the sorted list
            processAndPrintList(llista);

            // Send back the modified object to the client
            out.writeObject(llista);
            out.flush();

            // Close the resources
            clientSocket.close();
            in.close();
            out.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processAndPrintList(Llista llista) {
        // Sort and remove duplicates from the list
        List<Integer> sortedUniqueList = new ArrayList<>(new HashSet<>(llista.getNumberList()));
        Collections.sort(sortedUniqueList);

        // Print the modified list
        System.out.println("Received List from " + llista.getNom() + ": " + llista.getNumberList());
        System.out.println("Modified List (Sorted and Unique): " + sortedUniqueList);
    }
}