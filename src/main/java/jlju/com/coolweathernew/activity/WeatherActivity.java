package jlju.com.coolweathernew.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import jlju.com.coolweathernew.R;
import jlju.com.coolweathernew.service.AutoUpdateService;
import jlju.com.coolweathernew.util.HttpCallbackListener;
import jlju.com.coolweathernew.util.HttpUtil;
import jlju.com.coolweathernew.util.Utility;

public class WeatherActivity extends Activity implements View.OnClickListener {

    private LinearLayout weatherInfoLayout;
    /**
     * 切换城市按钮
     */
    private Button switchCity;

    /**
     * 更新天气按钮
     */
    private Button refreshWeather;
    /**
     * 用于显示城市名
     */
    private TextView cityNameText;

    /**
     * 用于显示发布时间
     */
    private TextView publishText;

    /**
     * 用于显示天气描述信息
     */
    private TextView weatherDespText;

    /**
     * 用于显示气温1
     */
    private TextView temp1Text;

    /**
     * 用于显示气温2
     */
    private TextView temp2Text;

    /**
     * 用于显示当前日期
     */
    private TextView currentDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);

        switchCity = (Button)findViewById(R.id.switch_city);
        refreshWeather = (Button)findViewById(R.id.refresh_weather);
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
        //初始化各种控件
        weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
        cityNameText = (TextView)findViewById(R.id.city_name);
        publishText = (TextView)findViewById(R.id.publish_text);
        weatherDespText = (TextView)findViewById(R.id.weather_desp);
        temp1Text = (TextView)findViewById(R.id.temp1);
        temp2Text = (TextView)findViewById(R.id.temp2);
        currentDateText = (TextView)findViewById(R.id.current_date);

        String countyCode = getIntent().getStringExtra("county_code");
        if(!TextUtils.isEmpty(countyCode)){
            //有县级代号就去查询天气
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        }else{
            showWehther();
        }
    }

    /**
     * 查询县级代号锁对应的天气代号
     */
    private void queryWeatherCode(String countryCode){
        String address = "http://www.weather.com.cn/data/list3/city"+countryCode+".xml";
        queryFromServer(address,"countyCode");
    }

    /**
     * 查询天气代号所对应的天气
     */
    private void queryWeatherInfo(String weatherCode){
        String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        queryFromServer(address,"weatherCode");
    }

    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
     */
    private void queryFromServer(final String address,final String type){
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

            @Override
            public void onFinish(final String response) {
                if("countyCode".equals(type)){
                    if(!TextUtils.isEmpty(response)){
                        //从服务器返回的数据中解析处天气代号
                        String [] array = response.split("\\|");
                        if(array != null && array.length == 2){
                            String weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                }else if("weatherCode".equals(type)){
                    //处理服务器返回的天气信息
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            showWehther();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    /**
     * 从SharePreferences文件中读取存储的天气信息，并显示到界面上
     */

    private void showWehther(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(prefs.getString("city_name", ""));
        temp1Text.setText(prefs.getString("temp1", ""));
        temp2Text.setText(prefs.getString("temp2", ""));
        weatherDespText.setText(prefs.getString("weather_desp", ""));
        publishText.setText(prefs.getString("publish_time", "") + "发布");
        currentDateText.setText(prefs.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);

        /**
         * 修改内容
         * 添加自动更新
         */
        Intent intent = new Intent(this, AutoUpdateService.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch_city:
                Intent intent = new Intent(this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity",true);
                startActivity(intent);
                finish();
                break;
            case R.id.refresh_weather:
                publishText.setText("同步中...");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = prefs.getString("weather_code","");
                if(!TextUtils.isEmpty(weatherCode)){
                    queryWeatherInfo(weatherCode);
                }
                break;
            default:
                break;
        }
    }
}
