package asia.wemap.androidsdk;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import asia.wemap.androidsdk.exceptions.WeMapConfigurationException;
import asia.wemap.androidsdk.geometry.LatLng;
import asia.wemap.androidsdk.geometry.LatLngBounds;

public class WeSearch {

    public WeSearch() {

    }

    private String buildSearchURL(String query, WeSearchOptions weSearchOptions) {
        String access_token = WeMap.getAccessToken();
        try {
            String searchURL = String.format("%s?key=%s&text=%s", WEMAP_SEARCH_API, access_token, URLEncoder.encode(query, "utf-8"));
            if (weSearchOptions != null) {
                int size = weSearchOptions.getSize();
                if (size > 0 && size < 25) {
                    searchURL = String.format("%s&size=%s", searchURL, String.valueOf(size));
                }
                LatLng focusPoint = weSearchOptions.getFocusPoint();
                if (focusPoint != null) {
                    searchURL = String.format("%s&location.lat=%s&location.lon=%s", searchURL, focusPoint.getLatitude(), focusPoint.getLongitude());
                }
                LatLngBounds latLngBounds = weSearchOptions.getLatLngBounds();
                if (latLngBounds != null) {
                    searchURL = String.format("%s&bbox.min_lon=%s&bbox.max_lon=%s&bbox.min_lat=%s&bbox.max_lat=%s", searchURL, latLngBounds.getLonWest(), latLngBounds.getLonEast(), latLngBounds.getLatSouth(), latLngBounds.getLatNorth());
                }
            }
            return searchURL;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String buildReverseURL(LatLng latlng, WeSearchOptions weSearchOptions) {
        String access_token = WeMap.getAccessToken();
        String reverseURL = String.format("%s?key=%s&point.lat=%s&point.lon=%s", WEMAP_REVERSE_API, access_token, latlng.getLatitude(), latlng.getLongitude());
        if (weSearchOptions != null) {
            int size = weSearchOptions.getSize();
            if (size > 0 && size < 25) {
                reverseURL = String.format("%s&size=%s", reverseURL, String.valueOf(size));
            }
        }
        return reverseURL;
    }

    private String WEMAP_SEARCH_API = "https://apis.wemap.asia/geocode-1/search";
    private String WEMAP_REVERSE_API = "https://apis.wemap.asia/geocode-1/reverse";


    public void search(@NonNull String query, @NonNull WeSearchCallBack weSearchCallBack) {
        if (!WeMap.hasInstance()) {
            throw new WeMapConfigurationException();
        }
        String searchURL = buildSearchURL(query, null);
        FetchDataTask fetchDataTask = new FetchDataTask(weSearchCallBack);
        fetchDataTask.execute(searchURL);
        fetchDataTask.getStatus();
    }

    public void search(@NonNull String query, @NonNull WeSearchOptions weSearchOptions, @NonNull WeSearchCallBack weSearchCallBack) {
        if (!WeMap.hasInstance()) {
            throw new WeMapConfigurationException();
        }
        String searchURL = buildSearchURL(query, weSearchOptions);
        FetchDataTask fetchDataTask = new FetchDataTask(weSearchCallBack);
        fetchDataTask.execute(searchURL);
        fetchDataTask.getStatus();
    }

    public void reverse(LatLng latlng, WeSearchCallBack weSearchCallBack) {
        if (!WeMap.hasInstance()) {
            throw new WeMapConfigurationException();
        }
        String searchURL = buildReverseURL(latlng, null);
        FetchDataTask fetchDataTask = new FetchDataTask(weSearchCallBack);
        fetchDataTask.execute(searchURL);
        fetchDataTask.getStatus();
    }

    public void reverse(LatLng latlng, WeSearchOptions weSearchOptions, WeSearchCallBack weSearchCallBack) {
        if (!WeMap.hasInstance()) {
            throw new WeMapConfigurationException();
        }
        String searchURL = buildReverseURL(latlng, weSearchOptions);
        FetchDataTask fetchDataTask = new FetchDataTask(weSearchCallBack);
        fetchDataTask.execute(searchURL);
        fetchDataTask.getStatus();
    }

    public interface WeSearchCallBack {
        void onReady(List<WePlace> wePlaces);
    }

    private class FetchDataTask extends AsyncTask<String, Void, String> {
        private WeSearchCallBack weSearchCallBack;

        public FetchDataTask(WeSearchCallBack weSearchCallBack) {
            this.weSearchCallBack = weSearchCallBack;
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
            List<WePlace> wePlaces = new ArrayList<WePlace>();
            try {
                JSONObject result = new JSONObject(dataFetched);
                JSONArray features = result.getJSONArray("features");
                for (int i = 0; i < features.length(); i++) {
                    wePlaces.add(new WePlace(features.getJSONObject(i).toString()));
                }
                weSearchCallBack.onReady(wePlaces);

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

    public static class WeSearchOptions {
        private int size;
        private LatLng focusPoint;
        private LatLngBounds latLngBounds;

        public WeSearchOptions() {

        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getSize() {
            return this.size;
        }

        public void setFocusPoint(LatLng focusPoint) {
            this.focusPoint = focusPoint;
        }

        public LatLng getFocusPoint() {
            return this.focusPoint;
        }

        public void setLatLngBounds(LatLngBounds latLngBounds) {
            this.latLngBounds = latLngBounds;
        }

        public LatLngBounds getLatLngBounds() {
            return this.latLngBounds;
        }
    }
}
