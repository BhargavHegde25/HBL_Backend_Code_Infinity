package com.kony.kmsinvoke.util;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueryClass {
	private QueryClass() {
	}

	private static BlockingQueue<Map<String, Object>> queries = null;

	public static synchronized BlockingQueue<Map<String, Object>> getQueries() {
		return queries;
	}

	public static void setQueries() {
		queries = new LinkedBlockingQueue<>();
	}

}
