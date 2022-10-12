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


    public boolean SendHeartBeat() throws RemoteException {
        boolean foundNewRoute = gps.findNewRoute();
        boolean isDisconnected = gps.isDisconnected();
        boolean checkedSensors = checkSensors();
        boolean heartBeat = ((foundNewRoute || isDisconnected) && checkedSensors);
        this.setAlive(heartBeat);
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
