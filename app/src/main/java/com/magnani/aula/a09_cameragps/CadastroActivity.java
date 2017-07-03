package com.magnani.aula.a09_cameragps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CadastroActivity extends AppCompatActivity {

    private ImageView ivFotografia;
    private TextView tvLocalizacao;

    private File fotoAtual;
    private LocationManager gerenciadorLocalizacao;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_LOCATION_ACCESS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        ivFotografia = (ImageView) findViewById(R.id.ivFotografia);
        tvLocalizacao = (TextView) findViewById(R.id.tvLocalizacao);

        gerenciadorLocalizacao = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        iniciaRequisicaoPorLocalizacao();
    }

    private void iniciaRequisicaoPorLocalizacao(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(CadastroActivity.this, "Usuário não tem permissão para obter coordendas", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_ACCESS);
            return;
        }else{
            Toast.makeText(CadastroActivity.this, "Usuário concedeu permissão para obter coordendas", Toast.LENGTH_SHORT).show();

        }

        LocationListener ouvidorLocalizacao = new LocationListener() {
            public void onLocationChanged(Location location) {
                tvLocalizacao.setText(location.toString());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        gerenciadorLocalizacao.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ouvidorLocalizacao);
    }



    public void onClickFoto(View v) {
        Intent intencaoTirarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        gerarNovoArquivoFoto();
//        Uri fotoUri = FileProvider.getUriForFile(
//                this,
//                "com.aula.magnani.aula.a09_cameragps",
//                fotoAtual);
//        intencaoTirarFoto.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
        startActivityForResult(intencaoTirarFoto, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivFotografia.setImageBitmap(imageBitmap);

//            Uri fotoUri = FileProvider.getUriForFile(
//                    this,
//                    "com.aula.magnani.aula.a09_cameragps",
//                    fotoAtual);
//            ivFotografia.setImageURI(fotoUri);
        }
    }

    private void gerarNovoArquivoFoto() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "MM_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            fotoAtual = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_ACCESS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    iniciaRequisicaoPorLocalizacao();
                } else {
                    Toast.makeText(CadastroActivity.this, "Usuário não concedeu permissão para obter coordendas", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }


}
