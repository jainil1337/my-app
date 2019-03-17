package com.test02.com;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText etName,etMail,etPass;
    Button btRegister;
    String name,email,pass;
   FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName=(EditText)findViewById(R.id.et_name);
        etMail=(EditText)findViewById(R.id.et_reg_email);
        etPass=(EditText)findViewById(R.id.et_reg_password);
        btRegister=(Button)findViewById(R.id.btn_register);

        firebaseAuth=FirebaseAuth.getInstance();


        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(validate())
               {
                   //upload data to the database
                   String user_email=etMail.getText().toString().trim();
                   String user_password=etPass.getText().toString().trim();

                   firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if(task.isSuccessful())
                           {
                              sendEmailVerification();
                           }
                           else
                           {
                               Toast.makeText(RegisterActivity.this,"Registration Failed", Toast.LENGTH_SHORT).show();
                           }

                       }
                   });
               }
            }

        });
    }
    public Boolean validate()
    {
        Boolean result = false;


        name=etName.getText().toString();
        email=etMail.getText().toString();
        pass=etPass.getText().toString();

        if(name.isEmpty() || email.isEmpty() || pass.isEmpty() )
        {
            Toast.makeText(this,"Please Enter all the details",Toast.LENGTH_SHORT).show();
        }
        else
        {
            result=true;
        }
        return result;
    }

private void sendEmailVerification()
{
    final FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
    if(firebaseUser!=null)
    {
        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    sendUserData();
                    Toast.makeText(RegisterActivity.this,"Successfully Registerd,Verification mail sent",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                }else
                {
                    Toast.makeText(RegisterActivity.this,"Verification mail hasn't been sent!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
private  void sendUserData()
{
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference myRef= firebaseDatabase.getReference(firebaseAuth.getUid());
   UserProfile userProfile=new UserProfile(name,email);
    myRef.setValue(userProfile);

}
}
