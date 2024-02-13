package joc;

import exemples.Llista;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientTcpAdivina {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5558;

    public static void main(String[] args) {
        try (Socket socket = new Socket(InetAddress.getByName(SERVER_HOST), SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Get user input for name
            Scanner scanner = new Scanner(System.in);
            System.out.print("Nombre: ");
            String name = scanner.nextLine();

            // Get user input for list of numbers
            System.out.print("Ingrese una lista de números separados por espacios: ");
            String numbersInput = scanner.nextLine();
            List<Integer> numberList = parseNumberList(numbersInput);

            // Create Llista object and send it to the server
            Llista llista = new Llista(name, numberList);
            out.writeObject(llista);
            out.flush();

            // Receive modified object from the server
            Llista modifiedLlista = (Llista) in.readObject();
            System.out.println("Lista modificada del servidor: " + modifiedLlista.getNumberList());

        } catch (UnknownHostException e) {
            System.out.println("Error connecting to the host: " + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error during communication: " + e.getMessage());
        }
    }

    private static List<Integer> parseNumberList(String input) {
        List<Integer> numberList = new ArrayList<>();
        String[] numbersArray = input.split(" ");
        for (String numberStr : numbersArray) {
            try {
                int number = Integer.parseInt(numberStr);
                numberList.add(number);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format: " + numberStr);
            }
        }
        return numberList;
    }
}
