package nelson.aparecido.estudaki;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
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

public class MateriaMaterialAulaActivity extends AppCompatActivity {

    private View calendario, lupa, home, professor, perfil, btn_me_ajuda;
    private TextView nomeMateria;
    private ImageView iconMateria, btnUploadMaterial;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String usuarioID;
    private GroupAdapter adapterMateriais;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_materia_material_de_aulas);
        StatusBarUtil.setTransparent(this);
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        barraDeTarefas();
        cabecalho();

        RecyclerView recyclerMateriais = findViewById(R.id.recycler_Materiais_Disponiveis);
        recyclerMateriais.setLayoutManager(new LinearLayoutManager(this));
        adapterMateriais = new GroupAdapter();
        recyclerMateriais.setAdapter(adapterMateriais);

        adapterMateriais.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull Item item, @NonNull @NotNull View view) {
                Intent intent = new Intent(MateriaMaterialAulaActivity.this, DescricaoAtividadeActivity.class);

                MaterialItem materialItem = (MaterialItem) item;
                intent.putExtra("arquivo", materialItem.materialAula);

                startActivity(intent);
            }
        });

        fetchMateriais();
    }

    private void fetchMateriais() {
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
                                    MaterialAula material = doc.toObject(MaterialAula.class);
                                    if(material.getTipoArquivo().equalsIgnoreCase("material"))
                                        if(material.getTurma().equalsIgnoreCase(user.getString("turma")))
                                            if(material.getMateria().equalsIgnoreCase(user.getString("materiaAtual")))
                                                adapterMateriais.add(new MaterialItem(material));

                                }
                            }
                        });

                    }
                });
    }

    private void cabecalho() {
        nomeMateria = findViewById(R.id.txt_nome_materia_material_de_aula);
        iconMateria = findViewById(R.id.img_icon_materia_material_de_aula);


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

        btnUploadMaterial = findViewById(R.id.btn_upload_material_de_aula);
        btnUploadMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadMaterial.class);
                startActivity(intent);
            }
        });
        btn_me_ajuda = (View) findViewById(R.id.view_me_ajuda_materia_material_de_aulas);
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

    private class MaterialItem extends Item<ViewHolder>{

        private final MaterialAula materialAula;

        private MaterialItem(MaterialAula materialAula) {
            this.materialAula = materialAula;
        }

        @Override
        public void bind(@NonNull @NotNull ViewHolder viewHolder, int position) {
            TextView txtTitulo = viewHolder.itemView.findViewById(R.id.txt_titulo_aula);
            ImageView ivIcon = viewHolder.itemView.findViewById(R.id.iv_icon_aulas_item);
            ImageView ivDownload = viewHolder.itemView.findViewById(R.id.btn_download_aulas_item);

            txtTitulo.setText(materialAula.getTitulo());
            if(materialAula.getTipoArquivo().equalsIgnoreCase("material")) {
                Drawable drawable = getResources().getDrawable(R.drawable.img_materias_aulas);
                ivIcon.setImageDrawable(drawable);
            }
        }

        @Override
        public int getLayout() {
            return R.layout.item_materiais_aulas;
        }
    }
}
