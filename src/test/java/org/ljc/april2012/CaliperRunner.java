package org.ljc.april2012;

import com.google.caliper.Runner;

public class CaliperRunner {


	public static void main(String[] args) {
		String[] caliperArgs = new String[1];
		StringBuilder classArgs = new StringBuilder();
		classArgs.append("-DbenchmarkClassName=");
		for (int i=0; i<args.length; ++i) {
			classArgs.append(args[i]);
			if (i != (args.length-1)) {
				classArgs.append(",");
			}
		}
		caliperArgs[0] = classArgs.toString();
		
		Runner.main(CaliperBenchmark.class, caliperArgs);
	}

}
