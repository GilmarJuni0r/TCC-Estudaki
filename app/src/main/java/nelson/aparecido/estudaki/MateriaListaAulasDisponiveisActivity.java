package nelson.aparecido.estudaki;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jaeger.library.StatusBarUtil;

public class MateriaListaAulasDisponiveisActivity extends AppCompatActivity {

    private View calendario, lupa, home, professor, perfil, btn_me_ajuda, btnAulaAoVivo;
    private ImageView btnUploadAula, btnEditaLink;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String usuarioID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_lista_aulas_disponiveis);
        StatusBarUtil.setTransparent(this);
        barraDeTarefas();

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        btnAulaAoVivo = findViewById(R.id.view_aulas_ao_vivo);
        btnUploadAula = findViewById(R.id.btn_upload_aula);
        btnEditaLink = findViewById(R.id.btn_edit_link);

        DocumentReference refUser = db.collection("Usuario").document(usuarioID);
        refUser.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(!(value.getString("ocupacao").equalsIgnoreCase("professor") || value.getString("ocupacao").equalsIgnoreCase("professora"))){
                    btnEditaLink.setVisibility(View.INVISIBLE);
                    btnUploadAula.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnAulaAoVivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        DocumentReference refAux = db.collection("Turma").document(value.getString("turma"));
                        refAux.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot docref, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                                String link = docref.getString("linkAula");
                                if(!link.equalsIgnoreCase("")){
                                    gotoURL(link);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Nenhum link para aulas ao vivo foi cadastrado", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

            }
        });
        btnEditaLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TelaCadastroLinkAula.class);
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

        btn_me_ajuda = (View) findViewById(R.id.view_me_ajuda_lista_aulas_disponiveis);
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

    private void gotoURL(String s) {

        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
}

