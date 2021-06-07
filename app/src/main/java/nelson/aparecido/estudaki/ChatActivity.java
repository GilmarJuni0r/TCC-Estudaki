package nelson.aparecido.estudaki;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.jaeger.library.StatusBarUtil;
        import com.xwray.groupie.GroupAdapter;
        import com.xwray.groupie.Item;
        import com.xwray.groupie.ViewHolder;

        import org.jetbrains.annotations.NotNull;

public class ChatActivity extends AppCompatActivity {

    View calendario, lupa, home, professor, perfil;
    private TextView nomeContato;
    private GroupAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_chat);
        StatusBarUtil.setTransparent(this);
        nomeContato = findViewById(R.id.txt_chatNome);

        Usuario usuario = getIntent().getExtras().getParcelable("usuario");
        nomeContato.setText(usuario.getNome());

        RecyclerView recyclerChat = findViewById(R.id.recycler_chat);
        adapter = new GroupAdapter();

        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerChat.setAdapter(adapter);

        adapter.add(new MessageItem(true));
        adapter.add(new MessageItem(false));
        adapter.add(new MessageItem(true));
        adapter.add(new MessageItem(false));
        adapter.add(new MessageItem(true));
        adapter.add(new MessageItem(false));
        adapter.add(new MessageItem(true));
        adapter.add(new MessageItem(true));

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

    private class MessageItem extends Item<ViewHolder> {

        private final boolean receptor;

        private MessageItem(boolean receptor){
            this.receptor = receptor;
        }
        @Override
        public void bind(@NonNull @NotNull ViewHolder viewHolder, int position) {

        }

        @Override
        public int getLayout() {
            return receptor ? R.layout.item_receptor : R.layout.item_emissor;
        }
    }
}
