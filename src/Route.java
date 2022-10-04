public class Route {
    private double startLat;
    private double endLat;
    private double startLong;
    private double endLong;

    public Route(double startLat, double endLat, double startLong, double endLong) {
        this.startLat = startLat;
        this.endLat = endLat;
        this.startLong = startLong;
        this.endLong = endLong;
    }

    public boolean isValid() {
        return (startLat <= 90 && startLat >= -90) && (endLat <= 90 && endLat >= -90)
                && (startLong <= 180 && startLong >= -180) && (endLong <= 180 && endLong >= -180);
    }
}
