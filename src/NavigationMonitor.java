import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.rmi.Naming;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class NavigationMonitor {
    private int checkingInterval;
    private int checkingTime;
    private int expireTime;
    private int lastUpdatedTime;
    private FaultMonitor faultMonitor;

    public NavigationMonitor() {
        this.checkingInterval = 4;
        this.checkingTime = 0;
        this.expireTime = 0;
        this.lastUpdatedTime = 0;
        this.faultMonitor = new FaultMonitor();
    }

    public boolean checkAlive(Thread nav) {
        return nav.isAlive();
    }

    public int getCheckingInterval() {
        return this.checkingInterval;
    }

    public void notifyFault() {
        this.faultMonitor.sendFaultNotification(this.expireTime);
    }

    public static void main(String[] args) throws Exception {
        try {
            NavigationMonitor navMonitor = new NavigationMonitor();
            Queue<Boolean> operationPool = new LinkedList<Boolean>();

            System.out.println("\nBeginning Multiprocess Navigation System Active Redundancy Test\n");
            ProcessBuilder monitorProcessBase = new ProcessBuilder("java", "Navigation", Integer.toString(1099));
            Process navProcessBase = monitorProcessBase.start();
            ProcessHandle processHandle = navProcessBase.toHandle();


            BufferedReader processReader = new BufferedReader(new InputStreamReader(navProcessBase.getInputStream()));
            String output = "";

            output = processReader.readLine();
            System.out.println(output+"\n");

            boolean isAlive = true;
            int testCounter = 0;
            while(isAlive == true) {
                testCounter++;
                NavigationInterface navRMIInterfaceBase = (NavigationInterface)Naming.lookup("Navigation");
                HashMap<String, Boolean> heartBeat = navRMIInterfaceBase.SendHeartBeat();
                isAlive = heartBeat.get("Is_Alive");
                //isAlive = navRMIInterfaceBase.SendHeartBeat();
                System.out.println("Navigation BASE SERVER Operation #"+testCounter+": "+isAlive+"\n");
                if(isAlive == true) {
                    System.out.println("BASE SERVER OPERATING AS NORMAL...\n");
                }
                else {
                    System.out.println("BASE SERVER HAS ENCOUNTERED ERROR...\n");
                }
                System.out.println("BASE SERVER RESPONSE #"+testCounter+": "+heartBeat+"\n");
                operationPool.add(isAlive);
            }
            processHandle.destroy();

            System.out.println("\nBASE SERVER ERROR - Base Server No Longer Functional\n");

            
            System.out.println("\nInitiating REDUNDANCY SERVER - Beginning Redundancy Process\n");

            TimeUnit.SECONDS.sleep(navMonitor.getCheckingInterval()); 

            ProcessBuilder monitorProcessRedundancy = new ProcessBuilder("java", "Navigation", Integer.toString(1099));
            Process navProcessRedundancy = monitorProcessRedundancy.start();
            
            System.out.println("\nREDUNDANCY SERVER INITIALIZED\n");

            processReader = new BufferedReader(new InputStreamReader(navProcessRedundancy.getInputStream()));
            output = "";
            output = processReader.readLine();
            System.out.println(output+"\n");

            NavigationInterface navRMIInterfaceRedundancy = (NavigationInterface)Naming.lookup("Navigation");
            navRMIInterfaceRedundancy.syncOperations(operationPool);
            operationPool.clear();

            isAlive = true;
            int testCounter2 = 0;
            while(isAlive == true) {
                testCounter2++;
                HashMap<String, Boolean> heartBeat = navRMIInterfaceRedundancy.SendHeartBeat();
                isAlive = heartBeat.get("Is_Alive");
                //isAlive = navRMIInterfaceRedundancy.SendHeartBeat();
                System.out.println("Navigation REDUNDANCY SERVER Operation #"+testCounter2+": "+isAlive+"\n");
                if(isAlive == true) {
                    System.out.println("REDUNDANCY SERVER OPERATING AS NORMAL...\n");
                }
                else {
                    System.out.println("REDUNDANCY SERVER HAS ENCOUNTERED ERROR...\n");
                }
                System.out.println("REDUNDANCY SERVER RESPONSE #"+testCounter2+": "+heartBeat+"\n");
                operationPool.add(isAlive);
            }
            navRMIInterfaceRedundancy.syncOperations(operationPool);

            ArrayList operations = navRMIInterfaceRedundancy.returnOperations();
            System.out.println("Completed Operations: "+Arrays.toString(operations.toArray())+"\n");
          
        }
        catch(Exception ex) {
            System.out.println("ERROR: "+ex);
        }
    }
}
