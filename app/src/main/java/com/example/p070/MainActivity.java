package com.example.p070;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        // добавляемпунктыменю
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
// TODO Auto-generated method stub
        Intent intent = null;
        switch (item.getItemId())
        {
            case R.id.m1: intent = new Intent(this, theatre.class);
                break;
            case R.id.m2: intent = new Intent(this, theatres_films.class);
                break;
            case R.id.m3: intent = new Intent(this, newtheatre.class);
                break;
            case R.id.m4: intent = new Intent(this, newfilm.class);
                break;
            case R.id.m5: intent = new Intent(this, update_theatres.class);
                break;
            case R.id.m6: intent = new Intent(this, workWithFilm.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}