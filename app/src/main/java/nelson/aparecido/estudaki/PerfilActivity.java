package nelson.aparecido.estudaki;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaeger.library.StatusBarUtil;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.Item;
import com.xwray.groupie.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class PerfilActivity extends AppCompatActivity {

    private View calendario, lupa, home, professor, perfil, btnLogout;
    private TextView txtNome, txtOcupacao, txtTurma, txtIdade, txtRaCpf, txtNomeProfessor, txtEscola, txtEmail;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference foto;
    private String usuarioID;
    private Button btnFotoSelecionada;
    private ImageView imgFoto;
    private Uri selectedUri;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_perfil);
        StatusBarUtil.setTransparent(this);
        inicializarComponentes();
        barraDeTarefas();

        btnFotoSelecionada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarFoto();
            }
        });
    }

    private void barraDeTarefas() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
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

    @Override
    protected void onStart() {
        super.onStart();
        exibirDados();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected static int calculaIdade(String dataNascimento){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate dataConvertida = LocalDate.parse(dataNascimento, formatter);
        final LocalDate dataAtual = LocalDate.now();
        final Period periodo = Period.between(dataConvertida, dataAtual);
        return periodo.getYears();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0){
            selectedUri = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedUri);
                imgFoto.setImageDrawable(new BitmapDrawable(bitmap));
                btnFotoSelecionada.setAlpha(0);
                salvarFoto();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void selecionarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    private void salvarFoto(){
        String filename = usuarioID;//String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename );
        ref.putFile(selectedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(getApplicationContext(), "Foto de perfil alterada com sucesso", Toast.LENGTH_LONG).show();
                    }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Falha ao enviar imagem, tente novamente", Toast.LENGTH_LONG).show(); }
                });
    }

    protected void inicializarComponentes(){

        //Compenentes da tela
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        btnLogout = findViewById(R.id.btn_logout);
        btnFotoSelecionada = findViewById(R.id.btn_foto_selecionada);
        imgFoto = findViewById(R.id.img_fotoPerfil);

        txtNome = findViewById(R.id.txt_nome);
        txtOcupacao = findViewById(R.id.txt_ocupacao);
        txtTurma = findViewById(R.id.txt_turma);
        txtIdade = findViewById(R.id.txt_idade);
        txtRaCpf = findViewById(R.id.txt_raCpf);
        txtNomeProfessor = findViewById(R.id.txt_NomeProfessor);
        txtEscola = findViewById(R.id.txt_NomeEscola);
        txtEmail = findViewById(R.id.txt_emailPerfil);

        //Barra de tarefa
        calendario = findViewById(R.id.view_calendario);
        lupa = findViewById(R.id.view_lupa);
        home = findViewById(R.id.view_home);
        professor = findViewById(R.id.view_conversa_professor);
        perfil = findViewById(R.id.view_perfil);
    }

    private void exibirDados(){
        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot documentSnapshotPerfil, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(documentSnapshotPerfil != null){
                    foto = FirebaseStorage.getInstance().getReferenceFromUrl(documentSnapshotPerfil.getString("fotoPerfil"));

                    try {
                        final File tempfile = File.createTempFile("fotoPerfil", "jpg");
                        foto.getFile(tempfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(tempfile.getAbsolutePath());
                                imgFoto.setImageBitmap(bitmap);
                                btnFotoSelecionada.setAlpha(0);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String dataNascimento = documentSnapshotPerfil.getString("dataNascimento");

                    String turmaID = documentSnapshotPerfil.getString("turma");
                    DocumentReference turmaRef = db.collection("Turma").document(turmaID);

                    String escolaID = documentSnapshotPerfil.getString("escola");
                    DocumentReference escolaRef = db.collection("Escola").document(escolaID);

                    txtNome.setText(documentSnapshotPerfil.getString("nome"));
                    txtEmail.setText("Email: "+FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    txtIdade.setText("Idade: "+ calculaIdade(dataNascimento) + " anos");

                    if(documentSnapshotPerfil.getString("ocupacao").equalsIgnoreCase("Professor") || documentSnapshotPerfil.getString("ocupacao").equalsIgnoreCase("Professora")){
                        txtOcupacao.setText("Docente");
                        //txtTurma.setText("");
                        txtRaCpf.setText("CPF: " + documentSnapshotPerfil.getString("cpf"));
                        txtNomeProfessor.setText("");
                    }else{
                        txtOcupacao.setText("Estudante");
                        txtRaCpf.setText("RA: " + documentSnapshotPerfil.getString("ra"));

                        turmaRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot documentSnapshotTurma, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                                if(documentSnapshotTurma != null){
                                    txtTurma.setText(documentSnapshotTurma.getString("sala"));

                                    String professorID =  documentSnapshotTurma.getString("professor");
                                    DocumentReference professorRef = db.collection("Usuario").document(professorID);

                                    professorRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot documentSnapshotProf, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                                            if(documentSnapshotProf != null)
                                                txtNomeProfessor.setText("Professor(a): "+documentSnapshotProf.getString("nome"));
                                        }
                                    });
                                }

                            }
                        });
                    }

                    escolaRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot documentSnapshotEscola, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                            if(documentSnapshotEscola != null)
                                txtEscola.setText("Escola: "+documentSnapshotEscola.getString("nome"));
                        }
                    });

                    turmaRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot documentSnapshotTurma, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                            if(documentSnapshotTurma!=null)
                                txtTurma.setText("Turma: "+documentSnapshotTurma.getString("sala"));
                        }
                    });
                }
            }
        });

    }
}