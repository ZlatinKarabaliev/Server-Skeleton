package org.softuni.javache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;

public class ConnectionHandler extends Thread {
    private Socket clientSocket;

    private InputStream clientSocketInputStream;

    private OutputStream clientSocketOutputStream;
    private HashSet<RequestHandler> requestHandlers;

    public ConnectionHandler(Socket clientSocket, HashSet<RequestHandler> requestHandlers) {
        this.initializeConnection(clientSocket);
        this.requestHandlers = requestHandlers;
    }

    private void initializeConnection(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            this.clientSocketInputStream = this.clientSocket.getInputStream();
            this.clientSocketOutputStream = this.clientSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void proccesRequest() {
        for (RequestHandler requestHandler : requestHandlers) {
            requestHandler.handleRequest(this.clientSocketInputStream, this.clientSocketOutputStream);
        }
    }

    @Override
    public void run() {
        try {
            this.proccesRequest();
            this.clientSocketInputStream.close();
            this.clientSocketOutputStream.close();
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}






