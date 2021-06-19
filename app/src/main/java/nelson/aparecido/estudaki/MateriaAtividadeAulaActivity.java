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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaeger.library.StatusBarUtil;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;
import com.xwray.groupie.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MateriaAtividadeAulaActivity extends AppCompatActivity {

    private View calendario, lupa, home, professor, perfil, btn_me_ajuda;
    private TextView nomeMateria;
    private ImageView iconMateria, btnUploadAtividade;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String usuarioID;
    private GroupAdapter adapterAtividades;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_atividades);
        StatusBarUtil.setTransparent(this);
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        barraDeTarefas();
        cabecalho();

        RecyclerView recyclerAtividades = findViewById(R.id.recyclerAtividades);
        recyclerAtividades.setLayoutManager(new LinearLayoutManager(this));
        adapterAtividades = new GroupAdapter();
        recyclerAtividades.setAdapter(adapterAtividades);

        adapterAtividades.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull Item item, @NonNull @NotNull View view) {
                Intent intent = new Intent(MateriaAtividadeAulaActivity.this, DescricaoAtividadeActivity.class);

                MateriaAtividadeAulaActivity.AtividadeItem atividadeItem = (MateriaAtividadeAulaActivity.AtividadeItem) item;
                intent.putExtra("atividade", atividadeItem.atividadeProva);

                startActivity(intent);
            }
        });

        fetchAtividades();
    }

    private void fetchAtividades() {
        FirebaseFirestore.getInstance().collection("Arquivos").orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        String usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("Usuario").document(usuarioID);
                        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot user, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                                List<DocumentSnapshot> docs = value.getDocuments();
                                for (DocumentSnapshot doc : docs) {
                                    AtividadeProva atividadeProva = doc.toObject(AtividadeProva.class);
                                    if(atividadeProva.getTipoArquivo().equalsIgnoreCase("atividade"))
                                        if(atividadeProva.getTurma().equalsIgnoreCase(user.getString("turma")))
                                            if(atividadeProva.getMateria().equalsIgnoreCase(user.getString("materiaAtual")))
                                                adapterAtividades.add(new MateriaAtividadeAulaActivity.AtividadeItem(atividadeProva));
                                }
                            }
                        });
                    }
                });
    }

    private void cabecalho() {
        nomeMateria = findViewById(R.id.txt_nome_materia_atividades);
        iconMateria = findViewById(R.id.img_icon_materia_atividades);

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

        btnUploadAtividade = findViewById(R.id.btn_upload_nova_atividade_menu);
        btnUploadAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadAtividades.class);
                startActivity(intent);
            }
        });

        btn_me_ajuda = (View) findViewById(R.id.view_me_ajuda_atividades);
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

    private class AtividadeItem extends Item<ViewHolder>{

        private final AtividadeProva atividadeProva;

        private AtividadeItem(AtividadeProva atividadeProva) {
            this.atividadeProva = atividadeProva;
        }

        @Override
        public void bind(@NonNull @NotNull ViewHolder viewHolder, int position) {
            TextView txtTitulo = viewHolder.itemView.findViewById(R.id.txt_titulo_aula);
            ImageView ivIcon = viewHolder.itemView.findViewById(R.id.iv_icon_aulas_item);

            txtTitulo.setText(atividadeProva.getTitulo());
            if(atividadeProva.getTipoArquivo().equalsIgnoreCase("atividade")) {
                Drawable drawable = getResources().getDrawable(R.drawable.img_correcao_atividades);
                ivIcon.setImageDrawable(drawable);
            }
        }

        @Override
        public int getLayout() {
            return R.layout.item_materiais_aulas;
        }
    }
}
