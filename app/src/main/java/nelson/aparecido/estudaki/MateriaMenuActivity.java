package nelson.aparecido.estudaki;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jaeger.library.StatusBarUtil;

import org.jetbrains.annotations.NotNull;

public class MateriaMenuActivity extends AppCompatActivity {

    private View aulas, material_aula, atividades, provas, btn_me_ajuda;
    private View calendario, nota, home, professor, perfil;
    private TextView nomeMateria;
    private ImageView iconMateria;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String usuarioID, materiaAtual;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_menu_da_materia);
        StatusBarUtil.setTransparent(this);
        barraDeTarefas();
        cabecalho();
        opcEscolhida();
    }

    private void cabecalho() {
        nomeMateria = findViewById(R.id.txt_nome_materia_menu_principal_materia);
        iconMateria = findViewById(R.id.img_icon_materia_menu_principal_materia);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                DocumentSnapshot value = task.getResult();

                if (value.getString("materiaAtual").equalsIgnoreCase("Matemática")) {
                    nomeMateria.setText(value.getString("materiaAtual"));
                    Drawable drawable = getResources().getDrawable(R.drawable.logo_matematica);
                    iconMateria.setImageDrawable(drawable);

                } else if (value.getString("materiaAtual").equalsIgnoreCase("Português")) {
                    nomeMateria.setText(value.getString("materiaAtual"));
                    Drawable drawable = getResources().getDrawable(R.drawable.logo_portugues);
                    iconMateria.setImageDrawable(drawable);

                } else if (value.getString("materiaAtual").equalsIgnoreCase("Ciências")) {
                    nomeMateria.setText(value.getString("materiaAtual"));
                    Drawable drawable = getResources().getDrawable(R.drawable.logo_ciencia);
                    iconMateria.setImageDrawable(drawable);

                } else if (value.getString("materiaAtual").equalsIgnoreCase("Geografia")) {
                    nomeMateria.setText(value.getString("materiaAtual"));
                    Drawable drawable = getResources().getDrawable(R.drawable.logo_geografia);
                    iconMateria.setImageDrawable(drawable);

                } else {
                    nomeMateria.setText(value.getString("materiaAtual"));
                    Drawable drawable = getResources().getDrawable(R.drawable.logo_historia);
                    iconMateria.setImageDrawable(drawable);
                }
            }
        });
    }

    private void opcEscolhida() {
        aulas = findViewById(R.id.view_aulas);
        material_aula = findViewById(R.id.view_material_de_aula);
        atividades = findViewById(R.id.view_matematica_atividades);
        provas = findViewById(R.id.view_matematica_provas);

        aulas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Usuario").document(usuarioID).update("tipoArquivoAtual", "aula");
                Intent intent = new Intent(getApplicationContext(), MateriaListaAulasDisponiveisActivity.class);
                startActivity(intent);
            }
        });

        material_aula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Usuario").document(usuarioID).update("tipoArquivoAtual", "material");
                Intent intent = new Intent(getApplicationContext(), MateriaMaterialAulaActivity.class);
                startActivity(intent);
            }
        });

        atividades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Usuario").document(usuarioID).update("tipoArquivoAtual", "atividade");
                Intent intent = new Intent(getApplicationContext(), MateriaAtividadeAulaActivity.class);
                startActivity(intent);
            }
        });

        provas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Usuario").document(usuarioID).update("tipoArquivoAtual", "prova");
                Intent intent = new Intent(getApplicationContext(), MateriaProvaActivity.class);
                startActivity(intent);
            }
        });
    }

    private void barraDeTarefas() {
        calendario = findViewById(R.id.view_calendario);
        nota = findViewById(R.id.view_notas);
        home = findViewById(R.id.view_home);
        professor = findViewById(R.id.view_conversa_professor);
        perfil = findViewById(R.id.view_perfil);

        //Ir para tela Me Ajuda
        btn_me_ajuda = (View) findViewById(R.id.imageView_me_ajuda_materia_menu);
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

        nota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NotasActivity.class);
                startActivity(intent);
            }
        });
    }
}



