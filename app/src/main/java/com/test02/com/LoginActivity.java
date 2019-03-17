package com.test02.com;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail,etPassword;
    TextView tvRegister;
    Button btnLogin;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    private  TextView forgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail=(EditText)findViewById(R.id.et_email);
        etPassword=(EditText)findViewById(R.id.et_password);
        btnLogin=(Button) findViewById(R.id.btn_login);
        tvRegister=(TextView) findViewById(R.id.tv_register);
        forgotPassword=(TextView)findViewById(R.id.tvForgotPassword);

      firebaseAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);


        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user !=null){
            finish();
            startActivity(new Intent(LoginActivity.this,Dashboard.class));

        }

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            validate(etEmail.getText().toString(),etPassword.getText().toString());
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,PasswordActivity.class));
            }
        });

    }

    private  void validate(String etEmail, String etPassword)
    {
          progressDialog.setMessage("Please Wait");
          progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(etEmail,etPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                     progressDialog.dismiss();
                   // Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();
                    checkEmailVerification();
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private  void checkEmailVerification()
    {
        FirebaseUser firebaseUser= firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag= firebaseUser.isEmailVerified();

        if(emailflag)
        {
            finish();
            startActivity(new Intent(LoginActivity.this,Dashboard.class));
            Toast.makeText(this,"Welcome!",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}
