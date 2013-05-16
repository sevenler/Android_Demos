package com.example.demo;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.os.Process;

interface SingleWorkerCallback {
	public void cancel(Thread thread);

	public void load(Thread thread);
	
	public void completed();
}

public class SingleThreadWorker {
	//工作线程
	private Thread mGetterThread;

	// 当前的加载序列 重新执行的时候++ 回调completed检查是否对应
	private int mCurrentSerial;

	// 加载过程中的回调
	private SingleWorkerCallback mCB;

	//工作任务队列
	private List<Runnable> tasks = Collections.synchronizedList(new LinkedList<Runnable>());

	//  主线程handler
	private GetterHandler mHandler;

	// 是否取消当前的加载进程
	private volatile boolean mCancel = true;

	// 是否位闲置状态
	private boolean mIdle = false;

	// True 表示线程工作已经完成
	private boolean mDone = false;

	private class ImageGetterRunnable implements Runnable {

		private Runnable completedCallback(final int requestSerial) {
			return new Runnable() {
				public void run() {
					if (requestSerial == mCurrentSerial) {
						mCB.completed();
					}
				}
			};
		}

		public void run() {
			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

			while (true) {
				synchronized (SingleThreadWorker.this) {
					while (mCancel || mDone) {
						if (mDone){
							stop();
							return;
						}
						mIdle = true;
						SingleThreadWorker.this.notify();
						try {
							SingleThreadWorker.this.wait();
						} catch (InterruptedException ex) {
						}
						mIdle = false;
					}
				}

				executeRequest();
			}
		}

		private void executeRequest() {
			Iterator<Runnable> it = tasks.iterator();
			Runnable work;
			while(it.hasNext()){
				work = it.next();
				tasks.remove(work);
				work.run();
			}
			
			mDone = true;
			mHandler.postGetterCallback(completedCallback(mCurrentSerial));
		}
	}

	public SingleThreadWorker(GetterHandler handler) {
		mHandler = handler;
		initWorkerThead();
	}
	
	private void initWorkerThead(){
		if(mGetterThread != null) return;
		mGetterThread = new Thread(new ImageGetterRunnable());
		mGetterThread.setName("ImageGettter");
		mGetterThread.start();
	}

	public synchronized void cancelCurrent() {
		mCancel = true;
		if(mCB != null) mCB.cancel(mGetterThread);
		tasks.clear();
	}

	private synchronized void cancelCurrentAndWait() {
		cancelCurrent();
		while (mIdle != true) {
			try {
				wait();
			} catch (InterruptedException ex) {
			}
		}
	}

	// 停止加载
	public void stop() {
		synchronized (this) {
			cancelCurrentAndWait();
			mDone = true;
			notify();
		}
		try {
			mGetterThread.join();
		} catch (InterruptedException ex) {
		}
		mGetterThread = null;
	}

	public synchronized void setNewWork(SingleWorkerCallback cb, Runnable runable) {
		initWorkerThead();
		// 取消之前的工作
		cancelCurrentAndWait();

		mCB = cb;
		mCurrentSerial += 1;

		tasks.add(runable);
		mCancel = false;
		mCB.load(mGetterThread);
		notify();
	}
}

class GetterHandler extends Handler {
	private static final int IMAGE_GETTER_CALLBACK = 1;

	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case IMAGE_GETTER_CALLBACK:
			((Runnable) message.obj).run();
			break;
		}
	}

	public void postGetterCallback(Runnable callback) {
		postDelayedGetterCallback(callback, 0);
	}

	public void postDelayedGetterCallback(Runnable callback, long delay) {
		if (callback == null) {
			throw new NullPointerException();
		}
		Message message = Message.obtain();
		message.what = IMAGE_GETTER_CALLBACK;
		message.obj = callback;
		sendMessageDelayed(message, delay);
	}

	public void removeAllGetterCallbacks() {
		removeMessages(IMAGE_GETTER_CALLBACK);
	}
}

