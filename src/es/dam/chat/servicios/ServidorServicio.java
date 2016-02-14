package es.dam.chat.servicios;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JTextArea;

public class ServidorServicio implements Runnable {

	private final String IO_ERROR = "Se ha producido un error en la entrada/salida de datos";
	private final int USUARIO_ABANDONA = 1;
	private final int USUARIO_ENVIA_MENSAJE = 2;
	
	private String nickUsuario;
	private Socket socketCliente;
	private JTextArea consola;
	
	private PrintWriter salida;
	private Scanner entrada;
	
	
	public ServidorServicio(String nickUsuario, Socket socketCliente, JTextArea consola){
		
		this.nickUsuario = nickUsuario;
		this.socketCliente = socketCliente;
		this.consola = consola;
		
	}
	
	
	@Override
	public void run() {
		
		try{
			salida = new PrintWriter(socketCliente.getOutputStream());
			entrada = new Scanner(socketCliente.getInputStream());
			
		}catch(IOException ex){
			consola.append(IO_ERROR);
			ex.printStackTrace();
		}
		
		while(entrada.hasNext()){
			
			switch(Integer.parseInt(entrada.nextLine())){
			
			case USUARIO_ABANDONA:
				
				break;
				
			case USUARIO_ENVIA_MENSAJE:
				
				break;
			
			
			}
			
		}

	}
	
	private synchronized void enviarMensaje() throws IOException{
		
		
		
	}
	
	
	private synchronized void AbandonarChat() throws IOException{
		
	}
	
	private synchronized void anunciarConexion() throws IOException{
		
	}

}
