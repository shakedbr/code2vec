package utils;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import py4j.GatewayServer;

public class PythonRunner {
    private static String FILE = "C:/code2vec/python_input.java";
    private static PythonRunner pythonRunner = null;
    private Listener iListener = null;
    private GatewayServer server;
    private Process serverProcess = null;

    private PythonRunner(String interperterPath, String modulePath, Listener listener) throws IOException {
        server = new GatewayServer(listener);
        server.start(true);
        ProcessBuilder pb = new ProcessBuilder(interperterPath, "-u", modulePath);
        final Process process = pb.inheritIO().start();
        serverProcess = process;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {System.out.println("Running Shutdown Hook");
                                                                new File(FILE).delete();
                                                                process.destroy();}));

        while (!new File(FILE).exists()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch(InterruptedException e) {
                // do nothing
            }
        }
        System.out.println("[J]: Python server ready to predict");
    }
    public static PythonRunner getInstance() {
        return pythonRunner;
    }

    public static PythonRunner getInstance(String interperterPath, String modulePath, Listener listener) throws IOException{
        if (pythonRunner == null) {
            pythonRunner = new PythonRunner(interperterPath, modulePath, listener);
        }
        return pythonRunner;
    }

    public void cleanResources() {
        server.shutdown();
        serverProcess.destroy();
        new File(FILE).delete();
    }

    @Override
    public void finalize() {
        cleanResources();
    }
}
