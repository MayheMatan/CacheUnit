package com.hit.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.services.CacheUnitController;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Scanner;

public class HandleRequest<T> implements Runnable {
    private CacheUnitController<T> cacheUnitController;
    private Socket socket;
    public static int countHandledRequests = 0;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public HandleRequest(CacheUnitController<T> cacheUnitController, Socket socket) {
        this.cacheUnitController = cacheUnitController;
        this.socket = socket;
    }

    @Override
    public void run() {
    	try {
            inputStream = new ObjectInputStream (socket.getInputStream ());
            outputStream = new ObjectOutputStream (socket.getOutputStream ());
            String req = (String) inputStream.readObject();
            System.out.println(req);
            Type ref = new TypeToken<Request<DataModel<T>[]>>(){}.getType();
            Request<DataModel<T>[]> request = new Gson().fromJson(req, ref);
            String action = request.getHeaders().get("action");
            String response;
    
            switch (action.toLowerCase()) {
                case "update":
                    countHandledRequests++;
                    if (cacheUnitController.update(request.getBody())) {
                    	outputStream.writeObject("Updated the requested content...");
                    } else {
                        outputStream.writeObject("Couldn't Update the requested content...");
                    }

                    break;
                case "delete":
                    countHandledRequests++;
                    if (cacheUnitController.delete(request.getBody())) {
                    	outputStream.writeObject("Deleted the requested content...");
                    } else {
                    	outputStream.writeObject("Couldn't Delete the requested content...");
                    }

                    break;
                case "get":
                    countHandledRequests++;
                    DataModel<T>[] dms = cacheUnitController.get(request.getBody());
                    if (dms != null) {
                        response = "Retrieved the requested content...\n";
                        for (DataModel<T> dm : dms) {
                            response += dm.toString() + "\n";
                        }

                        response = response.replaceAll("\n", ".EndLine.");
                        outputStream.writeObject(response);
                    } else {
                    	outputStream.writeObject("Couldn't Retrieve the requested content...");
                    }

                    break;
                default:
                	outputStream.writeObject("Unknown Command!");
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
		} catch (

		IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}