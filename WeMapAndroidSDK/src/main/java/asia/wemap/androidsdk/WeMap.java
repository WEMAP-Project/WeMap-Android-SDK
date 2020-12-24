package asia.wemap.androidsdk;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import com.mapbox.mapboxsdk.Mapbox;

public class WeMap {

    private static Mapbox MBINSTANCE;

    private static WeMap INSTANCE;

    private Context context;
    @Nullable
    private String accessToken;

    
    /** 
     * @param context
     * @param accessToken
     * @return WeMap
     */
    @UiThread
    @NonNull
    public static synchronized WeMap getInstance(@NonNull Context context, @Nullable String accessToken) {
        INSTANCE = new WeMap(context, accessToken);
        return INSTANCE;
    }

    public WeMap(@NonNull Context context, @Nullable String accessToken){
        MBINSTANCE = Mapbox.getInstance(context, "1");
        this.context = context;
        this.accessToken = accessToken;
    }

    
    /** 
     * @return boolean
     */
    @Nullable
    public static boolean hasInstance() {
        return Mapbox.hasInstance();
    }

    
    /** 
     * @return Context
     */
    public static Context getApplicationContext(){
        return INSTANCE.context;
    }

    
    /** 
     * @param accessToken
     */
    public static void setAccessToken(String accessToken) {
        INSTANCE.accessToken = accessToken;
    }

    
    /** 
     * @return String
     */
    public static String getAccessToken() {
        return INSTANCE.accessToken;
    }
}
