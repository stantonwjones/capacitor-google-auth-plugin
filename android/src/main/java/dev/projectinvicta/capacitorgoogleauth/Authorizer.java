package dev.projectinvicta.capacitorgoogleauth;
// https://developer.android.com/identity/authorization
// This may be a lot easier and will generate an access token

import static androidx.core.app.ActivityCompat.startIntentSenderForResult;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.google.android.gms.auth.api.identity.AuthorizationClient;
import com.google.android.gms.auth.api.identity.AuthorizationRequest;
import com.google.android.gms.auth.api.identity.AuthorizationResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class Authorizer {
    private final String TAG = "CGA AUTHORIZER";
    public static final int REQUEST_AUTHORIZE = 1337;
    private final Activity activity;
    private final Context context;
    private Intent authorizationResultIntent;
    private AuthorizationClient authorizationClient;
    public Authorizer(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        this.authorizationClient = Identity.getAuthorizationClient(this.context);
    }
    public void authorize(Function<Optional<JSObject>, Void> onAuthorized) {
        List<Scope> requestedScopes = Arrays.asList(new Scope(Scopes.EMAIL), new Scope(Scopes.PROFILE));
        AuthorizationRequest authorizationRequest = AuthorizationRequest.builder().setRequestedScopes(requestedScopes).build();
        this.authorizationClient
                .authorize(authorizationRequest)
                .addOnSuccessListener(
                        authorizationResult -> {
                            String authCode = authorizationResult.getServerAuthCode();
                            if (authCode != null && !authCode.isEmpty()) {
                                Log.i("AUTHORIZER AUTH CODE:", authorizationResult.getServerAuthCode());
                            }
                            if (authorizationResult.hasResolution()) {
                                // Access needs to be granted by the user
                                onAuthorized.apply(Optional.empty());
                                PendingIntent pendingIntent = authorizationResult.getPendingIntent();
                                try {
                                    startIntentSenderForResult(
                                            this.activity, pendingIntent.getIntentSender(),
                                            REQUEST_AUTHORIZE, null, 0, 0, 0, null);
                                } catch (IntentSender.SendIntentException e) {
                                    Log.e(TAG, "Couldn't start Authorization UI: " + e.getLocalizedMessage());
                                }
                            } else {
                                onAuthorized.apply(Optional.of(getJsonFromAuthorizationResult(authorizationResult)));
                            }
                        })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to authorize", e));
    }

    public JSObject attemptToCompleteLogin(Intent data) {
        try{
            AuthorizationResult authorizationResult = this.authorizationClient.getAuthorizationResultFromIntent(data);
            return getJsonFromAuthorizationResult(authorizationResult);
        } catch (ApiException e) {
            Log.e(TAG, "failed to get authorization result", e);
            return new JSObject().put("error", e.getMessage());
        }
    }

    private JSObject getJsonFromAuthorizationResult(AuthorizationResult authorizationResult) {
        JSObject loginResult = new JSObject();
        if (authorizationResult.getAccessToken() != null) {
            Log.i(TAG, "Access token: " + authorizationResult.getAccessToken());
            loginResult.put("accessToken", authorizationResult.getAccessToken());
        }
        if (authorizationResult.toGoogleSignInAccount() != null) {
            GoogleSignInAccount user = authorizationResult.toGoogleSignInAccount();
            loginResult.put("email", user.getEmail());
            loginResult.put("firstName", user.getGivenName());
            loginResult.put("lastName", user.getFamilyName());
            loginResult.put("profilePicture", user.getPhotoUrl());
            loginResult.put("id", user.getId());;
            loginResult.put("idToken", user.getIdToken());
        }
        Log.i(TAG, loginResult.toString());
        return loginResult;
    }
}