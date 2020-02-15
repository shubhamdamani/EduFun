package com.example.droidrun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Topic extends AppCompatActivity {

    TextView b1,b2,b3,b4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        b1=findViewById(R.id.tp1);
        b2=findViewById(R.id.tp2);
        b3=findViewById(R.id.tp3);
        b4=findViewById(R.id.tp4);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Topic.this,Article.class);
                String s=b1.getText().toString();
                i.putExtra("s",s);
                startActivity(i);


            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Topic.this,Article.class);
                String s=b2.getText().toString();
                i.putExtra("s",s);
                startActivity(i);


            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Topic.this,Article.class);
                String s=b3.getText().toString();
                i.putExtra("s",s);
                startActivity(i);


            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Topic.this,Article.class);
                String s=b4.getText().toString();
                i.putExtra("s",s);
                startActivity(i);


            }
        });

    }
}
