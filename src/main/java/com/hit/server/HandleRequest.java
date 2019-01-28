package com.hit.server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.services.CacheUnitController;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Scanner;

public class HandleRequest<T> extends java.lang.Object implements Runnable
{

	private CacheUnitController<T> cacheUnitController;
	private Socket socket;
    public static int countHandledRequests = 0;

	public HandleRequest(Socket s, CacheUnitController<T> controller) {
		this.cacheUnitController = controller;
		this.socket = s;
	}

	@Override
	public void run() {
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;

		try {
            inputStream = new ObjectInputStream (socket.getInputStream ());
            outputStream = new ObjectOutputStream (socket.getOutputStream ());
            System.out.println("waits for the request");
            String req  = (String) inputStream.readObject ();
            System.out.print(req);
            System.out.println("got the request");
		    Type ref = new TypeToken<Request<DataModel<T>[]>>() {}.getType();
		    Request<DataModel<T>[]> request = new Gson().fromJson(req, ref);
		    System.out.println("converted json");
			String action = request.getHeaders().get("action");
			System.out.println("The requested action is: " + action);
			
			switch (action.toLowerCase()) {
			case "update":
				System.out.println(request.getBody());
				for(int i =0; i<request.getBody().length; i++)
					System.out.println(request.getBody()[i]);
				System.out.println(request.getBody().getClass());
				boolean updateResult = this.cacheUnitController.update(request.getBody());
				System.out.println("UPDATE before send: " + updateResult);
				outputStream.writeObject("Update Result=" + updateResult);
				System.out.println("UPDATE: " + updateResult);
				break;
			case "delete":
				boolean deleteResult = this.cacheUnitController.delete(request.getBody());
				outputStream.writeObject("Delete Result=" + deleteResult);
				break;
			case "get":
				DataModel<T>[] dms = this.cacheUnitController.get(request.getBody());
				if (dms != null) {
					outputStream.writeObject(dms.toString());
				}else {
					outputStream.writeObject( "Don't exist in the memory");
				}
				break;
			
			default:
				System.out.println("SERVER CANT UNDERSTAND");
				outputStream.writeObject("FAILED");
				break;
			}
			outputStream.flush ();
			outputStream.close ();
            inputStream.close ();
			
		} catch (

		IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
