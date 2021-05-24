package nelson.aparecido.estudaki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;

public class MainActivity extends AppCompatActivity {

    ImageView materias;
    ImageView aulas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtil.setTransparent(this);


        materias = findViewById(R.id.img_materias_telaprincipal);


        aulas = findViewById(R.id.img_aulas_telaprincipal);



        materias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), TelaMaterias.class);
                startActivity(intent);


            }
        });

        aulas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), TelaAulas.class);
                startActivity(intent);


            }
        });








    }
}