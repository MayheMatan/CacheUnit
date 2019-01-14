package com.hit.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import com.hit.server.Server;


public class CLI extends java.lang.Object implements Runnable {

	private final PropertyChangeSupport support = new PropertyChangeSupport(this);
	private Scanner scanner;
	private PrintWriter printWriter;
	private StringBuilder builder = new StringBuilder("");
	private static final String STARTING = "starting server....";
	private static final String SHUTDOWN = "shutdown server";
	private static final String INVALID = "unknown command";
	private static final String ENTER_COMMAND = "Please Enter Your Command: ";
	private Thread thread;
	private String serverStatus = "on";
	
	public CLI(InputStream in, OutputStream out) {

		scanner = new Scanner(in);
		printWriter = new PrintWriter(out);
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		support.removePropertyChangeListener(pcl);
	}
    // both methodes are implemented to notify other classes.

	public void write(String string) {
		System.out.println(builder.append(string));
		builder.delete(0, string.length());
	}

	@Override
	public void run() {
		while (true) {
			write(ENTER_COMMAND);
			String input = scanner.nextLine();
			while (!input.equalsIgnoreCase("stop")) {
				if (input.equals("start")) {
					support.firePropertyChange(serverStatus,"off","on");
					write(STARTING);
					try {
						thread = new Thread((Runnable) new Server(34567));
					} catch (IOException e) {
						e.printStackTrace();
					}
					thread.start();
				} else if (!input.equals("stop")) {
					write(INVALID);
				}
				input = scanner.nextLine();
			}
			write(SHUTDOWN);
			support.firePropertyChange(serverStatus,"on","off");
		
		}
	
	}
}
