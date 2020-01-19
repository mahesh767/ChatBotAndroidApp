package com.example.chatbot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditText firstname;
    EditText lastname;
    EditText email;
    EditText password;
    Button  register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstname =findViewById(R.id.firstnameedit);
        lastname=findViewById(R.id.lastnameedit);
        email=findViewById(R.id.registeremailedit);
        password=findViewById(R.id.registerpasswordedit);
        register=findViewById(R.id.registersubmit);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userfirstname=firstname.getText().toString();
                String userlastname=lastname.getText().toString();
                String useremail=email.getText().toString();
                String userpassword=password.getText().toString();
                final JSONObject data=new JSONObject();
                try {
                    data.put("firstname", userfirstname);
                    data.put("lastname",userlastname);
                    data.put("email",useremail);
                    data.put("password",userpassword);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                String url="http://192.168.1.164:8080/addUser" ;
                RequestQueue mqueue= Volley.newRequestQueue(RegisterActivity.this);
                JsonObjectRequest jsonobjectRequest=new JsonObjectRequest(Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse==null)
                        {
                            Toast.makeText(RegisterActivity.this,"Succesfully registered",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(RegisterActivity.this,MainActivity.class);
                            startActivity(i);
                        }
                    }
                });
                mqueue.add(jsonobjectRequest);
            }
        });

    }
}
