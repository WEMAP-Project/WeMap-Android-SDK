package asia.wemap.androidsdk.permissions;
import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mapbox.android.core.permissions.PermissionsListener;

import java.util.List;

public class PermissionsManager {
    com.mapbox.android.core.permissions.PermissionsManager PM;
    boolean grantedPM = false;

    public PermissionsManager(Context ctx, Activity activity){
        PM = new com.mapbox.android.core.permissions.PermissionsManager(new com.mapbox.android.core.permissions.PermissionsListener(){
            @Override
            public void onExplanationNeeded(List<String> permissionsToExplain) {
                Toast.makeText(ctx, "This app needs location permissions to show its functionality.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionResult(boolean granted) {
                grantedPM = granted;
                if (!granted){
                    Toast.makeText(ctx, "You didn\\'t grant location permissions.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public boolean isGranted(){
        return this.grantedPM;
    }
}
