package com.hit.server;
/*
 * Gets request(socket) from server, reads the json fields and passing the action
 * command to CacheUnitController to invoke the method base on the string.
 * */
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.Map;
import java.util.Scanner;

public class HandleRequest<T> extends java.lang.Object implements Runnable
{

    Socket socket;
    CacheUnitController<T> unitController;
    Request<DataModel<T>[]> socketRequest;
    ObjectInputStream input;
    ObjectOutputStream output;
    Gson gson;
    Type ref;

    //initialize request socket(from server class) and create a gson instance
	public HandleRequest(java.net.Socket s, CacheUnitController<T> controller)
    {
        this.socket = s;
        this.unitController = controller;

        try
        {
        	Scanner reader = new Scanner(new InputStreamReader(socket.getInputStream()));
        	PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e)
        {
            e.printStackTrace ();
        }

        gson = new GsonBuilder().create();

    }


    @SuppressWarnings({ "rawtypes" })
	@Override
    public void run() {
    	
    	String inputString;
        String command;
        DataModel<T>[] body;
        
        try
        {
            ref = new TypeToken<Request<DataModel<T>[]>> (){}.getType();
            inputString = (String)input.readObject();
            socketRequest = new Gson().fromJson(inputString, ref);

        } catch (IOException e)
        {
            e.printStackTrace ();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace ();
        }
        //read headers from json and get the action
        Map headers = socketRequest.getHeaders ();
        command = (String)headers.get("action");
        body = socketRequest.getBody();

        //checks which action to pass to controller
        if(command.equals("GET"))
        {
            DataModel[] dataModels = null;
			try {
				dataModels = unitController.get(body);
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

            for(DataModel dataModel: dataModels)
            {
                try
                {
                    String outputGson = gson.toJson(dataModel);
                    output.writeObject(outputGson);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }


        }else if(command.equals("DELETE"))
        {

            boolean delete = false;
			try {
				try {
					delete = unitController.delete(body);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            try
            {
                output.writeObject(delete);
            } catch (IOException e)
            {
                e.printStackTrace();
            }

        }else if(command.equals("UPDATE"))
        {

            boolean update = false;
			try {
				update = unitController.update(body);
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

            try
            {
                output.writeObject(update);
            } catch (IOException e)
            {
                e.printStackTrace();
            }

        }else
        {
            try
            {
                output.writeObject("Unknown Action");
                output.flush();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
   
}