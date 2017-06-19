package unprogramador.com.calloutexpress;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import unprogramador.com.calloutexpress.Sqlite.Querys;
import unprogramador.com.calloutexpress.Sqlite.SqliteHelper;

public class MainActivity extends AppCompatActivity {
    int permissionCheckCall;
    private static final int PERMISSION_ACCESS_CALL_PHONE = 1;
    private static final int REQUEST_SELECT_CONTACT = 1;
    Querys q = new Querys();
    SqliteHelper sqlh;
    ArrayList<ArrayList<String>> listBlock;
    ListView ListA;
    ArrayList<BlockNumberData> arrayList;
    BlockNumberData BlockData;

    @Override
    //Actividad que regresa contact aqui podemos ver el contacto
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //comprovamos que requestcode sea 1 y resultcode sea ok
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
            Cursor cursor = getContentResolver()
                    .query(contactUri, projection, null, null, null);
            cursor.moveToFirst();
            int numberColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String number = cursor.getString(numberColumn);
            String name = cursor.getString(nameColumn);
            Log.d("miraloooo: ", number);
            number = number.replace("(", "");
            number = number.replace(")", "");
            number = number.replace("-", "");
            number = number.replace(" ", "");
            sqlh = new SqliteHelper(getApplicationContext());
            boolean insert = q.insert(sqlh, name, number);
            if (insert) {
                Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(refresh);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG);
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) < 0 || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) < 0 ){
                    Toast.makeText(getApplicationContext(),R.string.notaccess,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);

                }else{
                    final AlertDialog.Builder option = new AlertDialog.Builder(MainActivity.this);
                    View viewf = getLayoutInflater().inflate(R.layout.option, null);
                    Button contact = (Button) viewf.findViewById(R.id.contact);
                    Button calllog = (Button) viewf.findViewById(R.id.log);
                    Button insertphone = (Button) viewf.findViewById(R.id.insert);
                    option.setView(viewf);
                    option.create().show();
                    contact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            contact();
                        }
                    });
                    calllog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            calllogselect();

                        }
                    });
                    insertphone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent callwrite = new Intent(getApplicationContext(), AddCallLog.class);
                            startActivity(callwrite);
                            finish();
                        }
                    });

                }

            }
        });
        permissionCheckCall = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            //pide permisos para el telefono
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CALL_LOG, Manifest.permission.READ_CALL_LOG}, PERMISSION_ACCESS_CALL_PHONE);

        }
        //inyectamos en el list
        sqlh = new SqliteHelper(getApplicationContext());
        listBlock = q.selectnumberblock(sqlh);
        arrayList = new ArrayList<BlockNumberData>();
        ListA = (ListView) findViewById(R.id.listBlockMain);
        ListA.setAdapter(null);
        if(listBlock.size() > 0){
            arrayList.clear();
            for(int a = 0; a < listBlock.size(); a++){
                BlockData = new BlockNumberData(Long.parseLong(listBlock.get(a).get(0)),listBlock.get(a).get(1),listBlock.get(a).get(2));
                arrayList.add(BlockData);
            }
            BlockNumberAdapter adapter = new BlockNumberAdapter(this,arrayList);
            ListA.setAdapter(null);
            ListA.setAdapter(adapter);
        }
        ListA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent vv = new Intent(getApplication(),AddCallLog.class);
                vv.putExtra("name",listBlock.get(position).get(1));
                vv.putExtra("phone",listBlock.get(position).get(2));
                vv.putExtra("edit","true");
                startActivity(vv);
                finish();
            }
        });

    }

    private void contact() {
        //selecciona numero de contacto si tienen mas de un numero
        Intent intent1 = new Intent(Intent.ACTION_PICK);
        intent1.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent1, 1);
    }

    private void calllogselect() {
        //verifica si fue concedido el permiso 0 = si
        if (permissionCheckCall == 0) {

            String[] strFields = {android.provider.CallLog.Calls._ID,
                    android.provider.CallLog.Calls.NUMBER,
                    android.provider.CallLog.Calls.CACHED_NAME,};
            String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {

            }
            final Cursor cursorCall = getContentResolver().query(
                    android.provider.CallLog.Calls.CONTENT_URI, strFields,
                    null, null, strOrder);

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    MainActivity.this);
            builder.setTitle(R.string.recent_call);
            android.content.DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface,
                                    int item) {
                    String NumberDialog, NameDialog;
                    cursorCall.moveToPosition(item);
                    //recuperamos el numero y el nombre si tiene
                    NumberDialog = cursorCall.getString(cursorCall.getColumnIndex(CallLog.Calls.NUMBER));
                    NameDialog = cursorCall.getString(cursorCall.getColumnIndex(CallLog.Calls.CACHED_NAME));
                    cursorCall.close();
                    //lanzamos la nueva actividad
                    editlog(NameDialog,NumberDialog);
                    return;
                }
            };

            builder.setCursor(cursorCall, listener,android.provider.CallLog.Calls.NUMBER);
            builder.create().show();
            return;
        }


    }
    private void editlog(String name, String phone){
        Intent callwrite = new Intent(getApplicationContext(), AddCallLog.class);
        callwrite.putExtra("name",name);
        callwrite.putExtra("phone",phone);
        callwrite.putExtra("edit","false");
        startActivity(callwrite);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent About = new Intent(getApplicationContext(), unprogramador.com.calloutexpress.About.class);
            startActivity(About);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
