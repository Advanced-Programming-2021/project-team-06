package controller;
import java.lang.reflect.InvocationTargetException;
public class ActionExecutor {

    private static ActionExecutor instance;
    public static ActionExecutor getInstance() {
        if (instance == null)
            return (instance = new ActionExecutor());
        else
            return instance;
    }
    public void execute(String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        this.getClass().getDeclaredMethod(methodName).invoke(this);
    }

    private void sayHello() {
        System.out.println("hi!");
    }
}
