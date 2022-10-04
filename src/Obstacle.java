public class Obstacle {
    private String description;
    private boolean isCriticalDanger;

    public Obstacle(String description, boolean isCriticalDanger) {
        this.description = description;
        this.isCriticalDanger = isCriticalDanger;
    }

    public boolean isCriticalDanger() {
        return this.isCriticalDanger;
    }

    public String getDescription() {
        return this.description;
    }
}
