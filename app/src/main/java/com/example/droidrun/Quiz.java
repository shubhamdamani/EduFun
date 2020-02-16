package com.example.droidrun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Quiz extends AppCompatActivity {

    Button submit;
    TextView q1,q2,q3,q4,q5;
    EditText e1,e2,e3,e4,e5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        submit=findViewById(R.id.submit);
        q1=findViewById(R.id.q1);
        q2=findViewById(R.id.q2);
        q3=findViewById(R.id.q3);
        q4=findViewById(R.id.q4);

        e1=findViewById(R.id.a1);
        e2=findViewById(R.id.a2);
        e3=findViewById(R.id.a3);
        e4=findViewById(R.id.a4);


        Intent i=getIntent();

        String a=i.getStringExtra("s1");
        String b=i.getStringExtra("s2");
        String c=i.getStringExtra("s3");
        String d=i.getStringExtra("s4");


        List<String> list = new ArrayList<>();
        list.add("is");
        list.add("the");
        list.add("a");
        list.add("on");

        // String ans1 = null,ans2 = null,ans3 = null,ans4=null;

        StringBuilder tmp=new StringBuilder();
        StringBuilder tmp1=new StringBuilder();
        StringBuilder tmp2=new StringBuilder();
        StringBuilder tmp3=new StringBuilder();
        int cnt=0;

        List<String> ls=new ArrayList<>();
        for(String w:a.split("\\s",0))
        {
            ls.add(w);
        }

        int rd = new Random().nextInt(ls.size()-1);
        String ans1=ls.get(rd).toLowerCase();

        for(String w:a.split("\\s",0)) {
            if (cnt == rd) {
                tmp.append(" ___");
            } else {
                tmp.append(" " + w);
            }
            cnt++;
        }
        ls.clear();
        cnt=0;

        for(String w:b.split("\\s",0))
        {
            ls.add(w);
        }
        rd = new Random().nextInt(ls.size()-1);
        String ans2=ls.get(rd).toLowerCase();

        for(String w:b.split("\\s",0)) {
            if (cnt == rd) {
                tmp1.append(" ___");
            } else {
                tmp1.append(" " + w);
            }
            cnt++;
        }
        ls.clear();
        cnt=0;

        for(String w:c.split("\\s",0))
        {
            ls.add(w);
        }
        rd = new Random().nextInt(ls.size()-1);
        String ans3=ls.get(rd).toLowerCase();

        for(String w:c.split("\\s",0)) {
            if (cnt == rd) {
                tmp2.append(" ___");
            } else {
                tmp2.append(" " + w);
            }
            cnt++;
        }
        ls.clear();
        cnt=0;

        for(String w:d.split("\\s",0))
        {
            ls.add(w);
        }
        rd = new Random().nextInt(ls.size()-1);
        String ans4=ls.get(rd).toLowerCase();

        for(String w:d.split("\\s",0)) {
            if (cnt == rd) {
                tmp3.append(" ___");
            } else {
                tmp3.append(" " + w);
            }
            cnt++;
        }


        q1.setText(tmp);
        q2.setText(tmp1);
        q3.setText(tmp2);
        q4.setText(tmp3);


        final String finalAns = ans1;
        final String finalAns1 = ans2;
        final String finalAns2 = ans3;
        final String finalAns3 = ans4;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String r1=e1.getText().toString();
                String r2=e2.getText().toString();
                String r3=e3.getText().toString();
                String r4=e4.getText().toString();
                List<String> li=new ArrayList<>();
                if(finalAns.equals(r1.toLowerCase()))
                {

                }else{
                    li.add("1");


                    e1.setText("ANSWER : "+finalAns);
                }
                if(r2.toLowerCase().equals(finalAns1))
                {

                }else{
                    li.add("1");


                    e2.setText("ANSWER : "+finalAns1);
                }
                if(r3.toLowerCase().equals(finalAns2))
                {

                }else{

                    li.add("1");

                    e3.setText("ANSWER : "+finalAns2);
                }
                if(r4.toLowerCase().equals(finalAns3))
                {

                }else{

                    e4.setText("ANSWER : "+finalAns3);
                    li.add("1");
                }

                if(li.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"ALL CORRECT",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "CHECK THE CORRECT ANSWERS", Toast.LENGTH_SHORT).show();
                }


            }
        });








    }


}
