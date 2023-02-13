package com.chat.app.service;

import com.chat.app.handller.ChatMessage;
import com.chat.app.handller.ChatMessage.Action;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerService {
    private ServerSocket serverSocket;
    private Socket socket;
    private Map<String ,ObjectOutputStream> mapOnlines = new HashMap<String, ObjectOutputStream>();
    public ServerService(){
        try{
            serverSocket= new ServerSocket(8890);
            while(true){
                socket=serverSocket.accept();
                System.out.println("Server Started");
                new Thread(new SocketListner(socket)).start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
}


    private class SocketListner implements Runnable{
private ObjectOutputStream output;
private ObjectInputStream input;

public SocketListner(Socket socket){
    try {
        this.output=new ObjectOutputStream(socket.getOutputStream());
        this.input=new ObjectInputStream(socket.getInputStream());
    } catch (IOException ex) {
        Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
    }
}
        @Override
        public void run() {
            
        ChatMessage message=null;
    try {
        while((message=(ChatMessage) input.readObject())!=null){
            Action action=message.getAction();
            if(action.equals(Action.CONNECT)){
                boolean isConnect= connect(message, output);
                if(isConnect){
                    mapOnlines.put(message.getName(), output);
                    sendOnlines();
                }
            }else if(action.equals(Action.DISCONNECT)){
                disconnect(message, output);
            }else if(action.equals(Action.SEND_ONE)){
                sendOne(message);
            }else if(action.equals(Action.SEND_ALL)){
                sendAll(message);
            }else if(action.equals(Action.USER_ONLINE)){
                
            }
        }
    } catch (IOException ex) {
          disconnect(message, output);
        Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ClassNotFoundException ex) {
        Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
    }
        }
        
    }
    private boolean connect(ChatMessage message,ObjectOutputStream output){
        if(mapOnlines.size()==0){
            message.setText("YES");
            send(message, output);
            return true;
        }
        for(Map.Entry<String, ObjectOutputStream> kv :mapOnlines.entrySet()){
            if(kv.getKey()==message.getName()){
                message.setText("NO");
                send(message, output);
                return false;
            }else{
                message.setText("YES");
                 send(message, output);
                 return true;
            }
        }
        return false;
    }
    private void disconnect(ChatMessage message,ObjectOutputStream output){
        mapOnlines.remove(message.getName());
        message.setText(" Logout");
        message.setAction(Action.SEND_ONE);
        sendAll(message);
        System.out.println("user "+message.getName()+" Joind");
    }
    
        private void send(ChatMessage message,ObjectOutputStream output){
        try {
            output.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void sendOne(ChatMessage message) {
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            if (kv.getKey().equals(message.getNameReserved())) {
                try {
                    kv.getValue().writeObject(message);
                } catch (IOException ex) {
                    Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
        private void sendAll(ChatMessage message) {
               for(Map.Entry<String, ObjectOutputStream> kv :mapOnlines.entrySet()){
            if(!kv.getKey().equals(message.getName())){
                message.setAction(Action.SEND_ONE);
                try {
                    kv.getValue().writeObject(message);
                } catch (IOException ex) {
                    Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            }
        }
    }
        private void sendOnlines(){
            
            Set<String> setNames =new HashSet<String>();
            for(Map.Entry<String, ObjectOutputStream> kv :mapOnlines.entrySet()){
                setNames.add(kv.getKey());
            }
            
            
            
            ChatMessage message=new ChatMessage();
            message.setAction(Action.USER_ONLINE);
                    message.setSetOnline(setNames);
                     for(Map.Entry<String, ObjectOutputStream> kv :mapOnlines.entrySet()){
           message.setName(kv.getKey());
                try {
                    kv.getValue().writeObject(message);
                } catch (IOException ex) {
                    Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            
        }      
        }
}