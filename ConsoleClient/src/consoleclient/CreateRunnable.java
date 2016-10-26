/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consoleclient;

import static consoleclient.ConsoleClient.hasSpace;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 *
 * @author Selina
 */
public class CreateRunnable implements Runnable {
    
    String command;
    String hostName;
    int portNumber;
    ArrayList<Integer> ids;
    int currentId;
    
    public CreateRunnable(String command, String hostName, int portNumber, ArrayList<Integer> ids, int currentId){
        this.command = command;
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.ids = ids;
        this.currentId = currentId;
    }

    @Override
    public void run() {
        try (
            Socket socket = new Socket(hostName, portNumber);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
        ){
                out.write(command.getBytes());
                System.out.println("Sent bytes");
                String response = getResponseFromServer(in);
                System.out.println("response from server: " + response);
                if(response.equals("full")){
                    hasSpace = false;
                }else{
                    ids.add(currentId);
                    System.out.println("Added id: " + currentId);
                }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        } 
    }
    
        public String getResponseFromServer(InputStream in ) throws IOException{
        StringBuilder sb = new StringBuilder();
        int input;
        while ((input = in.read()) != (int)'#') {
            char character = (char)input;
            //System.out.println("Read char: " + character);
            sb.append(character);
        }
        String response = sb.toString();
        return response;
    }
    
}
