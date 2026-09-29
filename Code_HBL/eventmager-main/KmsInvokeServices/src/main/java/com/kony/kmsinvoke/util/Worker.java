package com.kony.kmsinvoke.util;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Worker implements Runnable {
	@Override
	public void run() {
		BlockingQueue<Map<String, Object>> queries = QueryClass.getQueries();
		boolean flag = true;
		while (flag) {
			try {
				Map<String, Object> map = queries.poll(0, TimeUnit.MILLISECONDS);
				if (map != null) {
					HelperMethods.callInternalService(map, KmsInvokeConstants.EVENTDBDBSERVICE,
							HelperMethods.replaceSchemaName(KmsInvokeConstants.ALERTHISTORY_CREATE,
									StaticDataHolder.getSchemaname()),
							null);
				}
			} catch (Exception e) {
				flag = false;
			}

		}
	}

}
