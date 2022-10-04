
public class FaultMonitor {
    private String faultNotification;

    public FaultMonitor() {
        this.faultNotification = "ERROR - FAULT DETECTED IN NAVIGATION";
    }

    public void setFaultNotification(String _faultNotification) {
        this.faultNotification = _faultNotification;
    }

    public String getFaultNotification() {
        return this.faultNotification;
    }

    public void sendFaultNotification(int faultTime) {
        System.out.println(this.getFaultNotification()+" - "+faultTime);
    }
}
