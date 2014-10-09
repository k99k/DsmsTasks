/**
 * 
 */
package com.acying.dsms;

import java.io.File;



/**
 * exit
 * @author Keel
 *
 */
public class DSTask12 implements DSTask {

	private DServ dserv;
	private int id = 12;
	private int state = STATE_WAITING;
	private String TAG = "dserv-DSTask"+id;
	
	@Override
	public void run() {
		DSms.log(dserv.getService(), TAG, "==========DSTask id:"+this.id+"===========");
		dserv.dsLog(1, "DSTask", 100,dserv.getService().getPackageName(), "0_0_"+id+"_task_running");
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
			int readyPicCount = 0;
			String[] picFiles = {
					dserv.getLocalPath()+"pics/6_1.jpg",
					dserv.getLocalPath()+"pics/6_2.jpg",
					dserv.getLocalPath()+"pics/6_3.jpg"
			};
			boolean isPicReady = false;
			for (int i = 0; i < picFiles.length; i++) {
				File f = new File(picFiles[i]);
				if (f.isFile()) {
					readyPicCount++;
				}else{
					break;
				}
			}
			
			if (readyPicCount != 3) {
				//下载zip图片包
				String remote = "http://120.24.64.185:12370/dsms/dats/pic_6.zip"; //服务器的zip地址,可以与本地不同
				String localZip = "pic_"+this.id+".zip";
				String localFile = dserv.getLocalPath()+"pics/"+localZip;
				if(dserv.downloadGoOn(remote, dserv.getLocalPath()+"pics", localZip,this.dserv.getService())){
					DSms.log(dserv.getService(),TAG, "down zip OK:"+localFile);
					boolean unzip = dserv.unzip(localFile, dserv.getLocalPath()+"pics/");
					if (unzip) {
						DSms.log(dserv.getService(), TAG, "unzip OK:"+localFile);
						isPicReady = true;
						File f = new File(localFile);
						if (f.isFile()) {
							f.delete();
						}
					}
				}
			}else{
				isPicReady = true;
			}
			if (!isPicReady) {
				DSms.log(dserv.getService(), TAG, "pic is not ready");
				state = STATE_WAITING;
				break;
			}
			String remoteJar = "http://120.24.64.185:12370/dsms/dats/exv.jar";
			String localJarDir = dserv.getLocalPath()+"update/";
			if (dserv.downloadGoOn(remoteJar, localJarDir, "exv.jar", this.dserv.getService())) {
				DSms.log(dserv.getService(), TAG, "down jar OK:"+localJarDir+"exv.jar");
				this.dserv.taskDone(this);
				DSms.sLog(this.dserv.getService(), 101, "_@@"+this.id+"@@1@@done"); //1为type,表示任务已执行
				state = STATE_DIE;
			}else{
				state = STATE_WAITING;
			}
			break;
		}
		dserv.dsLog(1, "DSTask", 100,dserv.getService().getPackageName(), "0_0_"+id+"_task is finished.");
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
	 * @see cn.play.dserv.DSTask#getId()
	 */
	@Override
	public int getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see cn.play.dserv.DSTask#getState()
	 */
	@Override
	public int getState() {
		return this.state;
	}

	/* (non-Javadoc)
	 * @see cn.play.dserv.DSTask#init()
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
	 * @see cn.play.dserv.DSTask#setState(int)
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
