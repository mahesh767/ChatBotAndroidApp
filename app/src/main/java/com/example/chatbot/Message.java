package com.example.chatbot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class Message extends AppCompatActivity {

    EditText userinput;
    Button sendbutton,loadprevmessages,logout;
    LinearLayout messagebody;

    String usermessage="";
    String serverresponse="";
    TextView usermessagetextView;
    TextView servmessagetextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userinput=findViewById(R.id.usermessage);
        sendbutton=findViewById(R.id.messagesubmit);
        loadprevmessages=findViewById(R.id.loadprevmessages);
        logout=findViewById(R.id.logout);
        messagebody=findViewById(R.id.messagebody);


        //send button onclick listener

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usermessagetextView=new TextView(getApplicationContext());
                servmessagetextView=new TextView(getApplicationContext());
                usermessagetextView.setBackgroundColor(getResources().getColor(R.color.green));
                usermessagetextView.setTextSize(20);
                usermessagetextView.setTextColor(getResources().getColor(R.color.black));
                usermessagetextView.setGravity(Gravity.RIGHT);
                usermessagetextView.setPadding(8,4,8,4);

                servmessagetextView.setBackgroundColor(getResources().getColor(R.color.white));
                servmessagetextView.setPadding(8,4,8,4);
                servmessagetextView.setTextSize(20);


                RequestQueue mqueue= Volley.newRequestQueue(Message.this);
                  usermessage=userinput.getText().toString();
                  userinput.setText("");
                String url="http://192.168.1.164:8080/getMessageResponse?messageId="+usermessage;
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {



                            System.out.println(response.toString());

                             {
                                serverresponse = response.getString("messages");
                                System.out.println(serverresponse);
                                String splitresponse[]=serverresponse.split("/");

                                int index=(int)(Math.random()*splitresponse.length);
                                String randomservresponse=splitresponse[index];
                                usermessagetextView.setText(usermessage);
                                messagebody.addView(usermessagetextView);


                                servmessagetextView.setText(randomservresponse);
                                messagebody.addView(servmessagetextView);
                            }






                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_SHORT);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse errorocurred=error.networkResponse;
                           if(errorocurred==null)
                           {
                               serverresponse="Sorry I could not help you with that";
                               usermessagetextView.setText(usermessage);
                               servmessagetextView.setText(serverresponse);
                               messagebody.addView(usermessagetextView);
                               messagebody.addView(servmessagetextView);


                           }
                    }
                });
                mqueue.add(jsonObjectRequest);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences=getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });

        loadprevmessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=getSharedPreferences("login",Context.MODE_PRIVATE).getString("email","DEFAULT");
                System.out.println(email);
                String url="http://192.168.1.164:8080/getPrevMessages?email="+email;
                RequestQueue mqueue=Volley.newRequestQueue(Message.this);
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String messages=response.getString("messages");
                            System.out.println(messages);
                            String splitmessages[]=messages.split(",");
                            for(int i=0;i<splitmessages.length;i++)
                            {
                                String againsplitmessages[]=splitmessages[i].split("/");
                                    if(againsplitmessages.length!=1) {
                                        usermessagetextView = new TextView(getApplicationContext());
                                        servmessagetextView = new TextView(getApplicationContext());
                                        usermessagetextView.setBackgroundColor(getResources().getColor(R.color.green));
                                        usermessagetextView.setTextSize(20);
                                        usermessagetextView.setTextColor(getResources().getColor(R.color.black));
                                        usermessagetextView.setGravity(Gravity.RIGHT);
                                        usermessagetextView.setPadding(8, 4, 8, 4);
                                        usermessagetextView.setText(againsplitmessages[0]);
                                        servmessagetextView.setBackgroundColor(getResources().getColor(R.color.white));
                                        servmessagetextView.setPadding(8, 4, 8, 4);
                                        servmessagetextView.setTextSize(20);

                                        servmessagetextView.setText(againsplitmessages[1]);
                                        messagebody.addView(usermessagetextView);
                                        messagebody.addView(servmessagetextView);
                                    }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Message.this,"something wrong occured",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.networkResponse);
                        System.out.println(error.getMessage());
                    }
                });
                mqueue.add(jsonObjectRequest);
            }
        });

    }
}
