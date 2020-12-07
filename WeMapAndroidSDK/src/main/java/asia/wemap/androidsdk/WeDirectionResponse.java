package asia.wemap.androidsdk;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import asia.wemap.androidsdk.geometry.LatLng;

public class WeDirectionResponse {
    private double distance;
    private double weight;
    private int time;
    private double transfers;
    private LatLng originPoint;
    private LatLng destinationPoint;
    private String pointsString;
    private List<LatLng> points;
    private List<LatLng> waypoints;
    private List<Instruction> instructions;

    public WeDirectionResponse(JSONObject weDirectionResponse) {
        try {
            JSONArray coordinates = weDirectionResponse.getJSONObject("snapped_waypoints").getJSONArray("coordinates");
            this.waypoints = new ArrayList<>();
            for(int i=0; i<coordinates.length(); i++){
                waypoints.add(new LatLng(coordinates.getJSONArray(i).getDouble(1), coordinates.getJSONArray(i).getDouble(0)));
            }
            this.originPoint = waypoints.get(0);
            this.destinationPoint = waypoints.get(waypoints.size()-1);

            this.distance = weDirectionResponse.getDouble("distance");
            this.weight = weDirectionResponse.getDouble("weight");
            this.transfers = weDirectionResponse.getDouble("transfers");
            this.time = weDirectionResponse.getInt("time");
            if(weDirectionResponse.has("instructions")){
                this.instructions = new ArrayList<>();
                JSONArray instructions = weDirectionResponse.getJSONArray("instructions");
                for(int i=0; i< instructions.length(); i++){
                    this.instructions.add(new Instruction(instructions.getJSONObject(i)));
                }
            }

            if(weDirectionResponse.has("points")){
                this.pointsString = weDirectionResponse.getJSONObject("points").toString();
                this.points = new ArrayList<>();
                JSONArray pois = weDirectionResponse.getJSONObject("points").getJSONArray("coordinates");
                for(int i=0; i< pois.length(); i++){
                    this.points.add(new LatLng(pois.getJSONArray(i).getDouble(1), pois.getJSONArray(i).getDouble(0)));
                }
            }
        } catch (Exception e) {
            Log.i("App", "Error parsing data" + e.getMessage());
        }
    }

    public double getDistance() {
        return this.distance;
    }

    public double getWeight() {
        return this.weight;
    }

    public int getTime() {
        return this.time;
    }

    public double getTransfers() {
        return this.transfers;
    }

    public LatLng getOriginPoint() {
        return this.originPoint;
    }

    public LatLng getDestinationPoint() {
        return this.destinationPoint;
    }

    public List<Instruction> getInstructions() {
        return this.instructions;
    }

    public String getPointsString() {
        return this.pointsString;
    }

    public List<LatLng> getPoints() {
        return this.points;
    }
    public List<LatLng> getWayPoints() {
        return this.waypoints;
    }

    public class Instruction {
        public double distance;
        public double heading;
        public int sign;
        public int time;
        public String text;
        public String street_name;
        public int[] interval;

        public Instruction(JSONObject instruction){
            try {
                this.distance = instruction.getDouble("distance");
                this.heading = instruction.has("heading") ? instruction.getDouble("heading") : 0;
                this.sign = instruction.getInt("sign");
                this.time = instruction.getInt("time");
                this.text = instruction.getString("text");
                this.street_name = instruction.getString("street_name");
                this.interval = new int[]{instruction.getJSONArray("interval").getInt(0), instruction.getJSONArray("interval").getInt(1)};
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}