package nelson.aparecido.estudaki;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;

public class tela_matematica_aulas extends AppCompatActivity {

    View aula21_05;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_matematica_aulas);

        StatusBarUtil.setTransparent(this);

        aula21_05 = findViewById(R.id.view_aula21_04);

        aula21_05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                gotoURL("https://drive.google.com/uc?export=download&id=12LKzhLjrd6YJvmKfyTXdc9ZY4fYu-DTQ");

            }
        });


    }

    private void gotoURL(String s) {

        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }


}
