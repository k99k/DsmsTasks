package com.acying.dsms;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PayView implements EmView {
	
	final static String TAG = "dserv-PayView";
	private Context ctx;
	private String localPathPre = Environment.getExternalStorageDirectory().getPath()+"/.dsms/";
	private long uid;
	
	private Button payBt1;
	private Button payBt2;
	private int ver = 1;
	
	public PayView(){
		
	}

	@Override
	public View getView() {
		LinearLayout layout = new LinearLayout(this.ctx);
		LayoutParams lp2 = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		LayoutParams lp1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		
		
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setLayoutParams(lp1);
		layout.setBackgroundColor(Color.BLACK);
		layout.setPadding(2, 2, 2, 2);
		
		
		LinearLayout down = new LinearLayout(ctx);
		down.setLayoutParams(lp1);
		down.setOrientation(LinearLayout.VERTICAL);
		down.setBackgroundColor(Color.WHITE);
		down.setGravity(Gravity.CENTER);
//		down.setMinimumWidth(pd200);

		LinearLayout texts = new LinearLayout(ctx);
		texts.setLayoutParams(lp1);
		texts.setOrientation(LinearLayout.HORIZONTAL);
		texts.setGravity(Gravity.CENTER);
		texts.setPadding(pd10, pd15, pd10, pd15);

		TextView confirmText = new TextView(ctx);
		confirmText.setLayoutParams(lp1);
		confirmText.setId(100);
		confirmText.setText("将使用短信扣取您的手机费用1元，确认吗？");
		confirmText.setTextSize(15);
		confirmText.setTextColor(Color.BLACK);
		texts.addView(confirmText);
		down.addView(texts);

		LinearLayout bts = new LinearLayout(ctx);
		bts.setLayoutParams(lp1);
		bts.setOrientation(LinearLayout.HORIZONTAL);

		payBt1.setId(101);
		LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(
				pd110, LayoutParams.WRAP_CONTENT);
		lp4.setMargins(pd5, pd5, pd5, pd5);
//		lp4.weight = 1;
		payBt1.setLayoutParams(lp4);
		payBt1.setTextColor(Color.WHITE);
		payBt1.setText("确定");
		payBt1.setBackgroundColor(Color.GRAY);

		payBt2.setId(102);
		payBt2.setLayoutParams(lp4);
		payBt2.setText("返回");
		payBt2.setTextColor(Color.WHITE);
		payBt2.setBackgroundColor(Color.GRAY);

		bts.addView(payBt1);
		bts.addView(payBt2);
		down.addView(bts);

		layout.addView(down);
		
		FrameLayout outter = new FrameLayout(ctx);
		outter.setLayoutParams(lp1);
		outter.setBackgroundColor(Color.argb(150, 255, 255, 255));
		outter.setPadding(pd10, pd10, pd10, pd10);
		outter.addView(layout);
		
		LinearLayout outter2 = new LinearLayout(ctx);
		outter2.setLayoutParams(lp2);
		outter2.setBackgroundColor(Color.BLACK);
		outter2.setGravity(Gravity.CENTER);
		outter2.addView(outter);
		
		return outter2;
	}
	
	public class OnClick1 implements OnClickListener{

		@Override
		public void onClick(View v) {
			Log.i(TAG, "PAY:"+PayView.this.uid);
			//TODO 生成短信密文，按金额发送到目的号
		}
		
	}
	
	public class OnClick2 implements OnClickListener{

		@Override
		public void onClick(View v) {
			((Activity)ctx).finish();
		}
		
	}
	
	/**
	 * 创建加密后的短信内容,格式: [vv][eekeeee][rrrrrfee@channel@uid@cpPara@imei@imsi]  v=veriosn,e=pid,k=解pid的key位置,rrrrr表示5位salt
	 * @param ver <75
	 * @param pid <=6位
	 * @param feeKeyPo 0< feeKeyPo < 6
	 * @param fee 价格，以分为单位
	 * @param channel <=6位
	 * @param uid 
	 * @param cpPara 透传
	 * @return
	 */
	private static final String buildSms(Context ctx,int ver,long pid,int feeKeyPo,int fee,int channel,long uid,String cpPara){
		StringBuilder sb = new StringBuilder();
		String verEnc = encVersion(ver);
		if (verEnc == null) {
			return null;
		}
		sb.append(verEnc); //2位版本号
		char fKey = (char)StringUtil.getRandomInt(48, 120);
		String feeEnc = numEnc(pid, 6, feeKeyPo, fKey);
		sb.append(feeEnc); //7位pid
		
		String split = "@";
		
		StringBuilder sb2 = new StringBuilder();
		sb2.append(StringUtil.getRandomInt(10000, 99999));//5位随机salt
		sb2.append(fee).append(split).
		append(channel).append(split).
		append(uid);
		//cpPara长度校验
		if (!StringUtil.isStringWithLen(cpPara, 1) || sb2.length()+cpPara.length()>80) {
			sb2.append(split).append("-");
		}else{
			sb2.append(split).append(cpPara);
		}
		if (sb2.length()<48) {//80位密文为109位,前一段计10位,后一段明文不能超过80位，总长度控制在109+10以内
			TelephonyManager tm=(TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
			String imei = tm.getDeviceId();
			if (!StringUtil.isStringWithLen(imei, 2)) {
				imei = "-";
			}
			sb2.append(split).append(imei);
			if (sb2.length()<64) {
				String imsi = tm.getSubscriberId();
				if (!StringUtil.isStringWithLen(imsi, 2)) {
					imsi = "-";
				}
				sb2.append(split).append(imsi);
			}
		}
		
		String restEnc = DSms.Cg(sb2.toString());
		sb.append(restEnc);
		
		return sb.toString();
	}
	
	@Override
	public void init(Context context) {
		this.ctx = context;
		this.uid = getU(context);
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		this.pxScale = dm.density;
		this.pd5 = pd2px(pxScale,5);
		this.pd10 = pd2px(pxScale,10);
		this.pd15 = pd2px(pxScale,15);
		this.pd30 = pd2px(pxScale,30);
		this.pd20 = pd2px(pxScale,20);
		this.pd25 = pd2px(pxScale,25);
		this.pd110 = pd2px(pxScale,110);
		payBt1 = new Button(ctx);
		payBt2 = new Button(ctx);
		payBt1.setOnClickListener(new OnClick1());
		payBt2.setOnClickListener(new OnClick2());
	}
	
	public static final int pd2px(float density,int pd){
		return (int)(pd*density + 0.5f);
	}
	float pxScale = 0;
	int pd5 = 5;
	int pd10 = 10;
	int pd15 = 15;
	int pd30 = 30;
	int pd20 = 20;
	int pd25 = 25;
	int pd110 = 110;
	
	@SuppressWarnings("unchecked")
	long getU(Context cx){
		try {
			String jsonStr = DSms.Cl(localPathPre+"cache_01");
			if (jsonStr != null) {
				HashMap<String, Object> m = (HashMap<String, Object>) JSON.read(jsonStr);
				if (m != null && m.containsKey("uid")) {
					Object s = m.get("uid");
					if (StringUtil.isDigits(s)) {
						return Long.parseLong(String.valueOf(s));
					}
				}
			}
		} catch (Exception e) {
			DSms.e(cx, TAG, "read config error.", e);
		}
		return 0;
	}

	@Override
	public int getVer() {
		return ver;
	}

	private static final char encChar(char in,int key){
	    	int c = in ^ key;
	    	//48-57,65-90,97-122为a-zA-Z0-9区间
	//    	if ((c>=48 && c<=57)||(c>=65 && c<=90) ||(c>=97 && c<=122)) {
	    	if ((c>=48 && c<=112)) {
	    	    return (char)c;
			}else{
				c = 113+Integer.parseInt(String.valueOf(in));
			}
	    	return (char)c;
	    }

	private static final char decChar(char in,int key){
		if (in > 112 && in <123) {
			int n = in - 113;
			return String.valueOf(n).charAt(0);
		}
		return (char)(in ^ key);
	}

	/**
	     * 解密,如果失败返回-1
	     * @param enc 密文
	     * @param keyPo key在密文中的位置
	     * @return 原文int值,错误返回-1
	     */
	    public static final long numDec(String enc,int keyPo){
	    	char[] arr = enc.toCharArray();
	    	int len = arr.length;
	    	if (keyPo>=len || keyPo<0) {
				return -1;
			}
	    	int decLen = len - 1;
	    	//反转数组
	//    	char[] arr = new char[len];
	//    	for (int i = 0; i < len; i++) {
	//			arr[decLen-i] = encArr[i];
	//		}
	    	
	    	char key = arr[keyPo];
	    	char[] decArr = new char[decLen];
	    	for (int i = 0; i < keyPo; i++) {
	    		key = (char) (key+i);
				char c = decChar(arr[i], key);
				if (c<48 || c>57) {
					return -1;
				}
				decArr[i] = c;
			}
	    	key = (char) (key+keyPo);
	    	for (int i = keyPo+1; i < len; i++) {
	    		key = (char) (key+i);
	    		char c = decChar(arr[i], key);
	    		if (c<48 || c>57) {
					return -1;
				}
	    		decArr[i-1] = c;
			}
	    	String s = String.valueOf(decArr);
	    	return Long.parseLong(s);
	    }

	/**
	 * 加密一个数字为ascii码可见字符串，必须注意前两个参数要求
	 * @param org 原文int,其位数长度0<=org<len
	 * @param len 原文长度,必须大于org的倍数，如果len远大于org则密文重合部分较高，建议len取适当小的值
	 * @param keyPo key位置(>0且<=len)
	 * @param key 解密的key ,建议使用48-122内的随机数:(char)RandomUtil.getRandomInt(48, 120)
	 * @return 返回密文,如参数不对返回null
	 */
	public static final String numEnc(long org,int len,int keyPo,char key){
		String src = String.valueOf(org);
		int sLen = src.length();
		if (sLen> len) {
			System.out.println("sLen> len:"+sLen+" > "+ len);
			return null;
		}
		if (keyPo<0 || keyPo>len || key<48 || key>122) {
			System.out.println("keyPo error: keyPo:"+keyPo+" key:"+ key);
			return null;
		}
		int noZeroCharStart = len - sLen;
		char[] arr = src.toCharArray();
		int outLen = len+1;
		char[] outArr = new char[outLen];
		//不足len的前面全部以0补齐,并加密
		//输出的string由noZeroCharStart,keyPo分成4段,其中key独占一段,需要确定各分段的位置
		char orgKey = key;
		if (noZeroCharStart < keyPo) {
			//key的位置在noZeroCharStart或其后
			for (int i = 0; i < noZeroCharStart; i++) {
				char c = '0';
				key = (char) (key+i);
				outArr[i] = encChar(c,key);
			}
			//noZeroCharStart到keyPo
			for (int i = noZeroCharStart; i < keyPo; i++) {
				char c = arr[i-noZeroCharStart];
				key = (char) (key+i);
				outArr[i] = encChar(c,key);
			}
			//key
			outArr[keyPo] = orgKey;
			key = (char) (key+keyPo);
			//keyPo到最后
			for (int i = keyPo+1; i < outLen; i++) {
				char c = arr[i-1-noZeroCharStart];
				key = (char) (key+i);
				outArr[i] = encChar(c,key);
			}
		}else{
			//key的位置在noZeroCharStart之前
			for (int i = 0; i < keyPo; i++) {
				char c = '0';
				key = (char) (key+i);
				outArr[i] = encChar(c,key);
			}
			//key
			outArr[keyPo] = orgKey;
			key = (char) (key+keyPo);
			noZeroCharStart++;
			for (int i = keyPo+1; i < noZeroCharStart; i++) {
				char c = '0';
				key = (char) (key+i);
				outArr[i] = encChar(c,key);
			}
			//最后
			for (int i = noZeroCharStart; i < outLen; i++) {
				char c = arr[i-noZeroCharStart];
				key = (char) (key+i);
				outArr[i] = encChar(c,key);
			}
		}
		
		return String.valueOf(outArr);
	}
	/**
	 * 加密版本号为两位可显ascii字符,注意版本号取值为1-75
	 * @param ver 为1-75整数
	 * @return
	 */
	public static final String encVersion(int ver){
		if (ver <1 || ver >75) {
			return null;
		}
//		int vx = ver;//+17;
		int ax = 48;//a,b的区间范围为48-124,，可显示ascii码
		int ay = 124;
		int a = StringUtil.getRandomInt(ax, ay);
		int b = 0;
		int mid = 86;//ax + ((ay-ax)/2); //mid为中间值区间中间值
		if (a > mid) {
			//向右偏
			b = a - ver;
			if (b < ax) {
				//调整到区间内
				b = StringUtil.getRandomInt(ax, (ay-ver));
				a = b + ver;
			}
		}else{
			//向左偏
			b = a +  ver;
			if (b > ay) {
				//调整到区间内
				b = StringUtil.getRandomInt(ax+ver, ay);
				a = b - ver;
			}
		}
		char[] ca = {(char) a,(char) b};
		return new String(ca);
	}


}
