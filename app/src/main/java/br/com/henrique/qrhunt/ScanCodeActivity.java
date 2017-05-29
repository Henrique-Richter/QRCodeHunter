package br.com.henrique.qrhunt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

public class ScanCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);

        Button scanCode = (Button)findViewById(R.id.scanCode);

        Bundle bundle = getIntent().getExtras();

        String code = bundle.getString("code");

        TextView testBatata = (TextView) findViewById(R.id.testBatata);

        testBatata.setText(code);

        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(ScanCodeActivity.this);
                integrator.initiateScan();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            TextView scanResponse = (TextView) findViewById(R.id.scanResult);

            try{
                JSONObject obj = new JSONObject(scanResult.getContents());
                if(obj.has("id")){
                    scanResponse.setText("Sucesso");
                }else{
                    scanResponse.setText("Invalido");
                }
            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: ");
            }

        }
        // else continue with any other code you need in the method

    }
}
