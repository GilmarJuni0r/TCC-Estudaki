package nelson.aparecido.estudaki;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;

public class TelaPerfil extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_perfil);

        StatusBarUtil.setTransparent(this);



    }

}




