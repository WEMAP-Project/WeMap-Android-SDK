package asia.wemap.androidsdk;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import asia.wemap.androidsdk.exceptions.WeMapConfigurationException;
import asia.wemap.androidsdk.geometry.LatLng;

public class WeDirection {

    public WeDirection() {

    }

    
    /** 
     * @param points
     * @param weDirectionOptions
     * @return String
     */
    private String buildDirectionURL(@NonNull List<LatLng> points, WeDirectionOptions weDirectionOptions) {
        String access_token = WeMap.getAccessToken();
        List<String> pointsString = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            pointsString.add("&point=" + points.get(i).getAPIPoint());
        }
        String directionURL = String.format("%s?key=%s%s&points_encoded=false&type=json", WEMAP_DIRECTION_API, access_token, TextUtils.join("", pointsString));
        if (weDirectionOptions != null) {
            String vehicle = weDirectionOptions.getVehicle();
            directionURL = String.format("%s&vehicle=%s", directionURL, vehicle);

            boolean instructions = weDirectionOptions.getInstructions();
            directionURL = String.format("%s&instructions=%s", directionURL, String.valueOf(instructions));
        }
        return directionURL;
    }

    private String WEMAP_DIRECTION_API = "https://apis.wemap.asia/route-api/route";

    
    /** 
     * Dan duong phai truyen it nhat 2 diem!
     * @param points
     * @param weDirectionCallBack
     */
    public void route(@NonNull List<LatLng> points, @NonNull WeDirectionCallBack weDirectionCallBack) {
        if (!WeMap.hasInstance()) {
            throw new WeMapConfigurationException();
        }
        if (points == null || points.size() < 2) {
            throw new WeDirectionException("dan duong phai truyen it nhat 2 diem!");
        }
        String searchURL = buildDirectionURL(points, null);
        FetchDataTask fetchDataTask = new FetchDataTask(weDirectionCallBack);
        fetchDataTask.execute(searchURL);
        fetchDataTask.getStatus();
    }

    
    /** 
     * Dan duong phai truyen it nhat 2 diem!
     * @param points
     * @param weDirectionOptions
     * @param weDirectionCallBack
     */
    public void route(@NonNull List<LatLng> points, @NonNull WeDirectionOptions weDirectionOptions, @NonNull WeDirectionCallBack weDirectionCallBack) {
        if (!WeMap.hasInstance()) {
            throw new WeMapConfigurationException();
        }
        if (points == null || points.size() < 2) {
            throw new WeDirectionException("dan duong phai truyen it nhat 2 diem!");
        }
        String directionURL = buildDirectionURL(points, weDirectionOptions);
        FetchDataTask fetchDataTask = new FetchDataTask(weDirectionCallBack);
        fetchDataTask.execute(directionURL);
        fetchDataTask.getStatus();
    }

    public interface WeDirectionCallBack {
        void onReady(WeDirectionResponse weDirectionResponse);
    }

    private class FetchDataTask extends AsyncTask<String, Void, String> {
        private WeDirectionCallBack weDirectionCallBack;

        public FetchDataTask(WeDirectionCallBack weDirectionCallBack) {
            this.weDirectionCallBack = weDirectionCallBack;
        }

        @Override
        protected String doInBackground(String... params) {

            InputStream inputStream = null;
            String result = null;
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(params[0]);

            try {

                HttpResponse response = client.execute(httpGet);
                inputStream = response.getEntity().getContent();

                // convert inputstream to string
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);

                } else
                    result = "Failed to fetch data";

                return result;

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String dataFetched) {
            try {
                JSONObject result = new JSONObject(dataFetched);
                JSONArray paths = result.getJSONArray("paths");
                WeDirectionResponse weDirectionResponse = new WeDirectionResponse(paths.getJSONObject(0));
                weDirectionCallBack.onReady(weDirectionResponse);

            } catch (Exception e) {
                Log.i("App", "Error parsing data" + e.getMessage());
            }
        }


        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }

    }

    public static class WeDirectionOptions {
        private String vehicle = VEHICLE_CAR;
        private boolean instructions = false;

        public static String VEHICLE_CAR = "car";
        public static String VEHICLE_FOOT = "foot";
        public static String VEHICLE_BIKE = "BIKE";

        public WeDirectionOptions() {

        }

        public void setVehicle(String vehicle) {
            if (vehicle == VEHICLE_CAR || vehicle == VEHICLE_FOOT || vehicle == VEHICLE_BIKE)
                this.vehicle = vehicle;
        }

        public String getVehicle() {
            return this.vehicle;
        }

        public void setInstructions(boolean instructions) {
            this.instructions = instructions;
        }

        public boolean getInstructions() {
            return this.instructions;
        }

    }
    /** 
     * De bat dau su dung Wemap SDK vui long goi WeMap.getInstance(Context context, String accessToken) truoc khi tao View. Access token truong bat buoc truoc khi su dung cac dich vu cua WeMap.
     */
    public class WeDirectionException extends RuntimeException {
        
        public WeDirectionException() {
            super("\nDe bat dau su dung Wemap SDK vui long goi WeMap.getInstance(Context context, String accessToken) truoc khi"
                    + "tao View. Access token truong bat buoc truoc khi su dung cac dich vu cua WeMap.");
        }

        public WeDirectionException(@NonNull String message) {
            super(message);
        }
    }
}
