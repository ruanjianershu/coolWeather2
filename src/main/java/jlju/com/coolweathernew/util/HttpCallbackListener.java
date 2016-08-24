package jlju.com.coolweathernew.util;

public interface HttpCallbackListener {
	
	void onFinish(String response);
	
	void onError(Exception e);
}
