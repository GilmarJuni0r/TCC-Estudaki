package nelson.aparecido.estudaki;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jaeger.library.StatusBarUtil;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class PerfilActivity extends AppCompatActivity {

    private View calendario, lupa, home, professor, perfil, btnLogout;
    private TextView txtNome, txtOcupacao, txtTurma, txtIdade, txtRaCpf, txtNomeProfessor, txtEscola, txtEmail;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String usuarioID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_perfil);

        StatusBarUtil.setTransparent(this);
        inicializarComponentes();

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

    protected void inicializarComponentes(){

        //Compenentes da tela
        btnLogout = findViewById(R.id.btn_logout);

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

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot documentSnapshotPerfil, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(documentSnapshotPerfil != null){
                    String dataNascimento = documentSnapshotPerfil.getString("dataNascimento");

                    String turmaID = documentSnapshotPerfil.getString("turma");
                    DocumentReference turmaRef = db.collection("Turma").document(turmaID);

                    String escolaID = documentSnapshotPerfil.getString("escola");
                    DocumentReference escolaRef = db.collection("Escola").document(escolaID);

                    txtNome.setText(documentSnapshotPerfil.getString("nome"));
                    txtEmail.setText("Email: "+FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    txtIdade.setText("Data de nascimento: "+documentSnapshotPerfil.getString("dataNascimento") +"\nIdade: "+ calculaIdade(dataNascimento) + " anos");

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