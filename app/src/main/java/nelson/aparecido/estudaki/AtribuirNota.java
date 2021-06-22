package nelson.aparecido.estudaki;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jaeger.library.StatusBarUtil;

import org.jetbrains.annotations.NotNull;

public class AtribuirNota extends AppCompatActivity {

    private View calendario, nota, home, professor, perfil, btn_me_ajuda;
    private TextView nomeMateria, lgdDownload, lgdAtribui;
    private EditText editNota, editComentarios;
    private ImageView iconMateria, imgDownloadResposta, imgAtualizNota;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String usuarioID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_atribui_nota);
        StatusBarUtil.setTransparent(this);
        Respostas atividadeEntregue = getIntent().getExtras().getParcelable("atividadeEntregue");
        barraDeTarefas();
        cabecalho(atividadeEntregue);

        editNota = findViewById(R.id.edit_edita_nota);
        editComentarios = findViewById(R.id.edit_comentarios_nota_atividade);
        imgDownloadResposta = findViewById(R.id.img_download_atividade_enviada);
        imgAtualizNota = findViewById(R.id.img_atualiza_nota);
        lgdAtribui = findViewById(R.id.txt_btn_atribuir_nota);
        lgdDownload = findViewById(R.id.txt_download_atividade_entregue);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if (documentSnapshot.exists()) {
                        String ocupacao = documentSnapshot.getData().get("ocupacao").toString();

                        if (ocupacao.equalsIgnoreCase("estudante")) {
                            editNota.setEnabled(false);
                            editComentarios.setEnabled(false);
                            imgAtualizNota.setVisibility(View.INVISIBLE);
                            imgDownloadResposta.setVisibility(View.INVISIBLE);
                            lgdAtribui.setVisibility(View.INVISIBLE);
                            lgdDownload.setVisibility(View.INVISIBLE);
                            setMessage(atividadeEntregue);
                        }
                    }
                }
            }
        });

        DocumentReference documentRef = db.collection("Respostas").document(atividadeEntregue.getUid());
        documentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if (documentSnapshot.exists()) {
                        editNota.setText(documentSnapshot.getString("nota"));
                        editComentarios.setText(documentSnapshot.getString("feedbackProf"));
                    }
                }
            }
        });


        imgAtualizNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editNota.getText().toString().isEmpty()) {
                    editNota.setError("Digite uma nota");
                } else {
                    Double notaAux = Double.parseDouble(editNota.getText().toString());
                    if (notaAux >= 0 && notaAux <= 10) {
                        db.collection("Respostas").document(atividadeEntregue.getUid()).update("nota", editNota.getText().toString());
                        db.collection("Respostas").document(atividadeEntregue.getUid()).update("feedbackProf", editComentarios.getText().toString());
                        Toast.makeText(getApplicationContext(), "Nota atualizada com sucesso!", Toast.LENGTH_LONG).show();
                    } else {
                        editNota.setError("Digite uma nota entre 0 e 10");
                    }
                }

            }
        });

        imgDownloadResposta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoURL(atividadeEntregue.getUrl());
            }
        });
    }

    private void setMessage(Respostas atividadeEntregue) {
        DocumentReference documentReference = db.collection("Respostas").document(atividadeEntregue.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> taskUser) {

                if (taskUser.isSuccessful()) {
                    DocumentSnapshot documentSnapshotAux = taskUser.getResult();

                    if (documentSnapshotAux.exists()) {
                            if (documentSnapshotAux.getString("nota").equalsIgnoreCase("") &&
                                    documentSnapshotAux.getString("feedbackProf").equalsIgnoreCase("")) {
                                editComentarios.setText("Aguarde a avaliação");
                            }

                    }
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

        btn_me_ajuda = (View) findViewById(R.id.view_me_ajuda_notas);
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

    private void cabecalho(Respostas atividadeEntregue) {
        nomeMateria = findViewById(R.id.txt_nome_materia_atribui_nota);
        iconMateria = findViewById(R.id.img_icon_materia_atribui_nota);

        if (atividadeEntregue.getMateria().equalsIgnoreCase("Matemática")) {
            nomeMateria.setText(atividadeEntregue.getMateria());
            Drawable drawable = getResources().getDrawable(R.drawable.logo_matematica);
            iconMateria.setImageDrawable(drawable);

        } else if (atividadeEntregue.getMateria().equalsIgnoreCase("Português")) {
            nomeMateria.setText(atividadeEntregue.getMateria());
            Drawable drawable = getResources().getDrawable(R.drawable.logo_portugues);
            iconMateria.setImageDrawable(drawable);

        } else if (atividadeEntregue.getMateria().equalsIgnoreCase("Ciências")) {
            nomeMateria.setText(atividadeEntregue.getMateria());
            Drawable drawable = getResources().getDrawable(R.drawable.logo_ciencia);
            iconMateria.setImageDrawable(drawable);

        } else if (atividadeEntregue.getMateria().equalsIgnoreCase("Geografia")) {
            nomeMateria.setText(atividadeEntregue.getMateria());
            Drawable drawable = getResources().getDrawable(R.drawable.logo_geografia);
            iconMateria.setImageDrawable(drawable);

        } else {
            nomeMateria.setText(atividadeEntregue.getMateria());
            Drawable drawable = getResources().getDrawable(R.drawable.logo_historia);
            iconMateria.setImageDrawable(drawable);
        }
    }

    private void gotoURL(String s) {

        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}


