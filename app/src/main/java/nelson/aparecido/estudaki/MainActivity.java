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

    View calendario, lupa, home, professor, perfil;

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

                /* BARRA DE TAREFA */
            calendario = findViewById(R.id.view_calendario);
            lupa = findViewById(R.id.view_lupa);
            home = findViewById(R.id.view_home);
            professor = findViewById(R.id.view_conversa_professor);
            perfil = findViewById(R.id.view_perfil);


           perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), TelaPerfil.class);
                startActivity(intent);


            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);


            }
        });

        /*  FIM BARRA DE TAREFA */








    }
}