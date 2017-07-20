package com.framework.net;



public interface OnGetBinListener {

	void onGetBinError(String msg);

	void onGet(int percent);

	void onGetFinish(String bt);

}
