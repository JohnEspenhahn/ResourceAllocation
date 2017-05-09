package org.espenhahn.allocate.likepaxos.registry;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DebugLauncher {
	
	static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public static void main(String[] args) throws IOException, InterruptedException {
		Process launcher = exec(PaxosSessionServerLauncher.class);

		Thread.sleep(700);
		
		PrintWriter a = execWriter(PaxosNodeLauncher.class, "A", "9001", "localhost"),
					b = execWriter(PaxosNodeLauncher.class, "B", "9002", "localhost"),
					c = execWriter(PaxosNodeLauncher.class, "C", "9003", "localhost");

		Thread.sleep(2000);
		
		a.println("B");
		a.println("C");
		a.flush();

		b.println("A");
		b.println("C");
		b.flush();
		
		c.println("A");
		c.println("B");
		c.flush();
		
		// No longer need launcher (all directly connected)
		Thread.sleep(1000);
		launcher.destroyForcibly();
		
		a.println("start");
		b.println("start");
		c.println("start");
		a.flush();
		b.flush();
		c.flush();
	}
	
	public static PrintWriter execWriter(Class clazz, String... args) throws IOException, InterruptedException {
		return new PrintWriter(exec(clazz, args).getOutputStream());
	}

	public static Process exec(Class clazz, String... args) throws IOException, InterruptedException {
		String javaHome = System.getProperty("java.home");
		String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
		String classPath = System.getProperty("java.class.path");
		String className = clazz.getCanonicalName();

		String[] all_args = new String[args.length + 4];
		all_args[0] = javaBin;
		all_args[1] = "-cp";
		all_args[2] = classPath;
		all_args[3] = className;
		System.arraycopy(args, 0, all_args, 4, args.length);

		ProcessBuilder builder = new ProcessBuilder(all_args);
//		builder.redirectOutput(Redirect.INHERIT);

		Process process = builder.start();
		
		if (clazz == PaxosNodeLauncher.class) {
			String name = args[0];
			
			// Redirect this processes stdout to console
			final InputStream inStream = process.getInputStream();
			new Thread(() -> {
	            InputStreamReader reader = new InputStreamReader(inStream);
	            Scanner scan = new Scanner(reader);
	            while (scan.hasNextLine()) {
	               System.out.println("[" + name + "] " + scan.nextLine());
	            }
	            scan.close();
		      }).start();
			
			// Redirect this processes stdout to console
			final InputStream inErrStream = process.getErrorStream();
			new Thread(() -> {
	            InputStreamReader reader = new InputStreamReader(inErrStream);
	            Scanner scan = new Scanner(reader);
	            while (scan.hasNextLine()) {
	               System.err.println("[" + name + "] " + scan.nextLine());
	            }
	            scan.close();
		      }).start();
		}
		
		executor.schedule(() -> {
			process.destroy();
		}, 10, TimeUnit.SECONDS);
			
		return process;
	}
}
