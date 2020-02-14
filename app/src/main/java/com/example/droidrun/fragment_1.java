package com.example.droidrun;

import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

public class fragment_1 extends Fragment {

    public MainActivity mainActivity;
    Button enableWifi,discover;

    public fragment_1(){
    //empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.mainActivity=(MainActivity)getActivity();
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        enableWifi=view.findViewById(R.id.enablewifi);
        discover=view.findViewById(R.id.discover);
        mainActivity.listView=view.findViewById(R.id.peerlist);



        enableWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mainActivity.wifiManager.isWifiEnabled())
                    mainActivity.wifiManager.setWifiEnabled(true);
                else
                    mainActivity.wifiManager.setWifiEnabled(false);

            }
        });

        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //discovering started
                mainActivity.manager.discoverPeers(mainActivity.channel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getActivity(),"devices discovered",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Toast.makeText(getActivity(),"discovery failed"+reasonCode,Toast.LENGTH_SHORT).show();

                    }
                });


            }

        });
        mainActivity.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //filling the list with available wifi devices

                final WifiP2pDevice device = mainActivity.deviceArray[position];
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;

                mainActivity.manager.connect(mainActivity.channel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getActivity(),"Connected to:"+device.deviceName,Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(int reason) {

                        Toast.makeText(getActivity(),"Connection Failed!",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
        return view;
    }




}

