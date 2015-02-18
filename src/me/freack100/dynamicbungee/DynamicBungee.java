/*
 *
 *  * Copyright © 2015 Paul Waslowski <freack1208@gmail.com>
 *  * This work is free. You can redistribute it and/or modify it under the
 *  * terms of the Do What The Fuck You Want To Public License, Version 2,
 *  * as published by Sam Hocevar. See the LICENSE file for more details.
 *
 */

package me.freack100.dynamicbungee;

import me.freack100.dynamicbungee.socket.ServerThread;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;

public class DynamicBungee extends Plugin {

    ServerThread serverThread;

    @Override
    public void onEnable() {

        try {
            serverThread = new ServerThread(this);
            serverThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        serverThread.stopSocket();
    }

    public void addServer(String name, String ip, int port) {
        InetSocketAddress address = new InetSocketAddress(ip, port);

        UUID uuid = UUID.randomUUID();

        ServerInfo info = ProxyServer.getInstance().constructServerInfo(uuid.toString(), address, "lolhax", false);
        getProxy().getServers().put(name, info);

        System.out.println("[DynamicBungee] Added server " + name + " with ip " + ip + ":" + port);

        String[] cmd = new String[]{"/bin/sh", "/srv/scripts/newserver.sh " + name};

        try {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void removeServer(String name) {
        getProxy().getServers().remove(name);
        System.out.println("[DynamicBungee] Removed server " + name);

        String[] cmd = new String[]{"/bin/sh", "/srv/scripts/removeserver.sh " + name};

        try {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}
