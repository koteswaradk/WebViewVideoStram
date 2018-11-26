package servicetutorial.service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ReceiveAndSendDataBack extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_and_send_data_back);
        findViewById(R.id.button3).setOnClickListener(ReceiveAndSendDataBack.this);

        String name= getIntent().getStringExtra("name");
        String lat=  getIntent().getStringExtra("latitude");
        String lang= getIntent().getStringExtra("langitude");

        Toast.makeText(this,name+lat+lang,Toast.LENGTH_LONG).show();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button3 :
                Intent data = new Intent();
                data.putExtra("success", "don");
                setResult(RESULT_OK, data);

                finish();
                break;
        }

    }
}
