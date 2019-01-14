package com.hit.server;

import java.beans.PropertyChangeEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.hit.services.CacheUnitController;

public class Server extends java.lang.Object implements java.beans.PropertyChangeListener, java.lang.Runnable
{
	private static final int PORT = 34567;
	private static boolean SERVER_IS_RUNNING = true;
	private static final int MAX_CLIENTS = 5;
	private static String serverStatus = "on";
	private Thread thread;
	private CacheUnitController<Request<String>> cacheUnitController;
	private ThreadPoolExecutor threadPoolExecutor;
	ServerSocket serverSocket;
	Socket socket;

	public Server(int port) throws FileNotFoundException, IOException {
		cacheUnitController = new CacheUnitController<Request<String>>();
		threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_CLIENTS);
	}

	public void propertyChange(PropertyChangeEvent evt) { // updates observers from the cli
		serverStatus = (String) evt.getNewValue();
	}

	@Override
	public void run() {
		try { // opening new connection
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (SERVER_IS_RUNNING) {
			try {
				socket = serverSocket.accept();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				thread = new Thread(new HandleRequest<Request<String>>(socket, cacheUnitController));
				threadPoolExecutor.submit(thread); // counts the number of Exectutors
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (serverStatus.equals("off") && threadPoolExecutor.getActiveCount() == 0) {
				try {
					threadPoolExecutor.shutdown(); // shuting down server and the threadooling.
				} catch (Exception e) {
					e.printStackTrace();
				} finally { // closing everything
					try {
						socket.close();
						serverSocket.close();
						SERVER_IS_RUNNING = false;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}