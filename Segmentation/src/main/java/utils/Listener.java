package utils;

import java.util.concurrent.TimeUnit;

public class Listener {
    private IPythonServerEntryPoint entryPoint = null;


    public void register(IPythonServerEntryPoint entryPoint) {
        this.entryPoint = entryPoint;
        System.out.println("Entry point registered");
    }

    public String runMethod(String code) throws IllegalStateException {
        if (entryPoint == null) {
            System.out.println("Entry point hasn't initialized yet, waiting 3 more secondes");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch(InterruptedException e) {
                // do nothing
            }
        }
        if (entryPoint == null) {
            throw new IllegalStateException("Entry point hasn't initialized by python process");
        }
        return entryPoint.runMethod(code);
    }
}
