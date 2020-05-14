package com.sk.ipcdemo;

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
        startService(new Intent(this, DataService.class));
        SkIPC.connect(this, IPCService.IPCService0.class);
    }


    public void getData(View view) {
        IDataProvider provider =SkIPC.getInstanceWithNormal(IPCService.IPCService0.class,IDataProvider.class);
        Toast.makeText(this,provider.getDataByUserId(1).toString(),Toast.LENGTH_SHORT).show();
    }
}
