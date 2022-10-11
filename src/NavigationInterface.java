import java.rmi.Remote;
import java.rmi.RemoteException;


public interface NavigationInterface extends Remote {
    public boolean SendHeartBeat() throws RemoteException; 

}
