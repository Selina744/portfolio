/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consoleclient;

import consoleclient.VariableIterator.Variable;
import java.util.ArrayList;
import models.Driver;
import models.RaceCar;

/**
 *
 * @author Selina
 */
public class VariablesToObjectHelper {
    
    public static RaceCar getRaceCarFromVariableList(ArrayList<Variable> variables){
        RaceCar raceCar = new RaceCar();
        boolean valid = false;
        for(Variable variable : variables){
            //System.out.println("Parsing variable: " + variable);
            switch(variable.getVariableType()){
                case "id":
                    valid = true;
                    raceCar.setId(Integer.parseInt(variable.getVariableString()));
                    break;
                case "make":
                    raceCar.setMake(variable.getVariableString());
                    break;
                case "model":
                    raceCar.setModel(variable.getVariableString());
                    break;
                case "horsepower":
                    raceCar.setHorsePower(Integer.parseInt(variable.getVariableString()));
                    break;
                case "quartermiletime":
                    raceCar.setQuarterMileTime(Double.parseDouble(variable.getVariableString()));
                    break;
            }
        }
        if(!valid){
            raceCar = null;
        }
        return raceCar;
    }
    
    public static Driver getDriverFromVariableList(ArrayList<Variable> variables){
        Driver driver = new Driver();
        boolean valid = false;
        for(Variable variable : variables){
            switch(variable.getVariableType()){
                case  "id":
                    valid = true;
                    driver.setId(Integer.parseInt(variable.getVariableString()));
                    break;
                case "name":
                    driver.setName(variable.getVariableString());
                    break;
                case "age":
                    driver.setAge(Integer.parseInt(variable.getVariableString()));
                    break;
                case "ismale":
                    driver.setIsMale(Boolean.parseBoolean(variable.getVariableString()));
                    break;                        
            }
        }
        if(!valid){
            driver = null;
        }
        return driver;
    }
}
