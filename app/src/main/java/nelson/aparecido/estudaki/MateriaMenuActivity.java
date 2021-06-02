package nelson.aparecido.estudaki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;

public class MateriaMenuActivity extends AppCompatActivity {

    View aulas, material_aula, atividades, provas, btn_me_ajuda;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_materia_menu);

        StatusBarUtil.setTransparent(this);

        //Ir para tela Me Ajuda
        btn_me_ajuda = (View) findViewById(R.id.imageView_me_ajuda_materia_menu);
        btn_me_ajuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MeAjudaActivity.class);
                startActivity(intent);
            }
        });

        aulas = findViewById(R.id.view_aulas);
        material_aula = findViewById(R.id.view_material_de_aula);
        atividades = findViewById(R.id.view_matematica_atividades);
        provas = findViewById(R.id.view_matematica_provas);

        aulas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MateriaListaAulasDisponiveisActivity.class);
                startActivity(intent);
            }
        });


        material_aula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MateriaMaterialAulaActivity.class);
                startActivity(intent);
            }
        });

        atividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MateriaAtividadeAulaActivity.class);
                startActivity(intent);
            }
        });

        provas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MateriaProvaActivity.class);
                startActivity(intent);
            }
        });
    }
}



