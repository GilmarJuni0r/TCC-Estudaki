package nelson.aparecido.estudaki;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.jaeger.library.StatusBarUtil;

public class tela_login extends AppCompatActivity {

    Button button_login;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_login);

        StatusBarUtil.setTransparent(this);


        button_login = findViewById(R.id.button_login);


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoURL("https://drive.google.com/uc?export=download&id=1zykW1stbon-IPyOkgixxz5UsLmDXQ2Ym");




                }
        });


    }

    private void gotoURL(String s) {

        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }


}
