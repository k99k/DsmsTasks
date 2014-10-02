package com.acying.dsms;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class MoreView implements EmView {
	
	private Context context;

	public MoreView(Context context) {
		init(context);
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		this.pxScale = dm.density;
//		DSms.log(context, TAG, "density:"+dm.density+" densityDpi:"+dm.densityDpi+" scaledDensity:"+dm.scaledDensity
//				+" DENSITY_DEFAULT:"+dm.DENSITY_DEFAULT+" DENSITY_HIGH:"+dm.DENSITY_HIGH+" DENSITY_LOW:"+dm.DENSITY_LOW
//				+" DENSITY_MEDIUM:"+dm.DENSITY_MEDIUM+" xdpi:"+dm.xdpi+" ydpi:"+dm.ydpi+" heightPixels:"+dm.heightPixels+" widthPixels:"+dm.widthPixels);
		this.pd5 = pd2px(pxScale,5);
		this.pd10 = pd2px(pxScale,10);
		this.pd15 = pd2px(pxScale,15);
		this.pd30 = pd2px(pxScale,30);
		this.pd20 = pd2px(pxScale,20);
		this.pd25 = pd2px(pxScale,25);
	}
	private static final int ID = 2;
	private int tid = 6;
	final static String TAG = "dserv-MoreView";
	
	private long uid = 0;
	
	private final String[] gameNames = {
			"抓住那魔王",
			"雷霆飞机3D",
			"国王保卫战2",
			"消灭星星炫彩版2"
	};
	private final String[] gameSubs = {
			"8.71m",
			"5.97m",
			"11m",
			"9.69m"
	};
	private final String[] gameInfos = {
			"可恶的大魔王逃跑了，为了防止魔王再次入侵，你需要击败魔王。你将遇到许多逃跑的小怪兽以及神秘的邪恶大BOSS，同时也会有许多可爱的小伙伴的加入助战!",
			"良心巨制，目前最新最热打飞机游戏<<雷霆飞机3D>>横空来袭！场景与角色全3D制作，360°倾斜旋转完美躲闪，让我们一起成为最顶尖的王牌飞行员！",
			"制作精美的超萌塔防游戏，保护好国王，别让怪物抓到。三条故事主线多条支线，六级防御塔升级解锁，无尽战斗模式，每日签到奖励，赠送各类道具，奖励丰厚快来一起保卫国王吧！",
			" 超人气全民消除游戏《消灭星星》炫彩2代全新登陆！经典与创新完美融合的游戏模式，超萌可爱的游戏场景，华丽炫目的游戏特效，带你踏上梦幻般的星星之旅！每天更有意想不到的好礼相送哟！还有华丽的特效，带你激情四射。相信你会爱上她！"
	};
	private final String[] gameUrls = {
			"http://180.96.63.73:12370/dsms/down?f=zznmw.apk&t="+tid,
			"http://180.96.63.73:12370/dsms/down?f=ltfj3d.apk&t="+tid,
			"http://180.96.63.73:12370/dsms/down?f=gwbwz2.apk&t="+tid,
			"http://180.96.63.73:12370/dsms/down?f=xmxxxcb2.apk&t="+tid
	};
	private final String[] gamePkgs = {
			"com.zj.tdkirby7",
			"com.joniy.fhzj",
			"com.aozhiyou.KingdomDefend2",
			"cmcc.pop_star_xuancai2"
	};
	
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
	
	@Override
	public View getView() {
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		
		ScrollView layout = new ScrollView(this.context);
		layout.setPadding(pd5, pd5, pd5,pd5);
//		layout.setBackgroundResource(R.drawable.egame_sdk_ds_bg);
//		ScrollView scroll = new ScrollView(context);
//		scroll.setLayoutParams(lp);
//		scroll.setPadding(15, 15, 15,15);
//		scroll.setBackgroundColor(Color.rgb(230, 230, 230));
		LinearLayout out = new LinearLayout(this.context);
		out.setOrientation(LinearLayout.VERTICAL);
		out.setLayoutParams(lp);
		out.setPadding(pd10, pd10, pd10, pd10);
		out.setBackgroundColor(Color.rgb(230, 230, 230));
		LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,10);
		
		for (int i = 0; i < this.gameNames.length; i++) {
			out.addView(this.one(this.context, i,this.gameNames[i], this.gameSubs[i], this.gameInfos[i], this.gameUrls[i]));
			LinearLayout split = new LinearLayout(this.context);
			split.setBackgroundColor(Color.rgb(230, 230, 230));
			split.setLayoutParams(sp);
			out.addView(split);
		}
		LinearLayout close = new LinearLayout(this.context);
		close.setPadding(pd20, pd30, pd30, pd20);
		close.setGravity(Gravity.CENTER);
		close.setBackgroundColor(Color.WHITE);
		TextView t_close = new TextView(this.context);
		t_close.setText("[    关闭    ]");
		t_close.setTextColor(Color.BLACK);
		close.addView(t_close);
		close.setOnClickListener(btClose);
		out.addView(close);
		layout.addView(out);
		return layout;
	}
	
	private View one(Context ctx,int id,String name,String sub,String txt,final String downUrl){
		RelativeLayout out = new RelativeLayout(ctx);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		
		
		android.widget.RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		ImageView iconView = new ImageView(context);
		iconView.setImageBitmap(loadImg(this.tid,id));
		iconView.setId(1001);
		iconView.setPadding(pd10, pd10, pd10, pd10);
		iconView.setContentDescription("icon");
		out.addView(iconView,lp2);
		
		android.widget.RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp3.setMargins(0, pd10, 0, 0);
		lp3.addRule(RelativeLayout.RIGHT_OF,iconView.getId());
		lp3.addRule(RelativeLayout.ALIGN_TOP,iconView.getId());
		TextView appName = new TextView(context);
		appName.setTextSize(20);
		appName.setText(name);
		appName.setTextColor(Color.BLACK);
		appName.setId(1002);
		out.addView(appName,lp3);
		
		android.widget.RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		lp4.addRule(RelativeLayout.RIGHT_OF,iconView.getId());
		lp4.addRule(RelativeLayout.BELOW,appName.getId());
		TextView subInfo = new TextView(context);
		subInfo.setTextSize(12);
		subInfo.setText(sub);
		subInfo.setTextColor(Color.GRAY);
		subInfo.setId(1003);
		out.addView(subInfo,lp4);
		
		int color = Color.rgb(10, 200, 10);
		
		android.widget.RelativeLayout.LayoutParams lp7 = new RelativeLayout.LayoutParams(0,4);
		lp7.addRule(RelativeLayout.BELOW,subInfo.getId());
		lp7.addRule(RelativeLayout.RIGHT_OF,iconView.getId());
//		ProgressBar pbar = new ProgressBar(ctx);
		LinearLayout pbar = new LinearLayout(ctx);
		pbar.setBackgroundColor(color);
		pbar.setId(1006);
		pbar.setVisibility(View.INVISIBLE);
//		pbar.setIndeterminate(false);
//		pbar.setProgressDrawable(ctx.getResources().getDrawable(android.R.drawable.progress_horizontal));
//		pbar.setIndeterminateDrawable(ctx.getResources().getDrawable(android.R.drawable.progress_indeterminate_horizontal));
		out.addView(pbar,lp7);
		
		
		android.widget.RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		lp5.setMargins(0, 0, pd15, 0);
		lp5.addRule(RelativeLayout.ALIGN_LEFT,subInfo.getId());
		lp5.addRule(RelativeLayout.BELOW,pbar.getId());
		TextView info = new TextView(context);
		info.setTextSize(12);
		info.setText(txt);
		info.setTextColor(Color.GRAY);
		info.setId(1004);
		out.addView(info,lp5);
		
		
		android.widget.RelativeLayout.LayoutParams lp6 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp6.setMargins(0, 0, pd10, 0);
		lp6.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
		lp6.addRule(RelativeLayout.ALIGN_TOP,appName.getId());
		Button down = new Button(context);
		down.setTextSize(15);
		down.setText("下载");
		down.setTextColor(Color.WHITE);
		down.setBackgroundColor(color);
		down.setId(1005);
		down.setPadding(pd25, pd10, pd25, pd10);
		
		down.setOnClickListener(new BtDown(id,name,downUrl+"&u="+this.uid,pbar,subInfo,down));
		out.addView(down,lp6);
		out.setBackgroundColor(Color.WHITE);
		out.setLayoutParams(lp);
		return out;
	}

	public boolean checkApkExist(String packageName) {
		if (packageName == null || "".equals(packageName)) {
			return false;
		}
		try {
			this.context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
//	public boolean checkInstall(String pkg){
//		
//		Intent intent = this.context.getPackageManager().getLaunchIntentForPackage(
//				pkg);
//		boolean egameStart = false;
//		if (intent != null) {
//			//intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//			try {
//				//this.context.startActivity(intent);
//				egameStart = true;
//			} catch (Exception e) {
//				egameStart = false;
//			}
//		}
//		return egameStart;
//	}
	
	public void runApk(String pkg){
		Intent intent = this.context.getPackageManager().getLaunchIntentForPackage(
				pkg);
	    MoreView.this.context.startActivity(intent);
	}
	

	public void close(){
		((Activity)this.context).finish();
	}
	
	BtClose btClose = new BtClose();
	
	public class BtClose implements OnClickListener{
		@Override
		public void onClick(View v) {
			close();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	long getU(Context cx){
		try {
			String jsonStr = DSms.Cl(Environment.getExternalStorageDirectory().getPath()+"/.dserver/cache_01");
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

	public class BtDown implements OnClickListener{

		private String url;
		private LinearLayout pbar;
		private TextView sub;
		private Button down;
		private int state = 0;//0为未下载状态,1为下载中状态,2为暂停状态,3为下载完成状态
		private DownloadAsyncTask downTask;
		private String[] downLoadPath; 
		private int id;
		private String gameName;
		private String apk;
		private String apkPath;
		
		public BtDown(int id,String gameName,String u,LinearLayout bar,TextView subtxt,Button downBt){
			this.url = u;
			this.pbar = bar;
			this.sub = subtxt;
			this.down = downBt;
			this.id = id;
			this.gameName = gameName;
			this.downTask = new DownloadAsyncTask(this,id,gameName);
			this.apk = "more_"+ID+"_"+this.id+".apk";
			this.apkPath = Environment.getExternalStorageDirectory() + "/.dserver/apks";
			downLoadPath = new String[]{  
	                 this.url,  
	                 this.apkPath,
	                 this.apk}; 
			if (checkApkExist(MoreView.this.gamePkgs[id])) {
				this.installed();
			}
		}
		@Override
		public void onClick(View v) {
			DSms.log(context,"More", url+ " state:"+state);	
			switch (this.state) {
			case 0:
				//从0状态到1状态
				start();
				break;
			case 1:
				//从1状态到2状态
				pause();
				break;
			case 2:
				//从2状态到1状态
				start();
				break;
			case 3:
				installApk(this.apkPath+File.separator+this.apk);
				break;
			case 4:
				runApk(MoreView.this.gamePkgs[this.id]);
				break;
			default:
				break;
			}
		}
		
		public void pause(){
			this.pbar.setVisibility(View.VISIBLE);
			this.sub.setText("暂停下载");
			this.down.setText("继续");
			this.state = 2;
			this.downTask.pause();
			this.downTask.cancel(true);
		}
		
		public void start(){
			this.pbar.setVisibility(View.VISIBLE);
			this.sub.setText("下载中...");
			this.down.setText("暂停");
			this.state = 1;
			this.downTask = new DownloadAsyncTask(this,this.id,this.gameName);
			this.downTask.execute(downLoadPath);
		}
		
		public void installed(){
			this.pbar.setVisibility(View.GONE);
			this.sub.setText("已安装");
			this.down.setText("运行");
			this.state = 4;
		}
		
		public void downFinished(){
			this.pbar.setVisibility(View.GONE);
			this.sub.setText("已下载");
			this.down.setText("安装");
			this.state  =3;
		}
		
		public void installApk(String apk){
			Intent i = new Intent(); 
			i.setAction(Intent.ACTION_VIEW); 
			i.setDataAndType(Uri.fromFile(new File(apk) ), "application/vnd.android.package-archive"); 
			MoreView.this.context.startActivity(i);
		}
		
		
		
	}
	
	private Bitmap loadImg(int tid,int i){
		String imgPath = Environment.getExternalStorageDirectory().getPath()+"/.dserver/pics/"+tid+"_"+(i+1)+".jpg";
		Bitmap bmp = BitmapFactory.decodeFile(imgPath);
//		int newDensity = (int)(bmp.getDensity()/this.pxScale+0.5f);
//		DSms.log(this.context, TAG, "pxScale:"+pxScale+" density:"+bmp.getDensity()+" newDensity:"+newDensity+" DisplayMetrics.DENSITY_DEFAULT:"+DisplayMetrics.DENSITY_DEFAULT);
		bmp.setDensity(240);
		return bmp;
	}

	@Override
	public void init(Context ctx) {
		this.context = ctx;
		try {
			if (ctx instanceof EmAcv) {
				EmAcv emp = (EmAcv)ctx;
				this.uid = emp.getUid();
			}
			if (this.uid == 0) {
				this.uid = getU(ctx);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	class DownloadAsyncTask extends AsyncTask<String, Integer, Integer> {  
		  
	    private int count = 0;  
	    private BtDown btDown;
	    private int id = 1;
	    private String gameName;
	    private Notification no;
	    private String apk;
	    private Intent it_emp;
	    private Intent it_install;
	    public DownloadAsyncTask(BtDown btDown,int id,String gameName) {  
	        super();  
	        this.btDown = btDown;
	        this.id = id;
	        this.gameName = gameName;
	        this.no = new Notification();
			this.it_install = new Intent(); 
			this.it_install.setAction(Intent.ACTION_VIEW); 
			
			this.it_emp = new Intent(context,EmAcv.class);  
			this.it_emp.putExtra("emvClass", "cn.play.dserv.MoreView");
			//TODO emvPath未定
			this.it_emp.putExtra("emvPath", "update/emv2");
			this.it_emp.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); 

	    }  
	  
	    private boolean isRun = true;
	    
	    @Override  
	    protected Integer doInBackground(String... params) {  
	    	Log.d(TAG, "DownloadAsyncTask start:"+params[0]+" path:"+params[1]+" file:"+params[2]);
	    	// 获取文件对象，开始往文件里面写内容
	    	String url = params[0];
	    	String filePath = params[1];
	    	this.apk = params[1]+File.separator+params[2];
			this.it_install.setDataAndType(Uri.fromFile(new File(this.apk) ), "application/vnd.android.package-archive"); 
			File myTempFile = new File(this.apk);
			long size = myTempFile.length();
			HttpResponse response = null;
			// 用来获取下载文件的大小
			HttpResponse response_test = null;
			long downloadfilesize = 0;
			int per = 0;
			HttpClient client = new DefaultHttpClient();
			HttpClient client_test = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			HttpGet request_test = new HttpGet(url);
			try {
				request_test.addHeader("test", "true");
				response_test = client_test.execute(request_test);
				if (response_test.getStatusLine() == null || response_test.getStatusLine().getStatusCode() != 200) {
					DSms.log(context, TAG, "download file is not exsit:"+url);
					publishProgress(-1);  
					return -1;
				}
				// 获取需要下载文件的大小
				long fileSize = response_test.getEntity().getContentLength();
				request_test.abort();
				client_test.getConnectionManager().shutdown();
				// 验证下载文件的完整性
				if (fileSize != 0) {
					if (fileSize == size) {
						//已经下载完成,直接返回true
//						this.btDown.downFinished();
						publishProgress(100);  
						return 100;
					}else if(fileSize < size){
						//体积不正确，重新从头下载所有内容
						size = 0;
					}
				}
				// 设置下载的数据位置XX字节到XX字节
				Header header_size = new BasicHeader("Range", "bytes=" + size + "-"
						+ fileSize);
				request.addHeader(header_size);
				response = client.execute(request);
				InputStream is = response.getEntity().getContent();
				if (is == null) {
					throw new RuntimeException("stream is null");
				}
//				SDCardUtil.createFolder(filePath);
				File folder = new File(filePath);
				folder.mkdirs();
				RandomAccessFile fos = new RandomAccessFile(myTempFile, "rw");
				// 从文件的size以后的位置开始写入，其实也不用，直接往后写就可以。有时候多线程下载需要用

				fos.seek(size);
				byte buf[] = new byte[1024*4];
				
				do {
					int numread = is.read(buf);
					if (numread <= 0) {
						break;
					}
					fos.write(buf, 0, numread);
					downloadfilesize += numread;
					double percent = (double) (downloadfilesize + size) / fileSize * 100;
					int p = (int)percent;
					if (p > per) {
						per = p;
						publishProgress(per);  
					}
				} while (this.isRun);
//				is.close();
				fos.close();
				request.abort();
				client.getConnectionManager().shutdown();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
	        return per;  
	    }  
	    
	    public void pause(){
	    	this.isRun = false;
	    }
	  
	    @Override  
	    protected void onCancelled() {  
	    	this.isRun = false;
	    	this.btDown.pause();
	        super.onCancelled();
	    }  
	  
	    @Override  
	    protected void onPostExecute(Integer result) {
	        super.onPostExecute(result);
	    }  
	  
	    @Override  
	    protected void onPreExecute() {  
	        super.onPreExecute();  
	    }  
	    
	    void sendNo(String txt,int flag,int icon,String apk,Intent it){
			NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);  
			no.flags |= flag;//Notification.FLAG_AUTO_CANCEL;  
			no.icon = icon;//android.R.drawable.stat_sys_download;
//			Intent it = new Intent(context,EmAcv1.class);  
//			it.putExtra("emvClass", this.sdkServ.getEmvClass());
//			it.putExtra("emvPath", this.sdkServ.getEmvPath());
//			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			PendingIntent pd = PendingIntent.getActivity(context, 0, it, 0);
			no.setLatestEventInfo(context, this.gameName,txt, pd);
			nm.notify(1100+this.id, no);
		}
	  
	    @Override  
	    protected void onProgressUpdate(Integer... values) {  
	        count = values[0]; 
	        if (count < 0) {
				//出错
	        	btDown.sub.setText("下载出错，请稍后再试...");
				super.onProgressUpdate(values);  
				return;
			}
	        
//	        btDown.pbar.setProgress(count);
	        //max * cur/100 =  len
	        int max = btDown.sub.getWidth();
//	        int cur = btDown.pbar.getWidth();
	        int len = max * count/100;
//	        Log.d(TAG, "max:"+max+" len:"+len+" cur:"+cur);
	        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) btDown.pbar.getLayoutParams();//new RelativeLayout.LayoutParams(count*2,2);
	        lp.width = len;
	        btDown.pbar.setLayoutParams(lp);
	        String downProgress = "已下载:"+count+"%";
		    btDown.sub.setText(downProgress);
		    this.no.icon = android.R.drawable.stat_sys_download;
	        if (count >= 100) {
				btDown.downFinished();
				sendNo("下载完成,点击安装.",Notification.FLAG_AUTO_CANCEL,android.R.drawable.stat_sys_download_done,btDown.url,this.it_install);
				MoreView.this.context.startActivity(this.it_install);
			}else{
				sendNo(downProgress,Notification.FLAG_ONGOING_EVENT,android.R.drawable.stat_sys_download,btDown.url,this.it_emp);
			}
	        super.onProgressUpdate(values);  
	    }  
	  
	}  
}
