package hk.com.a4gtsstask;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LOGIN TAG";
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mEmailView.getText().toString();
                String pass = mPasswordView.getText().toString();
                if (validData(mail,pass))
                    signIn(mail,pass);
            }
        });


        Button mEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String mail = mEmailView.getText().toString();
                String pass = mPasswordView.getText().toString();
                if (validData(mail,pass))
                    createAccount(mail,pass);
            }
        });
    }

    private boolean validData(String mail, String pass) {

        if (mail.length()==0|pass.length()==0)
        {
            Toast.makeText(LoginActivity.this, R.string.blank_field, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!mail.contains("@"))
        {
            Toast.makeText(LoginActivity.this, R.string.mail_format_error, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    private void signIn(String mail, String pass) {

        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            FirebaseUser user = mAuth.getCurrentUser();
                            UpdateUI(user);
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void createAccount(String mail, String pass) {

        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            FirebaseUser user = mAuth.getCurrentUser();
                            UpdateUI(user);
                        }
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, R.string.register_failed,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void UpdateUI(FirebaseUser user) {

        if (user !=null)
        {
            Intent intent = new Intent(getApplication(),MainActivity.class);
            intent.putExtra("User",user.getEmail());
            startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

