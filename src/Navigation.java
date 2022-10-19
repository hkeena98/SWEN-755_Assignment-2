import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.LinkedList;
import java.util.Queue;
import javax.management.remote.rmi.RMIServer;
import javax.swing.text.StyledEditorKit.BoldAction;
import java.util.HashMap;

public class Navigation extends UnicastRemoteObject implements NavigationInterface {
    private GPS gps;
    private List<VisualSensor> visualSensors = new ArrayList<>();
    private boolean alive;
    private int sendingInterval;
    private ArrayList operations;

    public Navigation() throws RemoteException {
        super(0);
        this.alive = true;
        this.sendingInterval = 3;
        this.gps = new GPS();
        this.operations = new ArrayList<Boolean>();
        visualSensors.add(new VisualSensor());
        visualSensors.add(new VisualSensor());
    }


    public HashMap SendHeartBeat() throws RemoteException {
        HashMap<String, Boolean> heartBeat = new HashMap<String, Boolean>();
        boolean foundNewRoute = gps.findNewRoute();
        heartBeat.put("Find_New_Route_Test", foundNewRoute);
        boolean isDisconnected = gps.isDisconnected();
        heartBeat.put("Is_Disconnected_Test", isDisconnected);
        boolean checkedSensors = checkSensors();
        heartBeat.put("Sensor_Check", checkedSensors);
        boolean alive = ((foundNewRoute || isDisconnected) && checkedSensors);
        heartBeat.put("Is_Alive", alive);
        this.setAlive(alive);
        return heartBeat;
    }

    public void syncOperations(Queue<Boolean> queue) throws RemoteException {
        for(int i = 0; i < queue.size(); i++) {
            boolean head = queue.peek();
            if(head == true) {
                this.operations.add(head);
            }
        }
    }

    public ArrayList<Boolean> returnOperations() throws RemoteException {
        return this.operations;
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
    

    public static void main(String[] args) throws RemoteException {
        try {
            Navigation nav = new Navigation();
            String portnum = args[0];

            //while(nav.isAlive() == true) {
            System.out.println("RMI Navigation Server Started on Port - "+portnum);
            Registry reg = LocateRegistry.createRegistry(Integer.parseInt(portnum));
            reg.rebind("Navigation", nav);
            System.out.println("RMI Navigation Server Bound in Registry");
        }
        catch(Exception ex) {
            System.out.println("ERROR: "+ex);
        }
    }

}
