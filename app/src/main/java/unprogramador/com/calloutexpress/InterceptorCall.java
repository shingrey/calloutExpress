package unprogramador.com.calloutexpress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;

import unprogramador.com.calloutexpress.Sqlite.Querys;
import unprogramador.com.calloutexpress.Sqlite.SqliteHelper;

import static android.content.ContentValues.TAG;

public class InterceptorCall extends BroadcastReceiver {
    Querys q = new Querys();
    SqliteHelper sqlh;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras != null){
            try{
                if ( extras.getString("state").equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    String phoneNumber = extras.getString("incoming_number");
                    this.sqlh = new SqliteHelper(context);
                    String name = q.select(this.sqlh,phoneNumber);
                    if(name.trim() != ""){
                        killCall callout = new killCall();
                        boolean co = callout.callOut(context);

                    }

                /*String incomingNumber =
                        intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d(TAG,"PhoneStateReceiver**Incoming call " + incomingNumber);
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        killCall(context);
                    }
                },10);*/

                }

            }catch (Exception e){

            }

        }
    }

}
