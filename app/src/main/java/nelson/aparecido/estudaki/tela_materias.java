package nelson.aparecido.estudaki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jaeger.library.StatusBarUtil;

public class tela_materias extends AppCompatActivity {

            ConstraintLayout matematica;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_materias);

        StatusBarUtil.setTransparent(this);




        matematica = findViewById(R.id.constaint_matematica);

        matematica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), tela_materias_matematica.class);
                startActivity(intent);


            }
        });







    }

}
