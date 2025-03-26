package dev.projectinvicta.capacitorgoogleauth;

import android.app.Activity;
import android.content.Context;
//import android.credentials.CredentialManager;
//import android.credentials.GetCredentialException;
//import android.credentials.GetCredentialResponse;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.GetPasswordOption;
import androidx.credentials.GetPublicKeyCredentialOption;
import androidx.credentials.PublicKeyCredential;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
//import com.google.android.libraries.identity.googleid.CustomCredential;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CapacitorGoogleAuth {
    private static final String LOG_TAG = "CapacitorGoogleAuth::";
    private static final CancellationSignal CANCELLATION_SIGNAL = new CancellationSignal();
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    private final Activity activity;
    private final Context context;
    private final CredentialManager credentialManager;
    private final GetSignInWithGoogleOption signInWithGoogleButtonOption;
    private String webClientId = "875126660467-8h9p8qj7irmar9stmakhpr4gajqmumgf.apps.googleusercontent.com";

    public CapacitorGoogleAuth(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        this.credentialManager = CredentialManager.create(context);
        this.signInWithGoogleButtonOption = new GetSignInWithGoogleOption.Builder(webClientId)
                // .setNonce(<nonce string to use when generating a Google ID token>)
                .build();
    }

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }

    public void initiateSigninUsingCredentialManager() {
        GetCredentialRequest request = new GetCredentialRequest.Builder()
          .addCredentialOption(signInWithGoogleButtonOption)
          .build();

          credentialManager.getCredentialAsync(
                    // Use activity based context to avoid undefined
                    // system UI launching behavior
                    context,
                    request,
          CANCELLATION_SIGNAL,
          EXECUTOR,
        new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
            @Override
            public void onResult(GetCredentialResponse result) {
                handleSignIn(result);
            }

            @Override
            public void onError(GetCredentialException e) {
                handleFailure(e);
            }
        }
            );
    }

    private void handleSignIn(GetCredentialResponse result) {
    // Handle the successfully returned credential.
    Credential credential = result.getCredential();

    if (credential instanceof CustomCredential) {
      // if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_SIWG_CREDENTIAL.equals(credential.getType())) {
      if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
        try {
          GoogleIdTokenCredential customCred = GoogleIdTokenCredential.createFrom(credential.getData());
            // Extract the required credentials and complete the
            // authentication as per the federated sign in or any external
            // sign in library flow

            // You can use the members of googleIdTokenCredential directly for UX
            // purposes, but don't use them to store or control access to user
            // data. For that you first need to validate the token:
            // pass googleIdTokenCredential.getIdToken() to the backend server.
            // GoogleIdTokenVerifier verifier = ... // see validation instructions
            // GoogleIdToken idToken = verifier.verify(idTokenString);
        } catch (Exception e) {
            // Unlikely to happen. If it does, you likely need to update the
            // dependency version of your external sign-in library.
            Log.e(LOG_TAG, "Failed to parse an GoogleIdTokenCredential", e);
        }
      } else {
          // Catch any unrecognized custom credential type here.
          Log.e(LOG_TAG, "Unexpected type of credential");
      }
    } else {
      // Catch any unrecognized credential type here.
      Log.e(LOG_TAG, "Unexpected type of credential");
    }
  }

  private void handleFailure(Exception e) {
        // TODO
  }

}
