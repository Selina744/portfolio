/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consoleclient;

import consoleclient.VariableIterator.Variable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Driver;
import models.RaceCar;

/**
 *
 * @author Selina
 */
public class ConsoleClient {

    /**
     * @param args the command line arguments
     */
    int currentId = 0;
    public static ArrayList<Integer> driverIds = new ArrayList<>();
    public static ArrayList<Integer> raceCarIds = new ArrayList<>();
    
    public static HashMap<Integer, Driver> drivers = new HashMap<>();
    public static HashMap<Integer, RaceCar> raceCars = new HashMap<>();

    public static Random rand = new Random();
    
    public static void main(String[] args) {
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        System.out.println("Running client socket on: " + hostName + " with port: " + portNumber);
        new ConsoleClient().runSockets(hostName, portNumber);
    }
    
    //Use threads. Do adds, then updates. Display concurrency problems and then fix them.
    public static boolean hasSpace = true;

    public void runSockets(String hostName, int portNumber){     
            //preform creates until full
            ArrayList<Thread> threads = new ArrayList<>();
            while (hasSpace) {
                String userInput = getRandomCreateCarCommand();
                Thread t = new Thread(new CreateRunnable(userInput, hostName, portNumber, raceCarIds, currentId));
                t.start();
                threads.add(t);
                userInput = getRandomCreateDriverCommand();
                Thread t2 = new Thread(new CreateRunnable(userInput, hostName, portNumber, driverIds, currentId));
                t2.start();
                threads.add(t2);
            }
            for(Thread t : threads){
                try {
                    t.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConsoleClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //perform 100 updates
            threads = new ArrayList<>();
            long startTime = System.currentTimeMillis();
            for(int i = 0;i < 100;i++){
                Thread t = new Thread(new UpdateRunnable(hostName, portNumber));
                t.start();
                threads.add(t);
            }
            for(Thread t : threads){
                try {
                    t.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConsoleClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }            
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("Total time to update: " + totalTime);
 
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
    public String getRandomCreateCarCommand(){
        currentId++;
        String[] models = { "civic", "accord", "fit", "crv" };
        String make = "honda";
        String model = models[rand.nextInt(4)];
        int horsePower = rand.nextInt(200) + 100;
        float quarterMileTime = rand.nextFloat() * (25 - 10) + 10;
        
        String command = String.format("CREATE racecar/id=%d&make=%s&model=%s&horsepower=%d&quartermiletime=%f#", 
                currentId, make, model, horsePower, quarterMileTime);    
        System.out.println("Random command: " + command);
        
        return command;
    }
        public String getRandomCreateDriverCommand(){
        currentId++;
        String[] names = { "kelly", "taylor", "sam", "alex" };
        int age = rand.nextInt(40) + 15;
        boolean isMale = rand.nextInt(1) == 0;
        String name = names[rand.nextInt(4)];
        Driver driver = new Driver();
        driver.setName(name);
        driver.setIsMale(isMale);
        driver.setAge(age);
        driver.setId(currentId);
        String command = String.format("CREATE %s#", driver);    
        System.out.println("Random command: " + command);
        
        return command;
    }
    
    //TODO - finish writing this method
    public String getUpdateCarCommand(String car){
        VariableIterator variableIterator = new VariableIterator(car);
        ArrayList<Variable> variableList = new ArrayList<>();
        while(variableIterator.hasNext()){
            Variable variable = variableIterator.getNextVariable();
            variableList.add(variable);
        }
        RaceCar raceCar = VariablesToObjectHelper.getRaceCarFromVariableList(variableList);
        raceCar.incrementHorsePower();
        String command = "UPDATE " + raceCar + "#"; 
        return command;
    }
        //Use variable parsing from server and change the toStrings
    //TODO - finish writing this method
    public String getUpdateDriverCommand(String driver){
        VariableIterator variableIterator = new VariableIterator(driver);
        ArrayList<Variable> variableList = new ArrayList<>();
        while(variableIterator.hasNext()){
            Variable variable = variableIterator.getNextVariable();
            variableList.add(variable);
        }
        Driver driverObject = VariablesToObjectHelper.getDriverFromVariableList(variableList);
        driverObject.incrementAge();
        String command = "UPDATE " + driverObject + "#";
        
        return command;
    }
}
