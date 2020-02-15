package com.example.droidrun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.droidrun.classification.DoodleImgClassifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    /* Lets begin HACK36*/
    //Shubham732
    //VIVEk
    //Dobby
    public DoodleImgClassifier classifier; // complete image classification
    IntentFilter intentFilter;
    WifiManager wifiManager;
    WifiP2pManager wifiP2pManager;//manager
    WifiP2pManager.Channel channel;
    BroadcastReceiver bReceiver;//receiver
    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String [] deviceNameArray;
    WifiP2pDevice[] deviceArray;
    ListView listView;
    EditText etMsg;
    TextView tvMsg;
    static final int MESSAGE_READ=1;
    MainActivity.ServerClass serverClass;
    MainActivity.ClientClass clientClass;
    MainActivity.SendReceive sendReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        try {
            this.classifier = new DoodleImgClassifier(this);
        } catch (IOException e) {
            Log.e("MainActivity", "Cannot initialize tfLite model!", e);
            e.printStackTrace();
        }

        intentFilter=new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        wifiManager=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);
        bReceiver = new Wifip2pBreceiver(wifiP2pManager, channel,this);

        addFragment(new fragment_1(),true,"one");


    }
    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction ft = fManager.beginTransaction();

        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.frame_container, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    WifiP2pManager.ConnectionInfoListener connectionInfoListener=new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {

            final InetAddress groupOwnerAddress=info.groupOwnerAddress;
            if(info.groupFormed && info.isGroupOwner)
            {
                Toast.makeText(getApplicationContext(),"host",Toast.LENGTH_SHORT).show();
                serverClass=new MainActivity.ServerClass();
                serverClass.start();
                addFragment(new fragment_2(),true,"two");

            }else if(info.groupFormed){
                clientClass=new MainActivity.ClientClass(groupOwnerAddress);
                clientClass.start();
                addFragment(new fragment_2(),true,"two");

                //client
            }

        }
    };

    WifiP2pManager.PeerListListener peerListListener=new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if(!peerList.getDeviceList().equals(peers))
            {
                peers.clear();
                peers.addAll(peerList.getDeviceList());
                deviceNameArray=new String[peerList.getDeviceList().size()];
                deviceArray=new WifiP2pDevice[peerList.getDeviceList().size()];
                int index=0;
                for(WifiP2pDevice device:peerList.getDeviceList())
                {
                    deviceNameArray[index]=device.deviceName;
//                    Toast.makeText(getApplicationContext(),"no dev",Toast.LENGTH_SHORT).show();
                    deviceArray[index++]=device;
                }

                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,deviceNameArray);
                listView.setAdapter(adapter);
            }
            if(peers.size()==0)
            {
                Toast.makeText(getApplicationContext(),"no devices available currently",Toast.LENGTH_SHORT).show();
                return;
            }


        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(bReceiver, intentFilter);
    }
    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(bReceiver);
    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch(msg.what)
            {
                case MESSAGE_READ:
                    byte[] readBuffer=(byte[]) msg.obj;
                    String Msg=new String(readBuffer,0,msg.arg1);
                    tvMsg.setText(Msg);
                    break;

            }

            return true;
        }
    });

    public class ServerClass extends Thread{
        Socket socket;
        ServerSocket serverSocket;

        @Override
        public void run()
        {
            try{
                serverSocket=new ServerSocket(8888);
                socket=serverSocket.accept();

                sendReceive=new MainActivity.SendReceive(socket);
                sendReceive.start();

            }catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }
    public class SendReceive extends Thread{
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public SendReceive(Socket skt)
        {
            socket=skt;
            try {
                inputStream=socket.getInputStream();
                outputStream=socket.getOutputStream();
            } catch (IOException e) {

                Toast.makeText(getApplicationContext(),"GADBAD",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            byte[] buffer=new byte[1024];
            int bytes;
            while(socket!=null)
            {
                try {
                    bytes=inputStream.read(buffer);
                    if(bytes>0)
                    {
                        handler.obtainMessage(MESSAGE_READ,bytes,-1,buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        //ye changekia h

        public void write(final byte[] bytes) {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        outputStream.write(bytes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }



    }

    public class ClientClass extends Thread{
        Socket socket;
        String hostAdd;
        public ClientClass(InetAddress hostAddress)
        {
            hostAdd=hostAddress.getHostAddress();
            socket=new Socket();
        }

        @Override
        public void run()
        {
            try {
                socket.connect(new InetSocketAddress(hostAdd,8888),500);
                sendReceive=new MainActivity.SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



}
