package nelson.aparecido.estudaki;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;

public class tela_materias extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_materias);

        StatusBarUtil.setTransparent(this);


    }

}
