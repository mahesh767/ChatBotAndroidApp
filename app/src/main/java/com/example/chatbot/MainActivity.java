package com.example.chatbot;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TimePicker;
        import android.widget.Toast;

        import com.android.volley.NetworkResponse;
        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.JsonObjectRequest;
        import com.android.volley.toolbox.JsonRequest;
        import com.android.volley.toolbox.Volley;

        import org.json.JSONException;
        import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button login,register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=findViewById(R.id.loginemailedit);
        password=findViewById(R.id.loginpasswordedit);
        register=findViewById(R.id.loginregisterbutton);



        login=findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userinputemail=email.getText().toString();
                final String userinputpassword=password.getText().toString();
                //Toast.makeText(MainActivity.this,userinputemail+" "+userinputpassword,Toast.LENGTH_LONG).show();
                function(userinputemail,userinputpassword);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
            }
        });





    }
    public void function(final String userinputemail, final String userinputpassword)
    {
        // String url="http://192.168.1.164:8080/checkLogin?email="+userinputemail;
        String url="http://192.168.1.164:8080/checkLogin?email="+userinputemail;
        System.out.println("function called");
        RequestQueue mqueue= Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(MainActivity.this,response.toString(),Toast.LENGTH_LONG).show();
                if (response == null) {
                    Toast.makeText(MainActivity.this,"Email is not registered",Toast.LENGTH_LONG).show();
                    return;
                } else {
                    try{
                        String email = response.getString("email");
                        String password = response.getString("password");
                        if (userinputemail.equals(email) && userinputpassword.equals(password)) {
                            final SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", userinputemail);
                            editor.commit();
                            Intent i=new Intent(getApplicationContext(),Message.class);
                            startActivity(i);

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Email or password is incorrect",Toast.LENGTH_SHORT).show();
                        }
                    }catch(JSONException e)
                    {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,"json exception",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    if(error.networkResponse==null)
                    {
                        Toast.makeText(MainActivity.this,"Email not registered",Toast.LENGTH_SHORT).show();

                    }
            }
        });
        mqueue.add(jsonObjectRequest);
    }
}
