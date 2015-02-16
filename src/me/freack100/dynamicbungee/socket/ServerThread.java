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

import java.io.IOException;
import java.net.*;

public class ServerThread extends Thread {

    private int port = 1337;
    private ServerSocket socket;
    private String ip = "127.0.0.1";
    private DynamicBungee plugin;

    public ServerThread(DynamicBungee plugin) throws IOException {
        this.plugin = plugin;
        socket = new ServerSocket(port, 10, InetAddress.getByName(ip));
    }

    @Override
    public void run(){
        while(true){
            try {
                Socket connected = socket.accept();
                (new ClientThread(plugin,connected)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopSocket(){
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
