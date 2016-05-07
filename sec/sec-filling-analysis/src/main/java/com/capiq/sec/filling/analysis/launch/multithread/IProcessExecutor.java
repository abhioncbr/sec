package com.capiq.sec.filling.analysis.launch.multithread;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import com.capiq.sec.filling.analysis.parse.stat.IParseStat;

public interface IProcessExecutor extends Callable<LinkedList<IParseStat>> {
	public void set(CountDownLatch batchCountDownLatch, String[][] form10KListData);
}
