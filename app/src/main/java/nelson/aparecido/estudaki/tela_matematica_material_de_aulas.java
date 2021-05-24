package nelson.aparecido.estudaki;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;

public class tela_matematica_material_de_aulas extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_matematica_material_de_aulas);

        StatusBarUtil.setTransparent(this);



    }

}
