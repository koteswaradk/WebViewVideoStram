package servicetutorial.service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SendData extends AppCompatActivity {

   int code =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.constrintsexample);

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(SendData.this,ReceiveAndSendDataBack.class);
                i.putExtra("name","koti");
                i.putExtra("latitude","12.123456");
                i.putExtra("langitude","14.98765436");
                startActivityForResult(i,code);
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == code) {
            if (resultCode == RESULT_OK) {

                String  datareceived= data.getStringExtra("success");

                Toast.makeText(this,datareceived,Toast.LENGTH_SHORT).show();

            }
        }
    }
}
