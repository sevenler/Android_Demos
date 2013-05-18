package com.example.demo;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class SingleThreadWorker {
	
	public SingleThreadWorker() {
		super();
		worker.start();
	}

	private Thread worker = new Thread(new SingleThreadWorkerRunnable());
	
	// 是否位闲置状态
	private boolean mIdle = false;
	// 是否取消当前的加载进程
	private volatile boolean mCancel = false;
	
	private Queue<Runnable> tasks = new LinkedBlockingQueue<Runnable>();
	
	private class SingleThreadWorkerRunnable implements Runnable {
		@Override
		public void run() {
			while (true) {
				synchronized (SingleThreadWorker.this) {
					mIdle = true;
					SingleThreadWorker.this.notify();
					try {
						SingleThreadWorker.this.wait();
					} catch (InterruptedException ex) {
					}
					mIdle = false;
				}
				
				executeRequest();
			}
		}
		
		private void executeRequest(){
			Runnable work;
			while (!tasks.isEmpty()) {
				if(mCancel) break;
				work = tasks.poll();
				work.run();
			}
		}
	}
	
	private void cancelCurrent(){
		mCancel = true;
	}
	
	private void waiting(){
		while (mIdle != true) {
			try {
				notify();
				mIdle = true;
				wait();
			} catch (InterruptedException ex) {
			}
		}
	}
	
	public synchronized void setNewWork(Runnable work){
		cancelCurrent();
		waiting();
		
		tasks.add(work);
		
		mCancel = false;
		if(mIdle) {
			notify();
		}
	}

}
