package com.reactlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class AndroidPermissionModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public AndroidPermissionModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "AndroidPermission";
    }

    @ReactMethod
    public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
        // TODO: Implement some actually useful functionality
        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }

    @ReactMethod
    public void requestPermission(Promise promise) {
        Context context = getReactApplicationContext();
        Activity activity = getCurrentActivity();

        try {
            boolean fineLocationPermissionApproved = ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED;

            boolean coarseLocationPermissionApproved = ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED;

            boolean needPermission = (!fineLocationPermissionApproved || !coarseLocationPermissionApproved)
                    && activity != null;

            if (needPermission) {
                ActivityCompat.requestPermissions(
                        activity,
                        new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        },
                        1
                );
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                boolean backgroundLocationPermissionApproved = ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;

                if (!backgroundLocationPermissionApproved && activity != null) {
                    ActivityCompat.requestPermissions(
                            activity,
                            new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            1
                    );
                }
            }

            promise.resolve(true);
        } catch (Exception e) {
            promise.reject(e);
        }
    }
}
