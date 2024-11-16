/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lab.ed2;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientServer {

    public static final int SERVER_PORT = 8081;
    public static final String SERVER_ADRESS = "localhost";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = 25000000;
        double t = 2;
        int choice = 1;
        int count = 0;

        // Elegir algoritmo
        System.out.println("Seleccione el algoritmo: ");
        System.out.println("1. QuickSort\n2. Heapsort\n3. MergeSort");
        //choice = scanner.nextInt();

        // Elegir tamaño del vector y tiempo límite
        System.out.print("Ingrese el tamaño del vector: ");
        //n = scanner.nextInt();
        System.out.print("Ingrese el tiempo límite para cada worker (en segundos): \n");
        //t = scanner.nextInt();

        // Generar vector aleatorio
        int[] vector = new Random().ints(n, 0, 100000).toArray();
        Task task = new Task(vector, choice, t);
        //Procesamiento de datos con los Worker_0 y Worker_1
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            while (true) {
                Socket socketWorker = serverSocket.accept();

                var worker = new ClientApp(socketWorker, count, task);

                //inicializa la conexion con el worker en otro hilo
                new Thread(worker).start();

                count++;
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
