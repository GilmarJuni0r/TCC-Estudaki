package nelson.aparecido.estudaki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;

public class TelaMenuMaterial extends AppCompatActivity {

        View aulas;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_menu_material);

        StatusBarUtil.setTransparent(this);



        aulas = findViewById(R.id.view_aulas);

        aulas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), TelaListaAulasDisponiveis.class);
                startActivity(intent);


            }
        });




    }

}



