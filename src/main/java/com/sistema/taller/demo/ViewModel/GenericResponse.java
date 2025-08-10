package com.sistema.taller.demo.ViewModel;

public class GenericResponse {

    private String result; 
    private String message; 
    private boolean active;



    
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    } 

    
}
