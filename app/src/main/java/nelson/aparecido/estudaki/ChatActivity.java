package nelson.aparecido.estudaki;

        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Parcelable;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.firestore.DocumentChange;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.EventListener;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.FirebaseFirestoreException;
        import com.google.firebase.firestore.Query;
        import com.google.firebase.firestore.QuerySnapshot;
        import com.jaeger.library.StatusBarUtil;
        import com.squareup.picasso.Picasso;
        import com.xwray.groupie.GroupAdapter;
        import com.xwray.groupie.Item;
        import com.xwray.groupie.ViewHolder;

        import org.jetbrains.annotations.NotNull;

        import java.util.List;

public class ChatActivity extends AppCompatActivity {

    View calendario, lupa, home, professor, perfil;
    private TextView nomeContato;
    private GroupAdapter adapter;
    private Usuario usuario;
    private EditText edtMensagem;
    private Usuario aux;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_chat);
        StatusBarUtil.setTransparent(this);

        usuario = getIntent().getExtras().getParcelable("usuario");
        nomeContato = findViewById(R.id.txt_chatNome);
        nomeContato.setText(usuario.getNome());
        RecyclerView recyclerChat = findViewById(R.id.recycler_chat);
        edtMensagem = findViewById(R.id.edit_mensagem);
        Button btnEnviar = findViewById(R.id.btn_enviar_mensagem);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensagem();
            }
        });

        adapter = new GroupAdapter();
        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerChat.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("Usuario")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                aux = documentSnapshot.toObject(Usuario.class);
                fetchMessages();
            }
        });

        ////////////////////////////////////////////////////////////////////////////////////////////

        /* BARRA DE TAREFA */
        calendario = findViewById(R.id.view_calendario);
        lupa = findViewById(R.id.view_lupa);
        home = findViewById(R.id.view_home);
        professor = findViewById(R.id.view_conversa_professor);
        perfil = findViewById(R.id.view_perfil);


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
        /*  FIM BARRA DE TAREFA */
    }

    private void fetchMessages() {
        if(aux != null){
            String fromId = aux.getUid();
            String toId = usuario.getUid();

            FirebaseFirestore.getInstance().collection("Conversas")
                    .document(fromId).collection(toId).orderBy("timestamp", Query.Direction.ASCENDING)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                            List<DocumentChange> documentChanges = value.getDocumentChanges();

                            if(documentChanges != null){
                                for (DocumentChange doc: documentChanges) {
                                    if(doc.getType() == DocumentChange.Type.ADDED){
                                        Mensagem mensagem = doc.getDocument().toObject(Mensagem.class);
                                        adapter.add(new MessageItem(mensagem , mensagem.getFromId()));
                                    }
                                }
                            }
                        }
                    });
        }
    }

    private void enviarMensagem() {
        String texto = edtMensagem.getText().toString();
        edtMensagem.setText(null);

        String fromId = FirebaseAuth.getInstance().getUid();
        String toId = usuario.getUid();
        long timestamp = System.currentTimeMillis();

        Mensagem mensagem = new Mensagem();
        mensagem.setTexto(texto);
        mensagem.setFromId(fromId);
        mensagem.setToId(toId);
        mensagem.setTimestamp(timestamp);

        if(!mensagem.getTexto().isEmpty()){
            FirebaseFirestore.getInstance().collection("Conversas").document(fromId).collection(toId)
                    .add(mensagem).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {

                }
            });
            FirebaseFirestore.getInstance().collection("Conversas").document(toId).collection(fromId)
                    .add(mensagem).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {

                }
            });
        }
    }

    private class MessageItem extends Item<ViewHolder> {

        private final Mensagem mensagem;
        private String fromId;

        private MessageItem(Mensagem mensagem,String Id) {
            this.mensagem = mensagem;
            this.fromId = Id;
        }

        public String getFromId() {
            return fromId;
        }

        public void setFromId(String id) {
            this.fromId = id;
        }

        @Override
        public void bind(@NonNull @NotNull ViewHolder viewHolder, int position) {
            TextView txtMensagem = viewHolder.itemView.findViewById(R.id.txt_mensagem);
            ImageView imgMensagem = viewHolder.itemView.findViewById(R.id.img_mensagem);

            txtMensagem.setText(mensagem.getTexto());
            Picasso.get().load(usuario.getFotoPerfil()).into(imgMensagem);

            // aqui eu baixo a foto da pessoa que enviou a mensagem e coloco no imageView pelo Picasso


            DocumentReference docRef = FirebaseFirestore.getInstance()
                    .collection("Usuario")
                    .document(fromId);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()){
                            String profileUrl = document.get("fotoPerfil").toString();

                            Picasso.get()
                                    .load(profileUrl)
                                    .fit().centerInside()
                                    .into(imgMensagem);
                        }
                    }
                }
            });
            //txtMsg.setText(message.getText());
        }

        @Override
        public int getLayout() {

            return mensagem.getFromId().equals( FirebaseAuth.getInstance().getUid())
                    ? R.layout.item_to_message
                    : R.layout.item_from_message;
        }
    }
}
