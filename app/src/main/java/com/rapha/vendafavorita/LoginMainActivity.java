package com.rapha.vendafavorita;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.rapha.vendafavorita.analitycs.AnalitycsGoogle;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;


public class LoginMainActivity extends AppCompatActivity {

    private ExtendedFloatingActionButton efabFace, efabGoogle;
    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private AnalitycsGoogle analitycsGoogle;

    private static final int RC_SIGN_IN_ADM = 67;

    private ProgressBar pb_login_main;
    private String intentExtraAdm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_main);
        efabGoogle = (ExtendedFloatingActionButton) findViewById(R.id.google_login_main);
        efabFace = (ExtendedFloatingActionButton) findViewById(R.id.face_login_main);
        auth = FirebaseAuth.getInstance();
        pb_login_main = (ProgressBar) findViewById(R.id.pb_login_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("978500802251-fho2o9t3kdnpd9kmb8l00bl9bqicliap.apps.googleusercontent.com")
                .requestProfile()
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("adm");
            //The key argument here must match that used in the other activity
            intentExtraAdm = value;
            if(value != null) {
                Log.d("TesteLogin", "Login activity Extras: " + value);
            }

        }



        analitycsGoogle = new AnalitycsGoogle(this);

        efabFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb_login_main.setVisibility(View.VISIBLE);
            }
        });

        efabGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb_login_main.setVisibility(View.VISIBLE);
                signIn();
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @NotNull FirebaseAuth firebaseAuth) {
                auth = firebaseAuth;
                FirebaseUser user = auth.getCurrentUser();
                if(user != null) finish();
            }
        };

    }

    @Override
    protected void onResume() {
        auth.addAuthStateListener(mAuthStateListener);
        super.onResume();
    }

    @Override
    protected void onStop() {
        auth.removeAuthStateListener(mAuthStateListener);
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TesteLogin", "activy result");
        if (requestCode == 587) {
            Log.d("TesteLogin", "google result");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account == null) {
                    Log.d("TesteLogin", "Google acount null");
                    return;
                }
                Log.d("TesteLogin", "Google acount: " + account.getDisplayName());
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.e("TesteLogin", e.getMessage());
                e.printStackTrace();
            }
        } else if (requestCode == RC_SIGN_IN_ADM) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                //user = FirebaseAuth.getInstance().getCurrentUser();
                //ADMINISTRADOR = true;
                //abrirMensagem();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        } else {
        }

    }

    private void loginAdmin() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN_ADM);
    }

    private void loginUsuarioAnonimo() {
        auth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d("TAG_Sucess", "signInAnonymously:success");
                            FirebaseUser user = auth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //telaInicialErro("Erro ao carregar lista de produtos...\n Tente Novamente");
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private String getFotoUser (FirebaseUser user) {
        if (user == null) return "";

        for(int xis = 0; xis < user.getProviderData().size(); xis++) {
            try {
                String xs = user.getProviderData().get(xis).getPhotoUrl().toString() + "";

                if (user.getProviderData().get(xis).getProviderId().equals("facebook.com")) {
                    xs = user.getProviderData().get(xis).getPhotoUrl().toString() + "?type=large&redirect=true&width=500&height=500";
                }
                return xs;
            } catch (NullPointerException e) {
                return "";
            }

        }

        return "";

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d("TesteLogin", "Google, firebase auth funcao");

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult != null) {
                            Log.d("TesteLogin", "Google, sucesso");
                            FirebaseUser user = authResult.getUser();
//                            Log.d("TesteLogin", "Goglle sucess");
                            String foto = getFotoUser(user);
                            analitycsGoogle.logLoginGoogleEvent(true, user.getDisplayName(), user.getUid(), foto, user.getEmail(), user.getPhoneNumber(), Constantes.CONTROLE_VERSAO_USUARIO);
                            updateUI(user);
                        } else {
                            Log.d("TesteLogin", "GoogleErro");
                            updateUI(null);
                        }
                    }
                });
    }


    private void updateUI(FirebaseUser firebaseUser) {
//        Timber.d("UpdateUI");
        if (firebaseUser != null) {
//            Log.d("TesteLogin", "UpdateUISucesso");
            //tirar container login e subir a lista de produtos
            if(intentExtraAdm != null) {
                startActivity(new Intent(this, MainActivity.class).putExtra("adm", intentExtraAdm));
                finish();
            } else {

                startActivity(new Intent(this, MainActivity.class));
                finish();
            }

        } else {
            pb_login_main.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Erro ao fazer login", Toast.LENGTH_LONG).show();
        }
    }

    private void signIn() {
        Log.d("TesteLogin", "sign in");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 587);
    }

}
