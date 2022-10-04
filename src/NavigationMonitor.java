import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class NavigationMonitor {
    private int checkingInterval;
    private int checkingTime;
    private int expireTime;
    private int lastUpdatedTime;
    private FaultMonitor faultMonitor;

    public NavigationMonitor() {
        this.checkingInterval = 2;
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

    public static void main(String[] args) {
        try {
            NavigationMonitor navMonitor = new NavigationMonitor();
            System.out.println("\nBeginning Navigation System Multiprocess Testing Suite...\n");
            for(int x = 0; x < 20; x++) {
                System.out.println("\nBeginning Multiprocess Navigation System Test #"+(x+1)+"\n");
                ProcessBuilder monitorProcess = new ProcessBuilder("java", "Navigation");
                Process navProcess = monitorProcess.start();
                BufferedReader processReader = new BufferedReader(new InputStreamReader(navProcess.getInputStream()));
                String output = "";
                while((output = processReader.readLine()) != null) {
                    System.out.println(output);
                }
                TimeUnit.SECONDS.sleep(navMonitor.getCheckingInterval());
            }
        }
        catch(Exception ex) {
            System.out.println("ERROR: "+ex);
        }
    }
}
