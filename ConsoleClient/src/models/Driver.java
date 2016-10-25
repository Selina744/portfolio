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
public class Driver {
    private int id;

    private String name;

    private int age;

    private boolean isMale;

    public Driver(int id, String name, int age, boolean isMale) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.isMale = isMale;
    }
    
    public Driver(){}
    
    public void incrementAge(){
        age++;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setIsMale(boolean isMale) {
        this.isMale = isMale;
    }

    public int getId() {
        return id;
    }
    
    @Override
    public String toString(){
        return String.format("driver/id=%d&name=%s&age=%d&ismale=%b", id, name, age, isMale);
    }
}
