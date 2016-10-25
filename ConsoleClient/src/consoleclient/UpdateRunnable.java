/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consoleclient;

import static consoleclient.ConsoleClient.driverIds;
import static consoleclient.ConsoleClient.hasSpace;
import static consoleclient.ConsoleClient.raceCarIds;
import static consoleclient.ConsoleClient.rand;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import models.Driver;
import models.RaceCar;

/**
 *
 * @author Selina
 */
public class UpdateRunnable implements Runnable {
    
    String hostName;
    int portNumber;
    
    boolean protection = true;
    
    public UpdateRunnable(String hostName, int portNumber){
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    @Override
    public void run() {
        try (
            Socket socket = new Socket(hostName, portNumber);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
        ){
            if(rand.nextInt(2) > 0){
                updateCar(out, in);
            }else{
                updateDriver(out, in);
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
        
        public void updateCar(OutputStream out, InputStream in) throws IOException{
            if(raceCarIds.size() > 0){
                String getCarCommand = String.format("GET racecar/id=%d#", raceCarIds.get(rand.nextInt(raceCarIds.size())));
                System.out.println("Sending: " + getCarCommand);  
                boolean concurrencyProblem = false;
                RaceCar car;
                do{
                    out.write(getCarCommand.getBytes());
                    String response = getResponseFromServer(in);
                    System.out.println("get racecar Response: " + response);
                    car = getRaceCarFromResponse(response);
                    if(protection){
                        concurrencyProblem = testForConcurrencyProblems(car);                        
                    }else{
                        hasConcurrencyProblem(car);
                    }
                }while(concurrencyProblem);
                
                String updateCarCommand = getUpdateCarCommand(car);
                out.write(updateCarCommand.getBytes());     
                String updateCarResponse = getResponseFromServer(in);
                if(updateCarResponse.equals("success")){
                    
                }                
                System.out.println("update car response: " + updateCarResponse);
            }

        }
        
        public void updateDriver(OutputStream out, InputStream in) throws IOException{
            if(driverIds.size() > 0){
                String getDriverCommand = String.format("GET driver/id=%d#", driverIds.get(rand.nextInt(driverIds.size())));
                System.out.println("Sending: " + getDriverCommand);
                boolean concurrencyProblem = false;
                Driver driver;
                do{
                    out.write(getDriverCommand.getBytes());
                    String response = getResponseFromServer(in);
                    System.out.println("get driver Response: " + response);
                    driver = getDriverFromResponse(response);
                    if(protection){
                        concurrencyProblem = testForConcurrencyProblems(driver);                                            
                    }else{
                        hasConcurrencyProblem(driver);
                    }
                    out.flush();
                }while(concurrencyProblem);

                String updateDriverCommand = getUpdateDriverCommand(driver);
                out.write(updateDriverCommand.getBytes()); 
                String updateDriverResponse = getResponseFromServer(in);
                if(updateDriverResponse.equals("success")){

                }
                System.out.println("update driver response: " + updateDriverResponse);   
            }         
        }
        
        public boolean hasConcurrencyProblem(RaceCar raceCar){
            boolean problem = false;
            if(ConsoleClient.raceCars.containsKey(raceCar.getId())){
                RaceCar previousRaceCar = ConsoleClient.raceCars.get(raceCar.getId());
                if(raceCar.getHorsePower() < previousRaceCar.getHorsePower()){
                    problem = true;
                    System.out.println("Horsepower for car id " + raceCar.getId() + " was last updated to " 
                            + previousRaceCar.getHorsePower() + ", but was pulled up as " + raceCar.getHorsePower());
                }
            }
            return problem;
        }
        
        public synchronized boolean testForConcurrencyProblems(RaceCar raceCar){
            boolean problem = hasConcurrencyProblem(raceCar);
            if(!problem){
                raceCar.incrementHorsePower();
                System.out.println("Saving race car: " + raceCar);
                saveRaceCar(raceCar);
            }
            return problem;
        }
        
        public boolean hasConcurrencyProblem(Driver driver){
            boolean problem = false;            
            if(ConsoleClient.drivers.containsKey(driver.getId())){
                Driver previousDriver = ConsoleClient.drivers.get(driver.getId());
                if(driver.getAge() != previousDriver.getAge()){
                    problem = true;
                    System.out.println("Age for driver id " + driver.getId() + " was last updated to " 
                            + previousDriver.getAge() + ", but was pulled up as " + driver.getAge());
                }
            }
            return problem;
        }
        
        public synchronized boolean testForConcurrencyProblems(Driver driver){
            boolean problem = hasConcurrencyProblem(driver);            
            if(!problem){
                    driver.incrementAge();
                    System.out.println("Saving driver: " + driver);
                    saveDriver(driver);
            }            
            return problem;
        }
        
        public String getUpdateCarCommand(RaceCar raceCar){
       //raceCar.incrementHorsePower();
        String command = "UPDATE " + raceCar + "#";
        System.out.println("Update: " + command);
        return command;
    }

    public String getUpdateDriverCommand(Driver driver){        
       // driver.incrementAge();
        String command = "UPDATE " + driver + "#";
        System.out.println("Update: " + command);
        return command;
    }
    
    public Driver getDriverFromResponse(String response){
        VariableIterator variableIterator = new VariableIterator(response);
        ArrayList<VariableIterator.Variable> variableList = new ArrayList<>();
        while(variableIterator.hasNext()){
            VariableIterator.Variable variable = variableIterator.getNextVariable();
            variableList.add(variable);
        }
        Driver driverObject = VariablesToObjectHelper.getDriverFromVariableList(variableList);
        return driverObject;
    }
    
    public RaceCar getRaceCarFromResponse(String response){
        VariableIterator variableIterator = new VariableIterator(response);
        ArrayList<VariableIterator.Variable> variableList = new ArrayList<>();
        while(variableIterator.hasNext()){
            VariableIterator.Variable variable = variableIterator.getNextVariable();
            variableList.add(variable);
        }
        RaceCar raceCar = VariablesToObjectHelper.getRaceCarFromVariableList(variableList);
        return raceCar;
    }
    
    public void saveRaceCar(RaceCar raceCar){
        ConsoleClient.raceCars.put(raceCar.getId(), raceCar);
    }
    
    public void saveDriver(Driver driver){
        ConsoleClient.drivers.put(driver.getId(), driver);
    }
}
