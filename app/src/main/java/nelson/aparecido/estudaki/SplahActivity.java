package nelson.aparecido.estudaki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.jaeger.library.StatusBarUtil;

public class SplahActivity extends AppCompatActivity {


    int tempoDeEspera = 500 * 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // desativando a tela noturna
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splah);
        StatusBarUtil.setTransparent(this); /*DEIXAR A TELA COM A PARTE DE CIMA TRANSPARENTE PRECISA ESTA ENM Telas*/

        trocarTela();


    }

    // fazer a troca de telas no inicio com um delay de 10s
    private void trocarTela(){


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent trocarDeTela = new Intent(SplahActivity.this, LoginActivity.class); /* teste muda aqui*/
                startActivity(trocarDeTela);
                finish();


            }
        },tempoDeEspera);



    }


}