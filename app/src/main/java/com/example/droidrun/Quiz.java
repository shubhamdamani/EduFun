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

         String ans1 = null,ans2 = null,ans3 = null,ans4=null;

        StringBuilder tmp=new StringBuilder();
        int cnt=0;

        for(String w:a.split("\\s",0))
        {
            if(cnt>0)
            {
                tmp.append(" "+w);
                continue;
            }

            if(w.equals(list.get(0)))
            {
                String r=list.get(0);
                cnt++;
                tmp.append(" ___");
                ans1=r;
                list.remove(r);
                list.add(r);



            }else if(w.equals(list.get(1)))
            {
                String r=list.get(1);
                cnt++;
                ans1=r;
                tmp.append(" ___");
                list.remove(r);
                list.add(r);


            }else if(w.equals(list.get(2)))
            {
                String r=list.get(2);
                cnt++;
                ans1=r;
                tmp.append(" ___");
                list.remove(r);
                list.add(r);


            }else if(w.equals(list.get(3)))
            {
                String r=list.get(3);
                cnt++;
                ans1=r;
                tmp.append(" ___");
                list.remove(r);
                list.add(r);


            }
        }

        q1.setText(tmp);
        //tmp="";
        StringBuilder tmp1=new StringBuilder();
        int cnt1=0;

        for(String w:b.split("\\s",0))
        {
            if(cnt1>0)
            {
                tmp1.append(" "+w);
                continue;
            }

            if(w.equals(list.get(0)))
            {
                String r=list.get(0);
                cnt1++;
                tmp1.append(" ___");
                ans2=r;
                list.remove(r);
                list.add(r);



            }else if(w.equals(list.get(1)))
            {
                String r=list.get(1);
                cnt1++;
                ans2=r;
                tmp1.append(" ___");
                list.remove(r);
                list.add(r);


            }else if(w.equals(list.get(2)))
            {
                String r=list.get(2);
                cnt1++;
                ans2=r;
                tmp1.append(" ___");
                list.remove(r);
                list.add(r);


            }else if(w.equals(list.get(3)))
            {
                String r=list.get(3);
                cnt1++;
                ans2=r;
                tmp1.append(" ___");
                list.remove(r);
                list.add(r);


            }
        }

        q2.setText(tmp1 );

        StringBuilder tmp2=new StringBuilder();
        int cnt2=0;

        for(String w:c.split("\\s",0))
        {
            if(cnt2>0)
            {
                tmp2.append(" "+w);
                continue;
            }

            if(w.equals(list.get(0)))
            {
                String r=list.get(0);
                cnt2++;
                tmp2.append(" ___");
                ans3=r;
                list.remove(r);
                list.add(r);



            }else if(w.equals(list.get(1)))
            {
                String r=list.get(1);
                cnt2++;
                ans3=r;
                tmp2.append(" ___");
                list.remove(r);
                list.add(r);


            }else if(w.equals(list.get(2)))
            {
                String r=list.get(2);
                cnt2++;
                ans3=r;
                tmp2.append(" ___");
                list.remove(r);
                list.add(r);


            }else if(w.equals(list.get(3)))
            {
                String r=list.get(3);
                cnt2++;
                ans3=r;
                tmp2.append(" ___");
                list.remove(r);
                list.add(r);


            }
        }

        q3.setText(tmp2 );


        StringBuilder tmp3=new StringBuilder();
        int cnt3=0;

        for(String w:d.split("\\s",0))
        {
            if(cnt3>0)
            {
                tmp3.append(" "+w);
                continue;
            }

            if(w.equals(list.get(0)))
            {
                String r=list.get(0);
                cnt3++;
                tmp3.append(" ___");
                ans4=r;
                list.remove(r);
                list.add(r);



            }else if(w.equals(list.get(1)))
            {
                String r=list.get(1);
                cnt3++;
                ans4=r;
                tmp3.append(" ___");
                list.remove(r);
                list.add(r);


            }else if(w.equals(list.get(2)))
            {
                String r=list.get(2);
                cnt3++;
                ans4=r;
                tmp3.append(" ___");
                list.remove(r);
                list.add(r);


            }else if(w.equals(list.get(3)))
            {
                String r=list.get(3);
                cnt3++;
                ans4=r;
                tmp3.append(" ___");
                list.remove(r);
                list.add(r);


            }
        }

        q4.setText(tmp3 );

        final String finalAns = ans1;
        final String finalAns1 = ans2;
        final String finalAns2 = ans3;
        final String finalAns3 = ans4;
        final String finalAns4 = ans1;
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
                }
                if(r2.toLowerCase().equals(finalAns1))
                {

                }else{

                    li.add("2");
                }
                if(r3.toLowerCase().equals(finalAns2))
                {

                }else{

                    li.add("3");
                }
                if(r4.toLowerCase().equals(finalAns3))
                {

                }else{

                    li.add("4");
                }

                if(li.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"ALL CORRECT",Toast.LENGTH_SHORT).show();
                }else{
                    StringBuilder stringBuilder=new StringBuilder();
                    int siz=0;
                    while(siz<li.size())
                    {
                        stringBuilder.append(li.get(siz));
                        if(li.get(siz).equals("1"))
                        {
                            stringBuilder.append(" "+ finalAns);
                        }else if(li.get(siz).equals("2"))
                        {
                            stringBuilder.append(" "+ finalAns2);
                        }
                        else if(li.get(siz).equals("3"))
                        {
                            stringBuilder.append(" "+ finalAns3);
                        }
                        else{
                            stringBuilder.append(" "+ finalAns4);
                        }
                        siz++;
                    }
                    String disp=stringBuilder.toString();
                    Toast.makeText(getApplicationContext(),disp,Toast.LENGTH_SHORT).show();

                }
                Intent i=new Intent(Quiz.this,MainMenu.class);
                startActivity(i);

            }
        });







    }
}
