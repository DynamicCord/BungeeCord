/*
 *
 *  * Copyright © 2015 Paul Waslowski <freack1208@gmail.com>
 *  * This work is free. You can redistribute it and/or modify it under the
 *  * terms of the Do What The Fuck You Want To Public License, Version 2,
 *  * as published by Sam Hocevar. See the LICENSE file for more details.
 *
 */

package me.freack100.dynamicbungee.socket;

import me.freack100.dynamicbungee.DynamicBungee;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {

    private DynamicBungee plugin;
    private Socket socket;

    private BufferedReader inFromClient = null;
    private DataOutputStream outToClient = null;

    public ClientThread(DynamicBungee plugin, Socket socket) {
        this.plugin = plugin;
        this.socket = socket;
    }

    public void run() {
        try {
            inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outToClient = new DataOutputStream(socket.getOutputStream());

            String[] message = inFromClient.readLine().split(";");
            if (message[0].equals("create")) {
                plugin.addServer(message[1], message[2], Integer.parseInt(message[3]));
            } else if (message[0].equals("remove")) {
                plugin.removeServer(message[1]);
            }

            outToClient.writeBytes("Success");

            outToClient.flush();

            inFromClient.close();
            outToClient.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
