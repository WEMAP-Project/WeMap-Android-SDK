package asia.wemap.androidsdk;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import asia.wemap.androidsdk.geometry.LatLng;

public class WePlace {
    private String placeId;
    private LatLng location;
    private String placeName;
    private String street;
    private String ward;
    private String district;
    private String city;
    private String housenumber;

    public WePlace(String place){
        try {
            JSONObject feature = new JSONObject(place);
            JSONArray coordinates = feature.getJSONObject("geometry").getJSONArray("coordinates");
            this.location = new LatLng(coordinates.getDouble(1), coordinates.getDouble(0));
            JSONObject properties = feature.getJSONObject("properties");
            this.placeName = properties.has("name") ? properties.getString("name") : null;
            this.placeId = properties.has("source_id") ? properties.getString("source_id") : null;
            this.housenumber = properties.has("housenumber") ? properties.getString("housenumber") : null;
            this.street = properties.has("street") ? properties.getString("street") : null;
            this.ward = properties.has("locality") ? properties.getString("locality") : null;
            this.district = properties.has("county") ? properties.getString("county") : null;
            this.city = properties.has("region") ? properties.getString("region") : null;
        } catch(Exception e){
            Log.i("App", "Error parsing data" +e.getMessage());
        }
    }

    
    /** 
     * @return String
     */
    public String getLocation(){
        return this.location.toString();
    }

    
    /** 
     * @return String
     */
    public String toString(){
        List<String> properties = new ArrayList<String>();
        if(placeName != null){
            properties.add(placeName);
        }
        if(housenumber != null){
            properties.add(housenumber);
        }
        if(street != null){
            properties.add(street);
        }
        if(ward != null){
            properties.add(ward);
        }
        if(district != null){
            properties.add(district);
        }
        if(city != null){
            properties.add(city);
        }
        return TextUtils.join(", ", properties);
    }

    
    /** 
     * @return String
     */
    public String getName(){
        return this.placeName;
    }
    
    /** 
     * @return String
     */
    public String getHouseNumber(){
        return this.housenumber;
    }
    
    /** 
     * @return String
     */
    public String getStreet(){
        return this.street;
    }
    
    /** 
     * @return String
     */
    public String getWard(){
        return this.ward;
    }
    
    /** 
     * @return String
     */
    public String getDistrict(){
        return this.district;
    }
    
    /** 
     * @return String
     */
    public String getCity(){
        return this.city;
    }
}