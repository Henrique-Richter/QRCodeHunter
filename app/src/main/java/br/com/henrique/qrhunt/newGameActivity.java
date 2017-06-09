package br.com.henrique.qrhunt;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class newGameActivity extends AppCompatActivity {

    EditText dateLimit;
    EditText startDate;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        dateLimit = (EditText) findViewById(R.id.dateLimit);
        startDate = (EditText) findViewById(R.id.startDate);

        // perform click event on edit text
        dateLimit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(newGameActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateLimit.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(newGameActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                startDate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        Button goToSharePare = (Button)findViewById(R.id.create);
        goToSharePare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(newGameActivity.this, ShareGameActivity.class);

                Bundle bundle = new Bundle();

                EditText nameText = (EditText) findViewById(R.id.name);
                EditText quantityText = (EditText) findViewById(R.id.numberOfTags);

                if(nameText.getText().toString().trim().equals("") && quantityText.getText().toString().trim().equals("")){
                    quantityText.setError("Number of tags is required! ");
                    nameText.setError(" Name for the game is required! ");
                }else if(nameText.getText().toString().trim().equals("")){
                    nameText.setError(" Name for the game is required! ");
                }else if(quantityText.getText().toString().trim().equals("")){
                    quantityText.setError("Number of tags is required! ");
                }else {

                    String name = nameText.getText().toString();
                    int quantity = Integer.parseInt(quantityText.getText().toString());
                    //Add your data to bundle
                    bundle.putString("name", name);
                    bundle.putInt("quantity", quantity);

                    //Add the bundle to the intent
                    i.putExtras(bundle);

                    //Fire that second activity
                    startActivity(i);
                }
            }
        });
    }
}
