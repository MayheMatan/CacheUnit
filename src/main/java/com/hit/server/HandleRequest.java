package com.hit.server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.services.CacheUnitController;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Scanner;

public class HandleRequest<T> extends java.lang.Object implements Runnable
{
	private static final String UPDATE = "update";
	private static final String DELETE = "delete";
	private static final String GET = "get";
	private static final String ACTION = "action";

	private CacheUnitController<T> cacheUnitController;
	private Socket socket;

	public HandleRequest(Socket s, CacheUnitController<T> controller) {
		this.cacheUnitController = controller;
		this.socket = s;
	}

	@Override
	public void run() {

		try {
			Scanner reader = new Scanner(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			String req = (String)reader.nextLine();
		    Type ref = new TypeToken<Request<DataModel<T>[]>>() {}.getType();
		    Request<DataModel<T>[]> request = new Gson().fromJson(req, ref);

			String action = request.getHeaders().get(ACTION);
			
			switch (action.toLowerCase()) {
			case UPDATE:
				boolean updateResult = this.cacheUnitController.update(request.getBody());
				writer.println("Update Result=" + updateResult);
				break;
			case DELETE:
				boolean deleteResult = this.cacheUnitController.delete(request.getBody());
				writer.println("Delete Result=" + deleteResult);
				break;
			case GET:
				DataModel<T>[] dms = this.cacheUnitController.get(request.getBody());
				if (dms != null) {
					writer.println(dms.toString());
				}else {
					writer.println( "Don't exist in the memory");
				}
				break;
			}
			writer.flush();
			reader.close();
			writer.close();
		} catch (

		IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}