/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.hallaLib.HallaStor;
import helpers.VariablesToObjectHelper;
import iterators.VariableIterator;
import iterators.VariableIterator.Variable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Driver;
import models.RaceCar;

/**
 *
 * @author Selina
 */
public class Server implements Runnable {
    
    HallaStor dataStore;
    InputStream in;
    OutputStream out;
    
    public Server(InputStream in, OutputStream out){
        this.in = in;
        this.out = out;
        dataStore = HallaStor.getInstance();
    }
    
    public void createRaceCar(String id, String make, String model, int horsePower, double quarterMileTime){
        RaceCar raceCar = new RaceCar(Integer.parseInt(id), make, model, horsePower, quarterMileTime);
        dataStore.add(id, raceCar);
    }
    
    public void updateRaceCar(String id){
        ((RaceCar)dataStore.get(id)).incrementHorsePower();
    }
    
    public void deleteObject(String id){
        dataStore.delete(id);
    }
    
    public void createDriver(String id, String name, int age, Boolean isMale){
        Driver driver = new Driver(Integer.parseInt(id),name, age, isMale);
        dataStore.add(id, driver);
    }
    
    public void updateDriver(String id){
        ((Driver)dataStore.get(id)).incrementAge();
    }
    
    //escape character - *
    //delimiter - #
    //separates variables - &
    //starts variables - /
    //CREATE racecar/id=123&make=honda&model=civic&horsepower=200&quartermiletime=1.5#
    public String parseCommand(String command){
        
        String[] commandArray = command.split(" ");
        String commandType = commandArray[0];
        String[] splitCommandArray = commandArray[1].split("/");
        String objectType = splitCommandArray[0];
        String variables = null;
        String response = "error";
        if(splitCommandArray.length > 1){
            variables = splitCommandArray[1];
            switch(commandType.toUpperCase()){
                case "GET":
                    response = executeGet(objectType, variables);
                    break;
                case "CREATE":
                    response = executeCreate(objectType, variables);                
                    break;
                case "UPDATE":
                    response = executeUpdate(objectType, variables);                
                    break;

                case "DELETE":
                    response = executeDelete(objectType, variables);               
                    break;  
            }
        }

        return response;
    }
    
    //These need to return responses
    public String executeGet(String objectType, String variables){
        VariableIterator variableIterator = new VariableIterator(variables);
        String id = null;
        while(variableIterator.hasNext()){
            Variable variable = variableIterator.getNextVariable();
            if(variable.getVariableType().equalsIgnoreCase("id")){
                id = variable.getVariableString();
            }
        }
        String response = "error";
        if(id != null){
            switch(objectType.toLowerCase()){
                case "racecar":
                    RaceCar raceCar = (RaceCar) dataStore.get(id);
                    response = raceCar.toString();
                    break;
                case "driver":
                    Driver driver = (Driver)dataStore.get(id);
                    response = driver.toString();
                    break;
            } 
        }
        return response;
    }
    
    public String executeCreate(String objectType, String variables){
        VariableIterator variableIterator = new VariableIterator(variables);
        ArrayList<Variable> variableList = new ArrayList<>();
        while(variableIterator.hasNext()){
            Variable variable = variableIterator.getNextVariable();
            variableList.add(variable);
        }
        String response = "error";
        if(variableList.size() > 0){
            switch(objectType.toLowerCase()){
                case "racecar":
                    RaceCar raceCar = VariablesToObjectHelper.getRaceCarFromVariableList(variableList);
                    if(raceCar != null){
                        System.out.println("RaceCar create: " + raceCar);
                        if(dataStore.containsKey(Integer.toString(raceCar.getId()))){
                            response = "id is already in use";
                        }else{
                            try{
                                dataStore.add(Integer.toString(raceCar.getId()), raceCar);   
                                response = "success"; 
                            }catch(IllegalStateException e){
                                response = "full";
                            }
                        }
                    }else{
                        response = "id must be provided";
                    }
                    break;
                case "driver":
                    Driver driver = VariablesToObjectHelper.getDriverFromVariableList(variableList);
                    if(driver != null){
                        if(dataStore.containsKey(Integer.toString(driver.getId()))){
                            response = "id is already in use";
                        }else{
                            try{
                                dataStore.add(Integer.toString(driver.getId()), driver);   
                                response = "success"; 
                            }catch(IllegalStateException e){
                                response = "full";
                            }
                        }
                    }else{
                       response = "id must be provided";
                    }
                    break;
            } 
        }
        return response;
    }
    
    public String executeUpdate(String objectType, String variables){
        VariableIterator variableIterator = new VariableIterator(variables);
        ArrayList<Variable> variableList = new ArrayList<>();
        while(variableIterator.hasNext()){
            Variable variable = variableIterator.getNextVariable();
            variableList.add(variable);
        }
        String response = "error";
        if(variableList.size() > 0){
            switch(objectType.toLowerCase()){
                case "racecar":
                    RaceCar raceCar = VariablesToObjectHelper.getRaceCarFromVariableList(variableList);
                    if(raceCar != null){
                        if(dataStore.containsKey(Integer.toString(raceCar.getId()))){
                            response = "success"; 
                            dataStore.update(Integer.toString(raceCar.getId()), raceCar);
                        }else{
                            response = "id not found";
                        }
                    }else{
                        response = "id must be provided";
                    }
                    break;
                case "driver":
                    Driver driver = VariablesToObjectHelper.getDriverFromVariableList(variableList);
                    if(driver != null){
                        if(dataStore.containsKey(Integer.toString(driver.getId()))){
                            response = "success"; 
                            dataStore.update(Integer.toString(driver.getId()), driver);
                        }else{
                            response = "id not found";
                        }
                    }else{
                       response = "id must be provided";
                    }
                    break;
            } 
        }
        return response;
    }
    
    public String executeDelete(String objectType, String variables){
        VariableIterator variableIterator = new VariableIterator(variables);
        String id = null;
        while(variableIterator.hasNext()){
            Variable variable = variableIterator.getNextVariable();
            if(variable.getVariableType().equalsIgnoreCase("id")){
                id = variable.getVariableString();
            }
        }
        String response = "error";
        if(id != null){
            if(dataStore.containsKey(id)){
                dataStore.delete(id);
                response = "success";
            }else{
                response = "id not found";
            }
//            switch(objectType.toLowerCase()){
//                case "racecar":
//                    RaceCar raceCar = (RaceCar) dataStore.get(id);
//                    response = raceCar.toString();
//                    break;
//                case "driver":
//                    Driver driver = (Driver)dataStore.get(id);
//                    response = driver.toString();
//                    break;
//            } 
        }
        return response;
    }

    @Override
    public void run() {
        int input;
        boolean cont = true;
        StringBuilder sb = new StringBuilder();
        try {
            while ((input = in.read()) != -1) {
                char character = (char)input;
                //System.out.println("Found character: " + character);
                switch(character){
                    case '#':
                        cont = false;
                        break;
                    case '*':
                        
                        break;
                    default:
                        sb.append(character);
                        break;
                }
                if(!cont){
                    System.out.println("End of line symbol found. Parsing command");
                    System.out.println(sb.toString());
                    String response = parseCommand(sb.toString());
                    System.out.println("Command executed. Sending response: " + response);
                    //out.print(response);
                    response = response + "#";
                    out.write(response.getBytes());
                    System.out.println("Response sent.");
                    cont = true;
                    System.out.println("Cont set to true");
                    sb = new StringBuilder();
                }
                //System.out.println("End of character looping");
                //RaceCar create: id=9&make=honda&model=fit&horsepower=147&quartermiletime=18.215405
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }

}
