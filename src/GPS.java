import java.util.ArrayList;
import java.util.List;

public class GPS {

    private List<Route> routes = new ArrayList<>();

    public boolean findNewRoute() {
        double startLat = Math.random() * 95;
        double endLat = Math.random() * 95;
        double startLong = Math.random() * 190;
        double endLong = Math.random() * 190;
        Route route = new Route(startLat, endLat, startLong, endLong);
        if(route.isValid()) {
            routes.add(route);
            return true;
        }
        return false;
    }

    public boolean isDisconnected() {
        double chance = (Math.random() * 100);
        boolean isDisconnected = chance <= 1;
        return isDisconnected;
    }

    public List<Route> getRoutes() {
        return this.routes;
    }
}
