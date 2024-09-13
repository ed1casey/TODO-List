import java.io.Serializable;

public class Task implements Serializable {
    private String description;
    private boolean isCompleted;

    public Task(String description) {
        this.description = description;
        isCompleted = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markAsCompleted() {
        isCompleted = true;
    }

    @Override
    public String toString() {
        return description;
    }
}
