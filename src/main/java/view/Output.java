package view;

import controller.Database;

public class Output {
    private Output(){}

    private static Output instance;

    public static Output getInstance(){
        if(instance==null)
            instance = new Output();
        return instance;
    }

    public void showMessage(String message){
        System.out.println(message);
    }

}
