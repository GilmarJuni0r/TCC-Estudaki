package nelson.aparecido.estudaki;

        import android.app.ProgressDialog;
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

public class UploadAtividades extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 3;
    private View calendario, nota, home, professor, perfil, btn_me_ajuda;
    private ImageView iconMateria, btnSelecionaAtividade, btnUploadNovaAtividade;
    private EditText tituloAtividade, descricaoAtividade, dataMaxAtividade;
    private TextView nomeMateria, tipoArquivo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private String usuarioID;
    private Uri diretorio;
    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_atividades);
        cabecalho();
        barraDeTarefas();
        StatusBarUtil.setTransparent(this);

        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);

        tituloAtividade = findViewById(R.id.edit_titulo_arquivo_upload_atividades);
        descricaoAtividade = findViewById(R.id.edit_descricao_arquivo_upload_atividades);
        dataMaxAtividade = findViewById(R.id.edit_datamax_upload_atividades);
        btnSelecionaAtividade = findViewById(R.id.btn_seleciona_nova_atividade);
        btnUploadNovaAtividade = findViewById(R.id.btn_upload_nova_atividade);

        uploadAtividadeProva();
    }

    private void uploadAtividadeProva() {
        btnSelecionaAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        selecionaArquivo();
                    }
                });
            }
        });

        btnUploadNovaAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                        if (!(tituloAtividade.getText().toString().isEmpty()) || !(descricaoAtividade.getText().toString().isEmpty())
                                || !(dataMaxAtividade.getText().toString().isEmpty())) {
                            String titulo = tituloAtividade.getText().toString();
                            String descricao = descricaoAtividade.getText().toString();
                            String dataMaxima = dataMaxAtividade.getText().toString();
                            long timestamp = System.currentTimeMillis();

                            if (diretorio != null) {
                                String filename = UUID.randomUUID().toString();
                                String codigo = UUID.randomUUID().toString();
                                progressDialog.setTitle("Realizando upload...");
                                progressDialog.show();
                                StorageReference atividadeProvaRef = storageReference.child(value.getString("tipoArquivoAtual")+"/" + filename);

                                atividadeProvaRef.putFile(diretorio).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        atividadeProvaRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                MaterialAula atividadeProva = new MaterialAula(
                                                        codigo,
                                                        value.getString("tipoArquivoAtual"),
                                                        value.getString("materiaAtual"),
                                                        value.getString("turma"),
                                                        titulo,
                                                        descricao,
                                                        dataMaxima,
                                                        uri.toString(),timestamp);

                                                FirebaseFirestore.getInstance().collection("Arquivos").add(atividadeProva).
                                                        addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                if(value.getString("tipoArquivoAtual").equalsIgnoreCase("atividade"))
                                                                    Toast.makeText(getApplicationContext(), "Atividade cadastrada com sucesso!", Toast.LENGTH_LONG).show();
                                                                else
                                                                    Toast.makeText(getApplicationContext(), "Prova cadastrado com sucesso!", Toast.LENGTH_LONG).show();

                                                                tituloAtividade.setText("");
                                                                descricaoAtividade.setText("");
                                                                dataMaxAtividade.setText("");
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
                                        Toast.makeText(getApplicationContext(), "Falha ao realizar upload", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                                        double progresso = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                        progressDialog.setMessage(((int) progresso) + "% Carregado");
                                    }
                                });
                            }else{
                                if(value.getString("tipoArquivoAtual").equalsIgnoreCase("atividade"))
                                    Toast.makeText(getApplicationContext(), "Selecione uma atividade para realizar o upload", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getApplicationContext(), "Selecione uma prova para realizar o upload", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            if(value.getString("tipoArquivoAtual").equalsIgnoreCase("atividade"))
                                Toast.makeText(getApplicationContext(), "Preencha os campos de título, descrição e data máxima para cadastrar uma nova atividade", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(getApplicationContext(), "Preencha os campos de título, descrição e data máxima para cadastrar uma nova prova", Toast.LENGTH_LONG).show();
                        }
                    }
                });
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

    private void cabecalho() {
        nomeMateria = findViewById(R.id.txt_nome_materia_upload_atividades);
        iconMateria = findViewById(R.id.img_icon_materia_upload_atividades);
        tipoArquivo = findViewById(R.id.txt_funcao_upload_atividades);

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
                if (value.getString("tipoArquivoAtual").equalsIgnoreCase("atividade")) {
                    tipoArquivo.setText("Upload de atividade");
                } else {
                    tipoArquivo.setText("Upload de prova");
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

        btn_me_ajuda = (View) findViewById(R.id.view_me_ajuda_upload_atividade);
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


