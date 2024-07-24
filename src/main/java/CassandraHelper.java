import java.util.concurrent.CompletableFuture;

// Java Class
public class CassandraHelper {
    public void writeIntoCassandra(String data) {
        // Simulate a blocking I/O operation
        try {
            Thread.sleep(2000); // Simulating a delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public CompletableFuture<Void> writeIntoCassandraAsync() {
        return CompletableFuture.runAsync(() -> {
            // Simulate blocking I/O operation
            try {
                Thread.sleep(2000); // Simulating a delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
