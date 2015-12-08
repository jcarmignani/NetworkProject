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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        MessageDigest messDigest = null;
        System.out.println("Openening Thread server : " + this.getName());
        MessageDigest messDigestTest = null;
        ByteBuffer bufferLast = ByteBuffer.allocate(BUFFER_SIZE);
        
        //****************************************************
        //********** Receiving Data from the Client **********
        //****************************************************
        try{
            aFile = new RandomAccessFile("/Users/starwers13/NetBeansProjects/test.jpg", "rw");
            fc = aFile.getChannel();
            messDigest = MessageDigest.getInstance("SHA-256");
            messDigestTest = MessageDigest.getInstance("SHA-256");
            
            while((byteRead = client.read(buffer)) > 0) {
                buffer.flip();
                fc.write(buffer);
            
                System.out.println("Size Buffer = " + byteRead);
                System.out.println("Receiving...");
                
                //messDigest.update(buffer);
                messDigest.update(buffer.array(), 0, byteRead);
                bufferLast = buffer;
                buffer.clear();
            }
        }
        catch(FileNotFoundException fe){
            fe.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        byte byteData[] = messDigest.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        System.out.println("Hex format : " + sb.toString());

        messDigestTest.update(bufferLast);
        byte byteData2[] = messDigest.digest();

        //convert the byte to hex format method 1
        StringBuffer sb2 = new StringBuffer();
        for (int i = 0; i < byteData2.length; i++) {
            sb2.append(Integer.toString((byteData2[i] & 0xff) + 0x100, 16).substring(1));
        }

        System.out.println("Hex format : " + sb2.toString());

        //****************************************************
        //*********** Sending Data from the Client ***********
        //****************************************************  
        try {
            client.write(ByteBuffer.wrap(byteData));
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Closing Thread server : " + this.getName());
    }
}
