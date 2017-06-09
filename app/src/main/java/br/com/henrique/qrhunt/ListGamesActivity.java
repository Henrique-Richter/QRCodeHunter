package br.com.henrique.qrhunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.List;

import br.com.henrique.qrhunt.DAO.GameDAO;
import br.com.henrique.qrhunt.Models.GameModel;

public class ListGamesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_games);
        final Spinner spinner = (Spinner)findViewById(R.id.spinner);

        try {
            GameDAO gameDao = new GameDAO(this);
            gameDao.open();

            List<GameModel> lista = gameDao.getAll();
            gameDao.close();

            String[] spinnerArray = new String[lista.size()];
            final HashMap<Integer,String> spinnerMap = new HashMap<Integer, String>();
            for (int i = 0; i < lista.size(); i++)
            {
                spinnerMap.put(i,lista.get(i).getCode());
                spinnerArray[i] = lista.get(i).getName();
            }

            ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            Button goToGamePage = (Button)findViewById(R.id.goToGame);

            goToGamePage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = spinnerMap.get(spinner.getSelectedItemPosition());
                    Intent i = new Intent(ListGamesActivity.this, ScanCodeActivity.class);

                    Bundle bundle = new Bundle();

                    //Add your data to bundle
                    bundle.putString("code",id);

                    //Add the bundle to the intent
                    i.putExtras(bundle);

                    //Fire that second activity
                    startActivity(i);
                }
            });

        }catch(Exception e){
            Log.e("Error","Exception : " + e);
        }



    }
}
