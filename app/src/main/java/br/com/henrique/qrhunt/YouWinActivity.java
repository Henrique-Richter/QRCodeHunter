package br.com.henrique.qrhunt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class YouWinActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_win);

        TextView winMessage = (TextView)findViewById(R.id.youWinMessage);

        Bundle bundle = getIntent().getExtras();

        String name = bundle.getString("name");

        winMessage.setText("Congratulations you won the game " + name);
    }
}
