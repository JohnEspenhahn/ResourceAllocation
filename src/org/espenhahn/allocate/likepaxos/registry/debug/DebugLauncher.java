package org.espenhahn.allocate.likepaxos.registry.debug;

import org.espenhahn.allocate.likepaxos.registry.PaxosNodeLauncher;
import org.espenhahn.allocate.likepaxos.registry.PaxosSessionServerLauncher;
import org.espenhahn.allocate.likepaxos.registry.debug.mvc.DebugController;
import org.espenhahn.allocate.likepaxos.registry.debug.mvc.DebugRenderer;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DebugLauncher {
	
	private static final int RUNTIME = 17;
	private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	private static DebugController controller;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		DebugRenderer renderer = new DebugRenderer();
		DebugLauncher.controller = new DebugController(renderer);

		// Start launcher and outputting from it
		Process launcher = exec(PaxosSessionServerLauncher.class);
		final InputStream inStream = launcher.getInputStream();
		new Thread(() -> {
			Scanner scan = new Scanner(new InputStreamReader(inStream));
			while (scan.hasNextLine()) {
				String stdin_line = scan.nextLine();
				System.out.println("[LAUNCHER] " + stdin_line);
			}
			scan.close();
		}).start();

		Thread.sleep(700);
		
		PrintWriter a = execWriter(PaxosNodeLauncher.class, "9001", "localhost"),
					b = execWriter(PaxosNodeLauncher.class, "9002", "localhost"),
					c = execWriter(PaxosNodeLauncher.class, "9003", "localhost");

		Thread.sleep(3000);
		
		a.println("9001");
		a.println("9002");
		a.println("9003");
		
		a.print("acceptorSetLargestProposalNumber\n1.0 ");
		
		a.flush();

		b.println("9001");
		b.println("9002");
		b.println("9003");
		b.flush();
		
		c.println("9001");
		c.println("9002");
		c.println("9003");
		c.flush();
		
		// No longer need launcher (all directly connected)
		Thread.sleep(3000);
		launcher.destroyForcibly();
		
		a.println("start");
		b.println("start");
		c.println("start");
		a.flush();
		b.flush();
		c.flush();
		
		b.println("sendPromiseRequest");
		b.flush();
		
		Thread.sleep(5000);
		a.println("sendPromiseRequest");
		a.flush();
		
		// Start
		renderer.show();
	}
	
	private static PrintWriter execWriter(Class clazz, String... args) throws IOException, InterruptedException {
		return new PrintWriter(exec(clazz, args).getOutputStream());
	}

    private static Process exec(Class clazz, String... args) throws IOException, InterruptedException {
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

		Process process = builder.start();
		
		if (clazz == PaxosNodeLauncher.class) {
			String name = args[0];
			
			// Redirect this processes stdout to console
			final InputStream inStream = process.getInputStream();
			new Thread(() -> {
	            InputStreamReader reader = new InputStreamReader(inStream);
	            Scanner scan = new Scanner(reader);
	            while (scan.hasNextLine()) {
	            	String stdin_line = scan.nextLine();
	            	
	            	// Update MVC
	            	controller.appendCommand(name, stdin_line);
	            	
	               System.out.println("[" + name + "] " + stdin_line);
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
			System.err.println("*** SAFE TO KILL ME ***");
			process.destroy();
		}, RUNTIME, TimeUnit.SECONDS);
			
		return process;
	}
}
