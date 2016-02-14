package es.dam.chat.servicios;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JTextArea;

import es.dam.chat.presentacion.Consola;

public class Servicio implements Runnable {

	private final String IO_ERROR = "Se ha producido un error en la entrada/salida de datos";
	private final int USUARIO_ABANDONA = 1;
	private final int USUARIO_ENVIA_MENSAJE = 2;
	
	private String nickUsuario;
	private String nickOtroUsuario;
	private String mensaje;
	private Socket socketCliente;
	private JTextArea consola;
	
	private PrintWriter otraSalida;
	private PrintWriter salida;
	private Scanner entrada;
	
	
	public Servicio(String nickUsuario, Socket socketCliente, JTextArea consola){
		
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
		
		nickOtroUsuario = entrada.nextLine();
		mensaje = entrada.nextLine();
		consola.append(nickOtroUsuario + " ha dicho: " + mensaje); 
		
		for(int i = 0; i < Consola.lista_usuarios.size(); i++){
			
			otraSalida = new PrintWriter(Consola.lista_sockets.get(i).getOutputStream(), true);
			
			otraSalida.println("2");
			otraSalida.println(nickOtroUsuario +": " + mensaje);
			
		}
		
	}
	
	
	private synchronized void AbandonarChat() throws IOException{
		
		for(int i = 0; i < Consola.lista_sockets.size(); i++){
			
			otraSalida = new PrintWriter(Consola.lista_sockets.get(i).getOutputStream(), true);
			
			otraSalida.println("3");
			otraSalida.println(nickUsuario);
		
		}
		
		consola.append(nickUsuario + "Ha dejado el chat");
		consola.append("\n");
		
		Consola.lista_sockets.remove(Consola.lista_usuarios.indexOf(nickUsuario));
		Consola.lista_usuarios.remove(Consola.lista_usuarios.indexOf(nickUsuario));
		
	}
	
	private synchronized void anunciarConexion() throws IOException{
		
		for(int i = 0; i < Consola.lista_sockets.size(); i++){
			
			
			otraSalida = new PrintWriter(Consola.lista_sockets.get(i).getOutputStream(), true);
			
			otraSalida.println("2");
			otraSalida.println(nickUsuario + " se ha conectado.");
			
			for(int j = 0; j < Consola.lista_sockets.size(); j++){
				
				otraSalida.println("1");
				otraSalida.println(Consola.lista_usuarios.get(j));
				
			}
		}
		
	}

}
