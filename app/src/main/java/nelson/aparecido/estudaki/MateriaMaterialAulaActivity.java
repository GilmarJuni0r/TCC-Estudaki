package nelson.aparecido.estudaki;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;

public class MateriaMaterialAulaActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_materia_material_de_aulas);

        StatusBarUtil.setTransparent(this);



    }

}
