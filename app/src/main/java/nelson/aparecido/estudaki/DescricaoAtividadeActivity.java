package nelson.aparecido.estudaki;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaeger.library.StatusBarUtil;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DescricaoAtividadeActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 4;
    private View calendario, lupa, home, professor, perfil, btn_me_ajuda;
    private TextView nomeMateria, tipoArquivo, tituloAtividade,descricaoConteudo, dataEntrega, txtUpload, txtSeleciona;
    private ImageView iconMateria, btnUpload, btnDownload, btnSeleciona;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String usuarioID;
    private StorageReference storageReference;
    private Uri diretorio;
    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_descricao_atividade);
        StatusBarUtil.setTransparent(this);
        barraDeTarefas();
        cabecalho();

        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        AtividadeProva atividadeProva = getIntent().getExtras().getParcelable("atividadeProva"); // <- Objeto contendo o conteúdo selecionado
        tituloAtividade.setText(atividadeProva.getTitulo());
        descricaoConteudo.setText(atividadeProva.getDescricao());

        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(value.getString("ocupacao").equalsIgnoreCase("professor")
                        || value.getString("ocupacao").equalsIgnoreCase("professora") ){
                    btnUpload.setVisibility(View.INVISIBLE);
                    txtUpload.setVisibility(View.INVISIBLE);
                    btnSeleciona.setVisibility(View.INVISIBLE);
                    txtSeleciona.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnDownload = findViewById(R.id.img_download_descricao_atividade);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoURL(atividadeProva.getUrl());
            }
        });

        uploadRespostas(atividadeProva);
    }

    private void uploadRespostas(AtividadeProva atividadeProva) {
        btnSeleciona = findViewById(R.id.img_seleciona_resposta);
        btnSeleciona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionaArquivo();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(diretorio != null){
                    DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
                    documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                            String filename = UUID.randomUUID().toString();
                            progressDialog.setTitle("Realizando upload...");
                            progressDialog.show();
                            long timestamp = System.currentTimeMillis();

                            StorageReference ref = storageReference.child("Respostas/" + filename);
                            ref.putFile(diretorio).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Respostas respostas = new Respostas(
                                                    value.getString("tipoArquivoAtual"),
                                                    value.getString("materiaAtual"),
                                                    value.getString("turma"),
                                                    usuarioID,
                                                    uri.toString(),
                                                    timestamp,
                                                    "-",
                                                    atividadeProva.getCodigo());
                                            FirebaseFirestore.getInstance().collection("Respostas").add(respostas).
                                                    addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Toast.makeText(getApplicationContext(), "Respostas cadastradas com sucesso!", Toast.LENGTH_LONG).show();
                                                            diretorio=null;
                                                        }
                                                    });
                                        }
                                    });
                                    progressDialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Falha ao cadastrar respostas!", Toast.LENGTH_LONG).show();
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                                    double progresso = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                    progressDialog.setMessage(((int) progresso) + "% Carregado");
                                }
                            });
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(), "Selecione um arquivo para enviar as respostas", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            diretorio = data.getData();
        }
    }

    private void selecionaArquivo() {
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecione um arquivo"), PICK_FILE_REQUEST);
    }

    private void gotoURL(String s) {

        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    private void cabecalho() {
        txtSeleciona = findViewById(R.id.txt_seleciona_resposta);
        txtUpload = findViewById(R.id.txt_upload_respostas);
        btnUpload = findViewById(R.id.img_upload_respostas);
        tipoArquivo = findViewById(R.id.txt_tipo_arquivo_descricao);
        tituloAtividade = findViewById(R.id.txt_nome_conteudo);
        descricaoConteudo = findViewById(R.id.txt_descricao_conteudo);
        dataEntrega = findViewById(R.id.txt_data_entrega_conteudo);

        nomeMateria = findViewById(R.id.text_nome_descricao_atividade);
        iconMateria = findViewById(R.id.img_descricao_atividade);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(value.getString("materiaAtual").equalsIgnoreCase("Matemática")){
                    nomeMateria.setText(value.getString("materiaAtual"));
                    Drawable drawable= getResources().getDrawable(R.drawable.logo_matematica);
                    iconMateria.setImageDrawable(drawable);

                }else if(value.getString("materiaAtual").equalsIgnoreCase("Português")){
                    nomeMateria.setText(value.getString("materiaAtual"));
                    Drawable drawable= getResources().getDrawable(R.drawable.logo_portugues);
                    iconMateria.setImageDrawable(drawable);

                }else if(value.getString("materiaAtual").equalsIgnoreCase("Ciências")){
                    nomeMateria.setText(value.getString("materiaAtual"));
                    Drawable drawable= getResources().getDrawable(R.drawable.logo_ciencia);
                    iconMateria.setImageDrawable(drawable);

                }else if(value.getString("materiaAtual").equalsIgnoreCase("Geografia")){
                    nomeMateria.setText(value.getString("materiaAtual"));
                    Drawable drawable= getResources().getDrawable(R.drawable.logo_geografia);
                    iconMateria.setImageDrawable(drawable);

                }else{
                    nomeMateria.setText(value.getString("materiaAtual"));
                    Drawable drawable= getResources().getDrawable(R.drawable.logo_historia);
                    iconMateria.setImageDrawable(drawable);
                }
            }
        });
    }

    private void barraDeTarefas() {

        calendario = findViewById(R.id.view_calendario);
        lupa = findViewById(R.id.view_lupa);
        home = findViewById(R.id.view_home);
        professor = findViewById(R.id.view_conversa_professor);
        perfil = findViewById(R.id.view_perfil);

        btn_me_ajuda = (View) findViewById(R.id.view_me_ajuda_descricao_atividades);
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
