package nelson.aparecido.estudaki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jaeger.library.StatusBarUtil;

public class MateriasActivity extends AppCompatActivity {

    ConstraintLayout matematica;
    ConstraintLayout portugues;
    ConstraintLayout ciencias;
    ConstraintLayout geografia;
    ConstraintLayout historia;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_materias);

        StatusBarUtil.setTransparent(this);

        //Ir para o menu da materia matematica
        matematica = findViewById(R.id.constaint_matematica);
        matematica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MaterialMenuActivity.class);
                startActivity(intent);
            }
        });

        //Ir para o menu da materia portugues
        portugues = findViewById(R.id.constaint_portugues);
        portugues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MaterialMenuActivity.class);
                startActivity(intent);
            }
        });

        //Ir para o menu da materia ciencias
        ciencias = findViewById(R.id.constaint_ciencias);
        ciencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MaterialMenuActivity.class);
                startActivity(intent);
            }
        });

        //Ir para o menu da materia geografia
        geografia = findViewById(R.id.constaint_geografia);
        geografia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MaterialMenuActivity.class);
                startActivity(intent);
            }
        });

        //Ir para o menu da materia historia
        historia = findViewById(R.id.constaint_historia);
        historia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MaterialMenuActivity.class);
                startActivity(intent);
            }
        });

    }

}
