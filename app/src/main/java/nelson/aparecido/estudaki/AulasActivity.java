package nelson.aparecido.estudaki;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaeger.library.StatusBarUtil;

public class AulasActivity extends AppCompatActivity {

    ConstraintLayout matematica, portugues, ciencias, geografia, historia;
    private View calendario, lupa, home, professor, perfil, btn_me_ajuda, aulas_matematica;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String usuarioID;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_aulas);
        barraDeTarefas();
        StatusBarUtil.setTransparent(this);
        selecionaMateria();
    }

    private void selecionaMateria() {
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Ir para o menu da materia matematica
        matematica = findViewById(R.id.constaint_aulas_matematica);
        matematica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Usuario").document(usuarioID).update("materiaAtual", "Matemática");
                Intent intent = new Intent(getApplicationContext(), MateriaListaAulasDisponiveisActivity.class);
                startActivity(intent);
            }
        });

        //Ir para o menu da materia portugues
        portugues = findViewById(R.id.constaint_aulas_portugues);
        portugues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Usuario").document(usuarioID).update("materiaAtual", "Português");
                Intent intent = new Intent(getApplicationContext(), MateriaListaAulasDisponiveisActivity.class);
                startActivity(intent);
            }
        });

        //Ir para o menu da materia ciencias
        ciencias = findViewById(R.id.constaint_aulas_ciencias);
        ciencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Usuario").document(usuarioID).update("materiaAtual", "Ciências");
                Intent intent = new Intent(getApplicationContext(), MateriaListaAulasDisponiveisActivity.class);
                startActivity(intent);
            }
        });

        //Ir para o menu da materia geografia
        geografia = findViewById(R.id.constaint_aulas_geografia);
        geografia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Usuario").document(usuarioID).update("materiaAtual", "Geografia");
                Intent intent = new Intent(getApplicationContext(), MateriaListaAulasDisponiveisActivity.class);
                startActivity(intent);
            }
        });

        //Ir para o menu da materia historia
        historia = findViewById(R.id.constaint_aulas_historia);
        historia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Usuario").document(usuarioID).update("materiaAtual", "História");
                Intent intent = new Intent(getApplicationContext(), MateriaListaAulasDisponiveisActivity.class);
                startActivity(intent);
            }
        });
    }

    private void barraDeTarefas() {
        calendario = findViewById(R.id.view_calendario);
        lupa = findViewById(R.id.view_lupa);
        home = findViewById(R.id.view_home);
        professor = findViewById(R.id.view_conversa_professor);
        perfil = findViewById(R.id.view_perfil);

        btn_me_ajuda = (View) findViewById(R.id.view_me_ajuda_aulas);
        btn_me_ajuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MeAjudaActivity.class);
                startActivity(intent);
            }
        });

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PerfilActivity.class);
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

        professor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContatosActivity.class);
                startActivity(intent);
            }
        });

        calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalendarioActivity.class);
                startActivity(intent);
            }
        });

        lupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PesquisaActivity.class);
                startActivity(intent);
            }
        });
    }
}
