import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.text.StyledEditorKit.BoldAction;

public class Navigation {
    private GPS gps;
    private List<VisualSensor> visualSensors = new ArrayList<>();
    private boolean alive;
    private int sendingInterval;

    public Navigation() {
        this.alive = true;
        this.sendingInterval = 3;
        this.gps = new GPS();
        visualSensors.add(new VisualSensor());
        visualSensors.add(new VisualSensor());
    }

    public boolean SendHeartBeat() {
        boolean foundNewRoute = gps.findNewRoute();
        boolean isDisconnected = gps.isDisconnected();
        boolean checkedSensors = checkSensors();
        boolean heartBeat = ((foundNewRoute || isDisconnected) && checkedSensors);
        if(heartBeat == true) {
            System.out.println("\nNavigation HeartBeat - HEALTHY\n");
        }
        else {
            System.out.println("\nNavigation Heartbeat - FLATLINE\n");
        }
        this.setAlive(heartBeat);
        return heartBeat;
    }

    private boolean checkSensors(){
        for(VisualSensor sensor:visualSensors) {
            if(sensor.detectObstacle() || sensor.isBroken()) {
                return false;
            }
        }
        return true;
    }

    public int getSendingInterval() {
        return this.sendingInterval;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void setAlive(boolean _alive) {
        this.alive = _alive;
    }

    public static void main(String[] args) {
        try {
            Navigation nav = new Navigation();
            while(nav.isAlive() == true) {
                nav.SendHeartBeat();
            }
        }
        catch(Exception ex) {
            System.out.println("ERROR: "+ex);
        }
    }

}
