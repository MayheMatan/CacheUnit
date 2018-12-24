package com.hit.server;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.hit.util.CLI;

public class CacheUnitServerDriver
{
		public static void main(java.lang.String[] args) throws FileNotFoundException, IOException {
		CLI cli = new CLI(System.in, System.out);
		Server server = new Server(34567);
		cli.addPropertyChangeListener(server);
		new Thread(cli).start();
		}
}
