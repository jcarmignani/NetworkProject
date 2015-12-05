/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.io.IOException;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julien CARMIGNANI
 */
public class Server {
    private static final int nbrPort = 15000;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Initiliasing the Server...");
        //Opening Server Socket on the port 15000
        ServerSocketChannel server = null;
        
        try{
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            
            //Set the Socket listening on a specific port
            InetSocketAddress isa = new InetSocketAddress("127.0.0.1", nbrPort);
            server.socket().bind(isa);
            System.out.println("Server ready to listen on port " + nbrPort + "...");
        }
        catch(IOException e){
            System.out.println( "Impossible to open the socket of the server on the port " + nbrPort);
            e.printStackTrace();
        }
        
        //Waiting for connection 
        for(;;){
            SocketChannel client = null;
            System.out.println("Waiting For Connection...");
            
            try {
                Thread.sleep(4000);
            } catch (InterruptedException ex) {
                System.out.println("Thread sleeping error...");
            }
            
            try{
                client = server.accept();
                if(client == null){
                    continue;
                }
            }
            catch(IOException e){
                System.out.println("Error while opening a client connection");
            }
            
            //Create a Thread to process the Client Connection
            System.out.println("Connection established...");
            new ServerThread(client).start();
        }
    }
    
}
