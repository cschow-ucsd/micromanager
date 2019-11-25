import java.util.List;

public class DistanceHelper {
    public DistanceHelper(double[] longitudes, double[] latitudes) {

    }

    public List<LocationDistance> getPossibleDistances() {
        // TODO: call the distance api to get travel between all possible longitudes and latitudes
    }

    class LocationDistance {
        private double lon1, lat1, lon2, lat2;
        private int travelTime;

        public LocationDistance(double lon1, double lat1, double lon2, double lat2, int travelTime) {
            this.lon1 = lon1;
            this.lat1 = lat1;
            this.lon2 = lon2;
            this.lat2 = lat2;
            this.travelTime = travelTime;
        }

        public double getLon1() {
            return lon1;
        }

        public void setLon1(double lon1) {
            this.lon1 = lon1;
        }

        public double getLat1() {
            return lat1;
        }

        public void setLat1(double lat1) {
            this.lat1 = lat1;
        }

        public double getLon2() {
            return lon2;
        }

        public void setLon2(double lon2) {
            this.lon2 = lon2;
        }

        public double getLat2() {
            return lat2;
        }

        public void setLat2(double lat2) {
            this.lat2 = lat2;
        }

        public int getTravelTime() {
            return travelTime;
        }

        public void setTravelTime(int travelTime) {
            this.travelTime = travelTime;
        }
    }
}
