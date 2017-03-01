package kamranali44.com.placesofinterest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //https://maps.googleapis.com/maps/api/place/search/json?location=52.25803451,-0.94128719&radius=100&sensor=true&key=AIzaSyD2kIo_ZDadEGQtIkADlKgflYKSS-C1_Vg

    Button cmdGetResults = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitialiseViews();

    }

    private void InitialiseViews() {
        cmdGetResults = (Button) findViewById(R.id.cmdGetResults);

        cmdGetResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        });
    }
}
