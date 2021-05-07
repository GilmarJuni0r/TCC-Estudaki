package nelson.aparecido.estudaki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StatusBarUtil.setTransparent(this);








    }
}