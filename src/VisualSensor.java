import java.util.ArrayList;
import java.util.List;

public class VisualSensor {
    private List<Obstacle> obstacles = new ArrayList<>();

    public boolean detectObstacle() {
        Obstacle obstacle = new Obstacle("Road Hazard", (Math.random() * 10) <= 1);
        if(!obstacle.isCriticalDanger()) {
            obstacles.add(obstacle);
            return false;
        }
        return true;
    }

    public boolean isBroken() {
        return (Math.random() * 100) <= 1;
    }

    public List<Obstacle> getObstacles() {
        return this.obstacles;
    }
}
