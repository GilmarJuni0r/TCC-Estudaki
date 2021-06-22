package nelson.aparecido.estudaki;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

public class DescricaoMaterialActivity extends AppCompatActivity {

    private View calendario, nota, home, professor, perfil, btn_me_ajuda;
    private TextView nomeMateria, tipoArquivo, tituloMaterial, descricaoMaterial;
    private ImageView iconMateria, btnDownload;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String usuarioID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_descricao_material_aula);
        StatusBarUtil.setTransparent(this);
        barraDeTarefas();
        cabecalho();

        MaterialAula materialAula = getIntent().getExtras().getParcelable("materialAula"); // <- Objeto contendo o conteúdo selecionado
        tituloMaterial.setText(materialAula.getTitulo());
        descricaoMaterial.setText(materialAula.getDescricao());

        btnDownload = findViewById(R.id.img_download_descricao_material);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoURL(materialAula.getUrl());
            }
        });
    }

    private void cabecalho() {
        tipoArquivo = findViewById(R.id.txt_tipo_arquivo_descricao);
        tituloMaterial = findViewById(R.id.txt_nome_material);
        descricaoMaterial = findViewById(R.id.txt_descricao_material);

        nomeMateria = findViewById(R.id.text_nome_descricao_material);
        iconMateria = findViewById(R.id.img_descricao_material);

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

    private void barraDeTarefas() {
        calendario = findViewById(R.id.view_calendario);
        nota = findViewById(R.id.view_notas);
        home = findViewById(R.id.view_home);
        professor = findViewById(R.id.view_conversa_professor);
        perfil = findViewById(R.id.view_perfil);

        btn_me_ajuda = (View) findViewById(R.id.view_me_ajuda_descricao_material);
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

    private void gotoURL(String s) {

        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
