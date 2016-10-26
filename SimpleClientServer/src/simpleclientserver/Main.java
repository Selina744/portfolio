/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpleclientserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import server.Server;

/**
 *
 * @author Selina
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Main().runSocket(args);
    }
    
    public void runSocket(String[] args){
        int portNumber;
        if(args.length > 0){
           portNumber = Integer.parseInt(args[0]); 
        }else{
            portNumber = 8080;
        }
        System.out.println("Port: " + portNumber);
        try{
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while(true){
                System.out.println("Waiting for client...");
                Socket clientSocket = serverSocket.accept();     
                //PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);   
                OutputStream out = clientSocket.getOutputStream();
                InputStream in = clientSocket.getInputStream();
                Server serverRunnable = new Server(in, out);
                Thread t = new Thread(serverRunnable);
                t.start();                
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
        
    }
    
}
