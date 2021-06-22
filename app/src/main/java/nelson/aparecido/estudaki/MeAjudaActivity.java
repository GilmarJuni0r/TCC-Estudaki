package nelson.aparecido.estudaki;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jaeger.library.StatusBarUtil;

public class MeAjudaActivity extends AppCompatActivity {

    private Button btn1_txt, btn2_txt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_me_ajuda);

        StatusBarUtil.setTransparent(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        btn1_txt = (Button) findViewById(R.id.btn_pergunta1);
        btn2_txt = (Button) findViewById(R.id.btn_pergunta2);

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioAtual != null) {
            btn1_txt.setText("NÃO CONSIGO ACHAR A MATÉRIA");
            btn1_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exibirConfirmacao("Per1WithLogin");
                }
            });

            btn2_txt.setText("ONDE VEJO MINHA NOTA?");
            btn2_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exibirConfirmacao("Per2WithLogin");
                }
            });
        } else {
            btn1_txt.setText("NÃO TENHO LOGIN E NEM A SENHA");
            btn1_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exibirConfirmacao("Per1WithoutLogin");
                }
            });
            btn2_txt.setText("ESQUECI MINHA SENHA");
            btn2_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exibirConfirmacao("Per2WithoutLogin");
                }
            });
        }
    }

    public void exibirConfirmacao(String orientacao) {
        AlertDialog.Builder msgBox = new AlertDialog.Builder(this);
        if (orientacao == "Per1WithoutLogin") {
            msgBox.setTitle("NÃO TENHO LOGIN E NEM A SENHA");
            msgBox.setMessage("ENTRE EM CONTATO COM A SECRETARIA DA SUA ESCOLA, E SOLICITE O SEU LOGIN E SENHA PARA ACESSO AO APLICATIVO ESTUDAKI.");
            msgBox.show();
        } else if (orientacao == "Per2WithoutLogin") {
            msgBox.setTitle("ESQUECI MINHA SENHA");
            msgBox.setMessage("ENTRE EM CONTATO COM A SECRETARIA DA SUA ESCOLA, E SOLICITE A NOVA SENHA.");
            msgBox.show();
        } else if (orientacao == "Per1WithLogin") {
            msgBox.setTitle("NÃO CONSIGO ACHAR A MATÉRIA");
            msgBox.setMessage("VÁ PARA GUIA MATÉRIAS E PROCURE A MATÉRIA QUE DESEJA;\n" +
                    "\n" +
                    "MATÉRIAS > PORTUGUÊS, MATEMÁTICA...\n" +
                    "\n" +
                    "CASO SUA MATÉRIA NÃO ESTEJA LÁ, CONTATE A SECRETARIA OU O SEU PROFESSOR.");
            msgBox.show();
        } else if (orientacao == "Per2WithLogin") {
            msgBox.setTitle("ONDE VEJO MINHA NOTA?");
            msgBox.setMessage("SELECIONE A OPÇÃO DE NOTAS NA BARRA DE TAREFAS E VERIFIQUE AS ATIVIDADES QUE FORAM ENTREGUES\n" +
                    "\n" +
                    "CASO A NOTA DA ATIVIDADE AINDA NÃO ESTEJA LÁ, AGUARDE OU CONTATE O SEU PROFESSOR.");
            msgBox.show();
        }
    }
}
