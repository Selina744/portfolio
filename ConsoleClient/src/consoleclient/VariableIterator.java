/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package consoleclient;

/**
 *
 * @author Selina
 */
public class VariableIterator {
    private char[] variablesCharArray;
    private int currentLocation = 0;
    
    public VariableIterator(String variables){
        variablesCharArray = variables.toCharArray();
    }
    
    private char getNextChar(){
        char nextChar = variablesCharArray[currentLocation];
        currentLocation++;
        return nextChar;
    }
    
    public boolean hasNext(){
        return variablesCharArray.length > currentLocation;
    }
    
    public Variable getNextVariable(){
        StringBuilder variableType = new StringBuilder();
        StringBuilder variableString = new StringBuilder();
        boolean cont = true;
        while(cont){
            if(hasNext()){
                char currentChar = getNextChar();
                switch(currentChar){
                    case '*':
                        variableType.append(getNextChar());
                        break;
                    case '=':
                        cont = false;
                        break;
                    case '&':
                        cont = false;
                        variableType = null;
                        break;
                    default:
                        variableType.append(currentChar);
                        break;
                }
            }else{
                cont = false;
            }  
        }
        cont = true;

        while(cont){
            if(hasNext()){
                char currentChar = getNextChar();
                switch(currentChar){
                    case '*':
                        variableString.append(getNextChar());
                        break;
                    case '=':
                        cont = false;
                        variableString = null;                    
                        break;
                    case '&':
                        cont = false;
                        break;
                    default:
                        variableString.append(currentChar);
                        break;
                }
            }else{
                cont = false;
            }
            
        }
        Variable variable = null;
        if(variableType != null && variableString != null){
            variable = new Variable(variableType.toString(), variableString.toString());
        }
        return variable;
    }
    
    public class Variable{
        private final String variableType;
        private final String variableString;
        
        public Variable(String type, String variableString){
            variableType = type;
            this.variableString = variableString;
        }

        public String getVariableType() {
            return variableType;
        }

        public String getVariableString() {
            return variableString;
        }
        
        @Override
        public String toString(){
            return variableType + " : " + variableString;
        }
    }
}
