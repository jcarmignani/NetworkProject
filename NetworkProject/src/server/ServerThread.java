/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Julien CARMIGNANI
 */

public class ServerThread extends Thread{
    //Constants
    private final int BUFFER_SIZE = 1024;
    //Variables
    private SocketChannel client;
    private String remoteAddress;
    
    public ServerThread(SocketChannel client){
        this.client = client;
        
        try{
            client.configureBlocking(false);
        } catch (IOException ex) {
            System.out.println("Error while puting non blocking mode on SocketChannel");
        }
        
        try{
            this.remoteAddress = this.client.getRemoteAddress().toString();
        }
        catch(IOException e){
            System.out.println("Error while getting the Remote Address IP");
        }
    }
    
    public void run(){
        //Read the buffer
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        RandomAccessFile aFile = null;
        FileOutputStream fos = null;
        FileChannel fc = null;
        int byteRead = 0;
        System.out.println("Openening Thread server : " + this.getName());
        
        try{
            aFile = new RandomAccessFile("/Users/starwers13/NetBeansProjects/test.jpg", "rw");
            fc = aFile.getChannel();
            
            while((byteRead = client.read(buffer)) > 0) {
                buffer.flip();
                fc.write(buffer);
                buffer.clear();
            
                System.out.println("Size Buffer = " + byteRead);
                System.out.println("Receiving...");
            }
        }
        catch(FileNotFoundException fe){
            fe.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /*int bufferRead = -1;
        
        try{
            bufferRead = client.read(buffer);
        } 
        catch (IOException ex) {
            System.out.println("Error during reading buffer...");
        }
        
        /*buffer.flip();
        System.out.println("Reading Socket and writing file...");
        byte[] buff = new byte[BUFFER_SIZE];
        buffer.get(buff, 0, bufferRead);
        System.out.println("Server said: " + new String(buff));
        
        try {
            fc.write(buffer);
        } 
        catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while(bufferRead != -1 && buffer.hasRemaining()){
            try{
                buffer.flip();
                fc.write(buffer);
                buffer.compact();
                bufferRead = client.read(buffer);
                System.out.println("Reading Socket and writing file...");
                System.out.println("bufferRead = " + bufferRead);
                
                try {
                    Thread.sleep(4000);
                } 
                catch (InterruptedException ex) {
                    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            catch(IOException e){
                System.out.println("Error while reading the buffer " + this.remoteAddress);
            } 
        }*/
        
        System.out.println("Closing Thread server : " + this.getName());
    }
}
