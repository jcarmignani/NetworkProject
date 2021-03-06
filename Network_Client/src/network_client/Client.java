/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network_client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Julien CARMIGNANI
 */
public class Client {
    private static final int SIZE_BUFFER = 1024;
    private static int nbrPort = 15000;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException{
        SocketChannel client=SocketChannel.open();
        //client.configureBlocking(false);
        boolean status = client.connect(new InetSocketAddress("127.0.0.1", nbrPort));
        System.out.println("Coucou");
        System.out.println("Status = " + status);
        
        System.out.println("Opening client side on port " + nbrPort +"...");
        
        //Getting File
        
        //byte[] data = Files.readAllBytes(Paths.get("/Users/starwers13/Pictures/julien.txt"));
        //System.out.println("Size = " + data.length);
        ByteBuffer buffer = ByteBuffer.allocate(SIZE_BUFFER);
        File file = null;
        FileChannel fc = null;
        RandomAccessFile raFile = null;
        int byteRead = 0;
        
        try{
            file = new File("/Users/starwers13/Documents/stage.jpg");
            raFile = new RandomAccessFile(file, "r");
            fc = raFile.getChannel();
            while ((byteRead = fc.read(buffer)) > 0) {
                buffer.flip();
                client.write(buffer);
                buffer.clear();
                System.out.println("Size Buffer = " + byteRead);
                System.out.println("Sending...");
            }
            
            String hashFile = new String();
            ByteBuffer hashBuffer = ByteBuffer.allocate(32);
            
            while(hashFile.isEmpty()) {
                byteRead = client.read(hashBuffer);
                
                if(byteRead > 0){
                    hashBuffer.flip();
                    byte[] hashByte = hashBuffer.array();

                    System.out.println("Size Buffer Recveived = " + byteRead);
                    System.out.println("Receiving...");

                    buffer.clear();
                    System.out.println("OK Hash");
                    
                                //convert the byte to hex format method 1
                    StringBuffer sbHash = new StringBuffer();
                    for (int i = 0; i < hashByte.length; i++) {
                      sbHash.append(Integer.toString((hashByte[i] & 0xff) + 0x100, 16).substring(1));
                    }
                    
                    hashFile = sbHash.toString();
                    
                    System.out.println("Hex format Server : ");
                    System.out.println(hashFile);
                }
                else{
                    System.out.println("BLOCKED Hash");
                }
                System.out.println("Loop");
            }
            
            System.out.println("Hex format Server : " + hashFile);
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            FileInputStream fis = new FileInputStream("/Users/starwers13/Documents/stage.jpg");

            byte[] dataBytes = new byte[1024];

            int nread = 0; 
            while ((nread = fis.read(dataBytes)) != -1) {
              md.update(dataBytes, 0, nread);
            };
            byte[] mdbytes = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++) {
              sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            System.out.println("Hex format Check : " + sb.toString());

        }
        catch (FileNotFoundException e) {
        e.printStackTrace();
        } 
        catch (IOException e) {
        e.printStackTrace();
        } 

        System.out.println("Closing client side...");
        client.close();
    }
}
