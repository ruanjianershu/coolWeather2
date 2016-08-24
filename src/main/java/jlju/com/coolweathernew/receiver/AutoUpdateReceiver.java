package jlju.com.coolweathernew.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import jlju.com.coolweathernew.service.AutoUpdateService;

/**
 * Created by 11415 on 2016/8/24.
 */
public class AutoUpdateReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startActivity(i);
    }
}
