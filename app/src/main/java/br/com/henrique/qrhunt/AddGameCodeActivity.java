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

import java.util.List;

import br.com.henrique.qrhunt.DAO.GameDAO;
import br.com.henrique.qrhunt.Models.GameModel;

public class AddGameCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game_code);

        Button scanCode = (Button)findViewById(R.id.addGame);

        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(AddGameCodeActivity.this);
                integrator.initiateScan();
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            TextView scanResponse = (TextView) findViewById(R.id.messageAddGame);

            try{
                JSONObject obj = new JSONObject(scanResult.getContents());
                if(obj.has("name") && obj.has("quantity") && obj.has("code")){
                    GameDAO gameDao = new GameDAO(this);
                    gameDao.open();

                    gameDao.create(obj.get("name").toString(),
                            Long.parseLong(obj.get("quantity").toString()),
                            obj.get("code").toString());

                    List<GameModel> lista= gameDao.getAll();

                    gameDao.close();
                    scanResponse.setText("Game " + obj.get("name") + " added");
                }else{
                    scanResponse.setText("Invalid Game Tag");
                }
            } catch (Throwable t) {
                scanResponse.setText("This game already exists");
                Log.e("My App", "Error at adding game");
            }

        }
        // else continue with any other code you need in the method

    }
}
