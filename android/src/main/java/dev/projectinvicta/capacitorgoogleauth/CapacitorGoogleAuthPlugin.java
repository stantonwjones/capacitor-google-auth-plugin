package dev.projectinvicta.capacitorgoogleauth;

import android.content.Intent;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "CapacitorGoogleAuth",
requestCodes={Authorizer.REQUEST_AUTHORIZE})
public class CapacitorGoogleAuthPlugin extends Plugin {
    private static final String TAG = "CapacitorGoogleAuthPlugin";

//    private CapacitorGoogleAuth implementation;
    private Authorizer authorizer;
    private PluginCall lastCall;

    @PluginMethod
    public void authorize(PluginCall call) {
        this.lastCall = call;
        String value = call.getString("value");

        JSObject ret = new JSObject();
//      ret.put("value", implementation.echo(value));
//      implementation.initiateSigninUsingCredentialManager();
        this.authorizer.authorize((jsonResult) -> {
            jsonResult.ifPresent(call::resolve);
            return null;
        });
    }

    @Override
    public void load() {
//        this.implementation = new CapacitorGoogleAuth(getActivity(), getContext());
        this.authorizer = new Authorizer(getActivity(), getContext());
        Log.i(TAG, "CapacitorGoogleAuth loaded");
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "Handled activity result " + requestCode);
        JSObject user = this.authorizer.attemptToCompleteLogin(data);
        Log.i(TAG, user.toString());
        this.lastCall.resolve(user);
    }

//    @Override
//    public void handleOnResume() {
//        this.authorizer.authorize();
//    }
}
