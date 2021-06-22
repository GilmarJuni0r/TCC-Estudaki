
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

public class AtividadesEntregues extends AppCompatActivity {

    private View calendario, nota, home, professor, perfil, btn_me_ajuda;
    TextView nomeAluno;
    private GroupAdapter adapterAtividadesEntregues;
    private Usuario usuario;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String usuarioID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_atividades_entregues);
        StatusBarUtil.setTransparent(this);
        barraDeTarefas();

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        RecyclerView recyclerAtividadesEntregues = findViewById(R.id.recycler_atividades_entregues);
        recyclerAtividadesEntregues.setLayoutManager(new LinearLayoutManager(this));
        adapterAtividadesEntregues = new GroupAdapter();
        recyclerAtividadesEntregues.setAdapter(adapterAtividadesEntregues);

        adapterAtividadesEntregues.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull @NotNull Item item, @NonNull @NotNull View view) {
                Intent intent = new Intent(AtividadesEntregues.this, AtribuirNota.class);

                AtividadesEntregues.AtividadesEntItem atividadeEntItem = (AtividadesEntregues.AtividadesEntItem) item;
                intent.putExtra("atividadeEntregue", atividadeEntItem.atividadeEntregue);

                startActivity(intent);
            }
        });

        fetchAtividadesEntregues();
    }

    private void fetchAtividadesEntregues() {
        FirebaseFirestore.getInstance().collection("Respostas").orderBy("timestamp", Query.Direction.ASCENDING)
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
                                    Respostas atividadeEntregue = doc.toObject(Respostas.class);
                                    if (user.getString("ocupacao").equalsIgnoreCase("professor") || user.getString("ocupacao").equalsIgnoreCase("professora")) {
                                        usuario = getIntent().getExtras().getParcelable("usuario");
                                        if (atividadeEntregue.getAlunoID().equalsIgnoreCase(usuario.getUid())) {
                                            nomeAluno.setText(usuario.getNome());
                                            adapterAtividadesEntregues.add(new AtividadesEntregues.AtividadesEntItem(atividadeEntregue));
                                        }
                                    } else {
                                        if (atividadeEntregue.getAlunoID().equalsIgnoreCase(usuarioID))
                                            nomeAluno.setText(user.getString("nome"));
                                        adapterAtividadesEntregues.add(new AtividadesEntregues.AtividadesEntItem(atividadeEntregue));
                                    }
                                }
                            }
                        });
                    }
                });
    }

    private void barraDeTarefas() {
        calendario = findViewById(R.id.view_calendario);
        nota = findViewById(R.id.view_notas);
        home = findViewById(R.id.view_home);
        professor = findViewById(R.id.view_conversa_professor);
        perfil = findViewById(R.id.view_perfil);
        nomeAluno = findViewById(R.id.txt_nomeAlunoAtividadesEntregues);

        btn_me_ajuda = (View) findViewById(R.id.view_me_ajuda_atividadesEntregues);
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

    private class AtividadesEntItem extends Item<ViewHolder> {

        private final Respostas atividadeEntregue;

        private AtividadesEntItem(Respostas atividadeEntregue) {
            this.atividadeEntregue = atividadeEntregue;
        }

        @Override
        public void bind(@NonNull @NotNull ViewHolder viewHolder, int position) {
            TextView txtTitulo = viewHolder.itemView.findViewById(R.id.txt_titulo_notas);
            TextView txtNota = viewHolder.itemView.findViewById(R.id.txt_nota_notas);
            ImageView ivIcon = viewHolder.itemView.findViewById(R.id.iv_icon_notas_item);

            txtTitulo.setText(atividadeEntregue.getMateria() + " - " + atividadeEntregue.getTituloProvaAtividade());

            if (atividadeEntregue.getNota().equalsIgnoreCase("")) {
                txtNota.setText("-");
            } else {
                txtNota.setText("Nota:\n" + atividadeEntregue.getNota());
            }


            if (atividadeEntregue.getTipo().equalsIgnoreCase("atividade")) {
                Drawable drawable = getResources().getDrawable(R.drawable.img_correcao_atividades);
                ivIcon.setImageDrawable(drawable);
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.img_provas_atividades);
                ivIcon.setImageDrawable(drawable);
            }
        }

        @Override
        public int getLayout() {
            return R.layout.item_notas;
        }
    }
}


