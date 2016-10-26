/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Selina
 */
public class RaceCar {
    private int id;

    private String make;

    private String model;

    private int horsePower;

    private double quarterMileTime;

    public RaceCar(int id, String make, String model, int horsePower, double quarterMileTime) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.horsePower = horsePower;
        this.quarterMileTime = quarterMileTime;
    }
    
    public RaceCar(){ }
    
    public void incrementHorsePower(){
        horsePower++;
    }

    public int getId() {
        return id;
    }

    public int getHorsePower() {
        return horsePower;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setHorsePower(int horsePower) {
        this.horsePower = horsePower;
    }

    public void setQuarterMileTime(double quarterMileTime) {
        this.quarterMileTime = quarterMileTime;
    }
    
    @Override
    public String toString(){
        return String.format("racecar/id=%d&make=%s&model=%s&horsepower=%d&quartermiletime=%f", id, make, model, horsePower, quarterMileTime);
    }
}
