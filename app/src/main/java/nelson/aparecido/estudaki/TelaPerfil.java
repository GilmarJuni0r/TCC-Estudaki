package nelson.aparecido.estudaki;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.jaeger.library.StatusBarUtil;

public class TelaPerfil extends AppCompatActivity {


    private View calendario, lupa, home, professor, perfil, btnLogout;
    private TextView txtNome, txtOcupacao, txtTurma, txtIdade, txtRaCpf, txtNomeProfessor, txtEscola, txtEmail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_perfil);

        StatusBarUtil.setTransparent(this);
        inicializarComponentes();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(TelaPerfil.this, TelaLogin.class);
                startActivity(intent);
                finish();
            }
        });

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), TelaPerfil.class);
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

    protected void inicializarComponentes(){

        btnLogout = findViewById(R.id.btn_logout);

        txtNome = findViewById(R.id.txt_nome);
        txtOcupacao = findViewById(R.id.txt_ocupacao);
        txtTurma = findViewById(R.id.txt_turma);
        txtIdade = findViewById(R.id.txt_idade);
        txtRaCpf = findViewById(R.id.txt_raCpf);
        txtNomeProfessor = findViewById(R.id.txt_NomeProfessor);
        txtEscola = findViewById(R.id.txt_NomeEscola);
        txtEmail = findViewById(R.id.txt_emailPerfil);

        /* BARRA DE TAREFA */
        calendario = findViewById(R.id.view_calendario);
        lupa = findViewById(R.id.view_lupa);
        home = findViewById(R.id.view_home);
        professor = findViewById(R.id.view_conversa_professor);
        perfil = findViewById(R.id.view_perfil);
    }
}




