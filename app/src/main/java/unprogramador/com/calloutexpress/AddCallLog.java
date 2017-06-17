package unprogramador.com.calloutexpress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import unprogramador.com.calloutexpress.Sqlite.Querys;
import unprogramador.com.calloutexpress.Sqlite.SqliteHelper;

public class AddCallLog extends AppCompatActivity {
    String phone, name,edit;
    EditText nameET,phoneET;
    Button saveb, cancelb, deleteb;
    Querys q = new Querys();
    SqliteHelper sqlh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_call_log);
        Bundle extras = getIntent().getExtras();
        nameET = (EditText)findViewById(R.id.insertname);
        phoneET = (EditText)findViewById(R.id.insertphone);
        saveb = (Button)findViewById(R.id.saveb);
        cancelb = (Button)findViewById(R.id.cancelb);
        deleteb = (Button)findViewById(R.id.deleteb);


        if (extras != null) {
            name = extras.getString("name");
            phone = extras.getString("phone");
            edit = extras.getString("edit");

            if(name != "" || name != null){
                nameET.setText(name);
            }
            if(phone != "" || phone != null){
                phoneET.setText(phone);
            }
            if(edit.equals("true")){
                deleteb.setVisibility(View.VISIBLE);
            }
        }

        deleteb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlh = new SqliteHelper(getApplicationContext());
                String Phones = phoneET.getText().toString().trim();
                if(Phones != "" || Phones != null){
                    if(q.deleteContact(sqlh,Phones)){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        saveb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = nameET.getText().toString();
                String p = phoneET.getText().toString().trim();
                sqlh = new SqliteHelper(getApplicationContext());
                q = new Querys();
                if(p.trim() != "" || p != null){
                    p = p.replace("(", "");
                    p = p.replace(")", "");
                    p = p.replace("-", "");
                    p = p.replace(" ", "");
                    boolean qq = q.insert(sqlh,n,p);
                    if (qq){
                        Intent home = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(home);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),R.string.error_insert,Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),R.string.number_null,Toast.LENGTH_SHORT);
                }


            }
        });
        cancelb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(home);
                finish();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
