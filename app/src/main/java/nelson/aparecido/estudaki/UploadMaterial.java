package nelson.aparecido.estudaki;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class UploadMaterial extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST = 1;
    private static final int PICK_FILE_REQUEST = 2;
    private View calendario, lupa, home, professor, perfil, btn_me_ajuda;
    private TextView nomeMateria;
    private ImageView iconMateria, btnSelecionaMaterial, btnUploadMaterial;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private String usuarioID;
    private boolean selecionouArquivo = false;
    private EditText editTitulo, editDescricao;

    private Uri diretorio;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_material);
        StatusBarUtil.setTransparent(this);
        cabecalho();
        barraDeTarefas();

        storageReference = FirebaseStorage.getInstance().getReference();

        editTitulo = findViewById(R.id.edit_titulo_arquivo_upload_material);
        editDescricao = findViewById(R.id.edit_descricao_arquivo_upload_material);
        btnSelecionaMaterial = findViewById(R.id.btn_seleciona_novo_material);
        btnUploadMaterial = findViewById(R.id.btn_upload_novo_material);

        btnSelecionaMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        if (value.getString("tipoArquivoAtual").equalsIgnoreCase("aulas")) {
                            selecionaAula();
                        }
                        if (value.getString("tipoArquivoAtual").equalsIgnoreCase("Material")) {
                            selecionaMaterial();
                        }
                    }
                });
            }
        });

        btnUploadMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                        if (!(editTitulo.getText().toString().isEmpty()) || !(editDescricao.getText().toString().isEmpty())) {
                            if(value.getString("tipoArquivoAtual").equalsIgnoreCase("aulas")){
                                uploadAula();
                            }
                            if(value.getString("tipoArquivoAtual").equalsIgnoreCase("material")){
                                uploadMaterial();
                            }
                                //Toast.makeText(getApplicationContext(), "Material cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                                //selecionouArquivo = false;

                        } else {
                            Toast.makeText(getApplicationContext(), "Preencha os campos de título e descrição para cadastrar um novo material", Toast.LENGTH_LONG).show();
                        }//db.collection("Arquivos").document(value.).update("tipoArquivoAtual", "Atividade");
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            diretorio = data.getData();
        }
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            diretorio = data.getData();
        }

    }

    private void cabecalho() {
        nomeMateria = findViewById(R.id.txt_nome_materia_upload_material);
        iconMateria = findViewById(R.id.img_icon_materia_upload_material);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
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
        lupa = findViewById(R.id.view_lupa);
        home = findViewById(R.id.view_home);
        professor = findViewById(R.id.view_conversa_professor);
        perfil = findViewById(R.id.view_perfil);

        btn_me_ajuda = (View) findViewById(R.id.view_me_ajuda_upload_materiais);
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

    private void selecionaAula() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecione uma aula"), PICK_VIDEO_REQUEST);
        selecionouArquivo = true;
    }

    private void selecionaMaterial() {
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecione o material"), PICK_FILE_REQUEST);
        selecionouArquivo = true;
    }

    private String uploadAula() {
        final String[] saida = {""};
        if (diretorio != null) {
            String filename = UUID.randomUUID().toString();
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Realizando upload...");
            progressDialog.show();
            StorageReference aulaRef = storageReference.child("aulas/" + filename);

            aulaRef.putFile(diretorio).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    saida[0] = aulaRef.getDownloadUrl().toString();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Aula cadastrada com sucesso!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Falha ao realizar upload de aula", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                    double progresso = (100.0 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                    progressDialog.setMessage(((int) progresso) + "% Carregado");

                }
            });
        }
        return saida[0];
    }

    private String uploadMaterial() {
        final String[] saida = {""};
        if (diretorio != null) {
            String filename = UUID.randomUUID().toString();
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Realizando upload...");
            progressDialog.show();
            StorageReference materialRef = storageReference.child("materiais/" + filename);

            materialRef.putFile(diretorio).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    saida[0] = materialRef.getDownloadUrl().toString();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Material cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Falha ao realizar upload de material", Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                    double progresso = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    progressDialog.setMessage(((int) progresso) + "% Carregado");

                }
            });
        }

        return saida[0];
    }
}


