package nelson.aparecido.estudaki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;

public class MateriaMenuActivity extends AppCompatActivity {

    View aulas, matematica_material_aula, matematica_atividades,matematica_provas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_materia_menu);

        StatusBarUtil.setTransparent(this);

        aulas = findViewById(R.id.view_aulas);

        matematica_material_aula = findViewById(R.id.view_material_de_aula);

        matematica_atividades = findViewById(R.id.view_matematica_atividades);

        matematica_provas = findViewById(R.id.view_matematica_provas);

        aulas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MateriaListaAulasDisponiveisActivity.class);
                startActivity(intent);


            }
        });


        matematica_material_aula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MateriaMaterialAulaActivity.class);
                startActivity(intent);


            }
        });

        matematica_atividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MateriaAtividadeAulaActivity.class);
                startActivity(intent);


            }
        });

        matematica_provas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MateriaProvaActivity.class);
                startActivity(intent);


            }
        });




    }

}



