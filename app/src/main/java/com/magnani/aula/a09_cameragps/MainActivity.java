package com.magnani.aula.a09_cameragps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickAdicionar(View view){
        Intent intencao = new Intent(this, CadastroActivity.class);
        startActivity(intencao);
    }
}
