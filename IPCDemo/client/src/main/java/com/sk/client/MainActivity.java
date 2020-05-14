package com.sk.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sk.ipc.IPCService;
import com.sk.ipc.SkIPC;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SkIPC.connect(this, "com.sk.ipcdemo",IPCService.IPCService0.class);
    }

    public void getData(View view){
        IDataProvider provider =SkIPC.getInstanceWithNormal(IPCService.IPCService0.class,IDataProvider.class);
        Toast.makeText(this,provider.getData().toString(),Toast.LENGTH_SHORT).show();
    }
}
