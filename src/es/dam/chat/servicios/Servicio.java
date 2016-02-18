package es.dam.chat.servicios;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.util.Scanner;

import javax.swing.JTextArea;

import es.dam.chat.modelo.Broadcast;
import es.dam.chat.modelo.Fecha;
import es.dam.chat.modelo.Mensaje;
import es.dam.chat.persistencia.MensajeDAO;
import es.dam.chat.presentacion.ChatPresentacion;
import es.dam.chat.presentacion.Consola;

public class Servicio implements Runnable {

	private final String IO_ERROR = "Se ha producido un error en la entrada/salida de datos";

	private String nickUsuario;
	private String nickOtroUsuario;
	private String mensaje;
	private Socket socketCliente;
	private JTextArea consola;
	private Fecha fecha;
	private int id_chat;
	
	private PrintWriter otraSalida;
	private PrintWriter salida;
	private Scanner entrada;
	
	private Mensaje objMensaje;
	private MensajeDAO mensajeDAO;
	
	
	public Servicio(String nickUsuario, Socket socketCliente, JTextArea consola, int id_chat){
		
		this.nickUsuario = nickUsuario;
		this.socketCliente = socketCliente;
		this.consola = consola;
		this.id_chat = id_chat;
		
	}
	
	
	@Override
	public void run() {
		
		try{
			salida = new PrintWriter(socketCliente.getOutputStream());
			entrada = new Scanner(socketCliente.getInputStream());
			anunciarConexion();
			
		}catch(IOException ex){
			consola.append(IO_ERROR);
			ex.printStackTrace();
		}
		
		while(entrada.hasNext()){
			
			int bcast = Integer.parseInt(entrada.nextLine());
			
			switch(bcast){
			
			case Broadcast.BROADCAST_MENSAJE_ACTUALIZAR_LISTA_USUARIOS:
				
				try {
					AbandonarChat();
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
				
				break;
				
			case Broadcast.BROADCAST_MENSAJE_IO:
				
				try {
					System.out.println("El hilo recibe mensaje");
					enviarMensaje();
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
				
				break;
			
			
			}
			
		}

	}
	
	private synchronized void enviarMensaje() throws IOException{
		
		fecha = new Fecha();
		
		nickOtroUsuario = entrada.nextLine();
		mensaje = entrada.nextLine();
		System.out.println("Servicio: " + mensaje);
		consola.append(nickOtroUsuario + " ha dicho: " + mensaje + " a las: " + fecha.obtenerHora());
		
		objMensaje = new Mensaje(nickOtroUsuario, mensaje, fecha.getFecha(), id_chat);
		
		mensajeDAO = new MensajeDAO();
		
		mensajeDAO.insert(objMensaje);
		
		
		for(int i = 0; i < Consola.lista_usuarios.size(); i++){
			
			otraSalida = new PrintWriter(Consola.lista_sockets.get(i).getOutputStream(), true);
			
			otraSalida.println(Broadcast.BROADCAST_MENSAJE_IO);
			otraSalida.println(nickOtroUsuario +": " + mensaje);
			
		}
		
	}
	
	
	private synchronized void AbandonarChat() throws IOException{
		
		for(int i = 0; i < Consola.lista_sockets.size(); i++){
			
			otraSalida = new PrintWriter(Consola.lista_sockets.get(i).getOutputStream(), true);
			
			otraSalida.println(Broadcast.BROADCAST_MENSAJE_BORRAR_USUARIO);
			otraSalida.println(nickUsuario);
			
			otraSalida.println(Broadcast.BROADCAST_MENSAJE_IO);
			otraSalida.println(nickUsuario + " se ha deconectado.");
		
		}
		
		consola.append(nickUsuario + "Ha dejado el chat");
		consola.append("\n");
		
		Consola.lista_sockets.remove(Consola.lista_usuarios.indexOf(nickUsuario));
		Consola.lista_usuarios.remove(Consola.lista_usuarios.indexOf(nickUsuario));
		
	}
	
	private synchronized void anunciarConexion() throws IOException{
		
		for(int i = 0; i < Consola.lista_sockets.size(); i++){
			
			otraSalida = new PrintWriter(Consola.lista_sockets.get(i).getOutputStream(), true);
			
			otraSalida.println(Broadcast.BROADCAST_MENSAJE_IO);
			otraSalida.println(nickUsuario + " se ha conectado.");
			
			
			
			for(int j = 0; j < Consola.lista_sockets.size(); j++){
				
				otraSalida.println(Broadcast.BROADCAST_MENSAJE_ACTUALIZAR_LISTA_USUARIOS);
				otraSalida.println(Consola.lista_usuarios.get(j));
				
			}
		}
		
	}

}
