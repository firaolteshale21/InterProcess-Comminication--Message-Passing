// Import necessary packages
import java.io.*;

// Main class for IPC implementation
public class IPCMessagePassing {
    public static void main(String[] args) {
        try {
            // Create piped streams
            PipedOutputStream outputStream = new PipedOutputStream();
            PipedInputStream inputStream = new PipedInputStream(outputStream);

            // Create and start Process A (Sender)
            Thread processA = new Thread(new Sender(outputStream));
            processA.setName("Process-A");
            processA.start();

            // Create and start Process B (Receiver)
            Thread processB = new Thread(new Receiver(inputStream));
            processB.setName("Process-B");
            processB.start();
        } catch (IOException e) {
            System.err.println("Error setting up IPC: " + e.getMessage());
        }
    }
}

// Sender class (Process A)
class Sender implements Runnable {
    private PipedOutputStream outputStream;

    public Sender(PipedOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))) {
            String[] messages = {"Hello, Process B!", "How are youx?", "Goodbye!"};

            for (String message : messages) {
                // Send each message
                System.out.println(Thread.currentThread().getName() + " Sending: " + message);
                writer.write(message + "\n");
                writer.flush();

                // Simulate processing time
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println(Thread.currentThread().getName() + " Error: " + e.getMessage());
        }
    }
}

// Receiver class (Process B)
class Receiver implements Runnable {
    private PipedInputStream inputStream;

    public Receiver(PipedInputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String message;

            // Read messages from the sender
            while ((message = reader.readLine()) != null) {
                System.out.println(Thread.currentThread().getName() + " Received: " + message);
            }
        } catch (IOException e) {
            System.err.println(Thread.currentThread().getName() + " Error: " + e.getMessage());
        }
    }
}
