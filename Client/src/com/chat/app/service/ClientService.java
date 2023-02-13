package com.chat.app.service;

import com.chat.app.handller.ChatMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientService {
    private Socket socket;
    private ObjectOutputStream output;
    
    public Socket connect(){
        try {
            this.socket=new Socket("localhost",8890);
            this.output=new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return socket;
    }
    
    public void send(ChatMessage message){
        try {
            this.output.writeObject(message); 
        } catch (IOException ex) {
            Logger.getLogger(ClientService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
