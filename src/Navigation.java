import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;

import javax.management.remote.rmi.RMIServer;
import javax.swing.text.StyledEditorKit.BoldAction;

public class Navigation extends UnicastRemoteObject implements NavigationInterface {
    private GPS gps;
    private List<VisualSensor> visualSensors = new ArrayList<>();
    private boolean alive;
    private int sendingInterval;

    public Navigation() throws RemoteException {
        this.alive = true;
        this.sendingInterval = 3;
        this.gps = new GPS();
        visualSensors.add(new VisualSensor());
        visualSensors.add(new VisualSensor());
    }


    public boolean SendHeartBeat() throws RemoteException {
        boolean foundNewRoute = gps.findNewRoute();
        boolean isDisconnected = gps.isDisconnected();
        boolean checkedSensors = checkSensors();
        boolean heartBeat = ((foundNewRoute || isDisconnected) && checkedSensors);
        //if(heartBeat == true) {
            //System.out.println("\nNavigation HeartBeat - HEALTHY\n");
        //}
        //else {
            //System.out.println("\nNavigation Heartbeat - FLATLINE\n");
        //}
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
            System.out.println("RMI Navigation Server Started");

            Navigation nav = new Navigation();
            LocateRegistry.createRegistry(1099);
            Naming.rebind("//localhost/Navigation", nav);
            System.out.println("PeerServer bound in registry");

            
            //Server obj = new Server();
            //Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);
            //Navigation nav = new Navigation();

            //Navigation stub = (Navigation)UnicastRemoteObject.exportObject(nav, 0);

            // Bind the remote object's stub in the registry
            //Registry registry = LocateRegistry.getRegistry();
            //registry.bind("Hello", stub);

            //System.err.println("Server ready");
            
            //Navigation nav = new Navigation();
            //while(nav.isAlive() == true) {
            //    nav.SendHeartBeat();
            //    TimeUnit.SECONDS.sleep(nav.getSendingInterval());
            //}
        }
        catch(Exception ex) {
            System.out.println("ERROR: "+ex);
        }
    }

}
