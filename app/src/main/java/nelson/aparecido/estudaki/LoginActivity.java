package nelson.aparecido.estudaki;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jaeger.library.StatusBarUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txt_email, txt_senha;
    private Button btn_login;
    private View btn_me_ajuda;

    private FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);

        StatusBarUtil.setTransparent(this);

        //Ir para tela Me Ajuda
        btn_me_ajuda = (View) findViewById(R.id.view_meAjuda);
        btn_me_ajuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MeAjudaActivity.class);
                startActivity(intent);
            }
        });

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_senha = (EditText) findViewById(R.id.txt_senha);

        mAuth = FirebaseAuth.getInstance();

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_login:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = txt_email.getText().toString();
        String senha = txt_senha.getText().toString();

        if (email.isEmpty()) {
            txt_email.setError("Insira um endereço de e-mail");
            txt_email.requestFocus();
            return;
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txt_email.setError("Insira um e-mail válido");
            txt_email.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            txt_senha.setError("Insira a senha");
            txt_senha.requestFocus();
            return;
        }

        else if (senha.length() < 6) {
            txt_senha.setError("Insira uma senha com pelo menos 6 dígitos");
            txt_senha.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Falha ao realizar login! Revise seus dados", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if(usuarioAtual != null){
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            startActivity(intent);
        }
    }
}