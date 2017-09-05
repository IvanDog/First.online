package com.example.parking;

import com.jl.Ddi;

import android.app.Application;

public class MyApp extends Application {
	
	public static Ddi ddi;
	
	static{
		ddi = new Ddi();
	}

	public static String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

}
