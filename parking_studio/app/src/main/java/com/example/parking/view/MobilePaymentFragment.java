package com.example.parking.view;

import java.util.Hashtable;

import com.example.parking.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MobilePaymentFragment extends Fragment {
	    private static final int PAYMENT_TYPE_ALIPAY=202;
	    private static final int PAYMENT_TYPE_WECHATPAY=203;
	    private View mView;
	    private View mFragmentView;
	    private ImageView mPaymentIV;
 	    private String mCodeUrl;
 		private int mPayType;
 		private String mParkNumber;
 		private String mCarType;
 		private String mParkType;
 		private String mStartTime;
 		private String mLeaveTime;
 		private String mPaidMoney;
 		private String mFeeScale;
 		private String mParkingRecordID;
 		private String mTradeRecordID;
 		private String mLicensePlateNumber;
	    public static String LOG_TAG = "MobilePaymentFragment";
 		
 	    public MobilePaymentFragment(int payType,String codeUrl){
 	    	mPayType=payType;
 	    	mCodeUrl = codeUrl;
	    }

	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	    }

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    	mView = inflater.inflate(R.layout.fragment_mobile_payment, container, false);
	    	Intent intent = getActivity().getIntent();
	        mPayType = intent.getExtras().getInt("paytype");
			mParkNumber =  intent.getExtras().getString("parkNumber");
			mLicensePlateNumber =  intent.getExtras().getString("licensePlateNumber");
			mParkingRecordID =  intent.getExtras().getString("parkingRecordID");
			mTradeRecordID =   intent.getExtras().getString("tradeRecordID");
			mCarType =   intent.getExtras().getString("carType");
			mParkType =   intent.getExtras().getString("parkType");
			mStartTime =   intent.getExtras().getString("startTime");
			mLeaveTime =   intent.getExtras().getString("leaveTime");
			mPaidMoney =   intent.getExtras().getString("paidMoney");
			mParkType =   intent.getExtras().getString("parkType");
	    	mFragmentView = (View)mView.findViewById(R.id.fm_mobile_payment);
	    	mPaymentIV=(ImageView)mView.findViewById(R.id.iv_two_dimensions_code);
            if(mCodeUrl!=null){
                if(mPayType==PAYMENT_TYPE_ALIPAY){
                    mFragmentView.setBackgroundResource(R.color.blue);
                    mPaymentIV.setBackgroundResource(R.drawable.ic_alipay_two_dimensions_code);
                    Drawable drawable =new BitmapDrawable(createQRCodeBitmap(mCodeUrl));
                    mPaymentIV.setBackgroundDrawable(drawable);
					mPaymentIV.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							Log.e(LOG_TAG, "clientQueryResult-> mPaymentIV is onclick");
							((MobilePaymentActivity)getActivity()).queryResult();
						}
					});
                }else if(mPayType==PAYMENT_TYPE_WECHATPAY){
                    mFragmentView.setBackgroundResource(R.color.green);
                    Drawable drawable =new BitmapDrawable(createQRCodeBitmap(mCodeUrl));
                    mPaymentIV.setBackgroundDrawable(drawable);
                }
            }
	        return mView;
	    }

	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	    }

	    @Override
	    public void onStart() {
	        super.onStart();
	    }

	    @Override
	    public void onResume() {
	        super.onResume();
	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	    }

	    @Override
	    public void onStop() {
	        super.onStop();
	    }

	    @Override
	    public void onDestroyView() {
	        super.onDestroyView();
	    }

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	    }

	    @Override
	    public void onDetach() {
	        super.onDetach();
	    }

	    private Bitmap createQRCodeBitmap(String codeUrl) {  
	        // 用于设置QR二维码参数  
	        Hashtable<EncodeHintType, Object> qrParam = new Hashtable<EncodeHintType, Object>();  
	        // 设置QR二维码的纠错级(这里选择最高H级别)  
	        qrParam.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
	        // 设置编码方式  
	        qrParam.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
	      
	        // 设定二维码里面的内容
	        String content = codeUrl;  
	      
	        // 生成QR二维码数据
	        // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数  
	        try {  
	            BitMatrix bitMatrix = new MultiFormatWriter().encode(content,  
	                    BarcodeFormat.QR_CODE, 300, 300, qrParam);  
	      
	            // 开始利用二维码数据创建Bitmap图片，分别设为黑白两色  
	            int w = bitMatrix.getWidth();  
	            int h = bitMatrix.getHeight();  
	            int[] data = new int[w * h];  
	      
	            for (int y = 0; y < h; y++) {  
	                for (int x = 0; x < w; x++) {  
	                    if (bitMatrix.get(x, y))  
	                        data[y * w + x] = 0xff000000;// 黑色  
	                    else  
	                        data[y * w + x] = -1;// -1 相当于0xffffffff 白色  
	                }  
	            }  
	      
	            // 创建一张bitmap图片，采用最高的效果显示  
	            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);  
	            // 将上面的二维码颜色数组传入，生成图片颜色  
	            bitmap.setPixels(data, 0, w, 0, 0, w, h);  
	            return bitmap;  
	        } catch (WriterException e) {  
	            e.printStackTrace();  
	        }  
	        return null;  
	    }  
}
