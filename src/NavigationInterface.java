import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public interface NavigationInterface extends Remote {
    public HashMap SendHeartBeat() throws RemoteException;
    public void syncOperations(Queue<Boolean> queue) throws RemoteException;
    public ArrayList<Boolean> returnOperations() throws RemoteException;
}
