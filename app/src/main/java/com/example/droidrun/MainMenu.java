package com.example.droidrun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    Button multiplayer,singleplayer,inst,exi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        multiplayer=findViewById(R.id.multi);
        singleplayer=findViewById(R.id.single);
        inst=findViewById(R.id.inst);
        exi=findViewById(R.id.exit);

        multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainMenu.this,MainActivity.class);
                startActivity(i);
            }
        });

        singleplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainMenu.this,SinglePlayer.class);
                startActivity(i);
            }
        });

        inst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainMenu.this,Instruction.class);
                startActivity(i);

            }
        });

        exi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });

    }
}
