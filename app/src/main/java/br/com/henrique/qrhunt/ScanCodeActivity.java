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

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.sql.SQLException;

import br.com.henrique.qrhunt.DAO.GameDAO;
import br.com.henrique.qrhunt.Models.GameModel;

public class ScanCodeActivity extends AppCompatActivity {

    GameModel game;
    long missing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);

        Button scanCode = (Button)findViewById(R.id.scanCode);

        Bundle bundle = getIntent().getExtras();

        String code = bundle.getString("code");

        TextView gameName = (TextView) findViewById(R.id.gameName);
        TextView missingText = (TextView) findViewById(R.id.missing);


        GameDAO gameDAO = new GameDAO(this);

        try {
            gameDAO.open();

            game = gameDAO.getGameByCode(code);

             missing = game.getQuantity() - StringUtils.countMatches(game.getFound(),",");

            gameName.setText("Game: " + game.getName());
            missingText.setText("Missing: "+ missing);

        }catch(SQLException s){
            Log.e("SQL exception", "Error: "+ s);
        }


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
                if(obj.has("code")){
                    if(!obj.get("code").toString().equals(game.getCode())){
                       Log.d("Code","Objeto : "+ obj.get("code").toString() + " , game: " + game.getCode());
                        scanResponse.setText("Código invalido");
                        return;
                    }

                   String resposta = addStep(game.getFound(),obj.get("step").toString());
                    scanResponse.setText(resposta);

                }else{
                    scanResponse.setText("Invalido");
                }
            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: ");
            }

        }
        // else continue with any other code you need in the method

    }

    private String addStep(String found, String new_code){

        if(Long.parseLong(new_code) > game.getQuantity() || Long.parseLong(new_code) <=0){
            return "Invalid Tag";
        }

        for (String codes_found : found.split(",")) {
            if(codes_found.equals(new_code)){
                return "Tag already added";
            }
        }

        found = found + new_code +",";
        GameDAO gameDAO = new GameDAO(this);

        try {
            gameDAO.open();

            gameDAO.updateGame(found, game.getCode());
        }catch (Exception e){
            Log.e("Error","Error" + e);
        }
        return "Tag added";
    }
}
