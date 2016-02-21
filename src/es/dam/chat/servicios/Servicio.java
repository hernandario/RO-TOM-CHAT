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

/**
 * Esta clase es la encargada de gestionar los hilos de cada cliente por separado.
 * 
 * @author Hernan Darío Villamil y Elizabeth Gordon
 * @version 1.0
 * @since 21/02/2016
 */
public class Servicio implements Runnable {

	private final String IO_ERROR = "Se ha producido un error en la entrada/salida de datos";

	private String nickUsuario;
	private String usuarioHilo;
	private String mensaje;
	private Socket socketCliente;
	private JTextArea consola;
	private Fecha fecha;
	private int id_chat;
	
	private PrintWriter salidaHilo;
	private PrintWriter salida;
	private Scanner entrada;
	
	private Mensaje objMensaje;
	private MensajeDAO mensajeDAO;
	
	/**
	 * 
	 * @param nickUsuario String con el nick del usuario
	 * @param socket Cliente Socket con el socket del cliente
	 * @param consola JTextArea que dará acceso a la consola del servidor
	 * @param id_chat entero con el id del chat/servidor
	 */
	public Servicio(String nickUsuario, Socket socketCliente, JTextArea consola, int id_chat){
		
		this.nickUsuario = nickUsuario;
		this.socketCliente = socketCliente;
		this.consola = consola;
		this.id_chat = id_chat;
		
	}
	
	
	
	/**
	 * Inica la ejecución del hilo correspondiente al cliente
	 * 
	 * @Override
	 * @Exception IOException si existe algún problema con la lectura de los mensajes BROADCAST
	 */
	public void run() {
		
		//Se abre una entrada y una salida de información para enviar y leer mensajes
		try{
			salida = new PrintWriter(socketCliente.getOutputStream());
			entrada = new Scanner(socketCliente.getInputStream());
			anunciarConexion();
			
		}catch(IOException ex){
			consola.append(IO_ERROR);
			ex.printStackTrace();
		}
		
		while(entrada.hasNext()){
			
			int bcast = Integer.parseInt(entrada.nextLine()); //La primer linea de cada mensaje será un broadcast de tipo entero
			
			switch(bcast){
			
			case Broadcast.BROADCAST_MENSAJE_ACTUALIZAR_LISTA_USUARIOS: //Cuando se trare de este, se llama al metodo AbandonarChat()
				
				try {
					AbandonarChat();
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
				
				break;
				
			case Broadcast.BROADCAST_MENSAJE_IO: //Cuando se trare de este, se llama al metodo enviarMensaje()
				
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
	
	/**
	 * Metodo que enviará el mensaje a todos los clientes conectados
	 * 
	 * @throws IOException
	 */
	private synchronized void enviarMensaje() throws IOException{
		
		fecha = new Fecha(); //Se crea un objeto de tipo fecha con la ohra y la fecha en la que se envio el mensaje

		usuarioHilo = entrada.nextLine(); //Se recoge la información del usuario
		mensaje = entrada.nextLine(); //Se recoge la información del mensaje
		System.out.println("Servicio: " + mensaje);
		
		String cola = usuarioHilo + " ha dicho (" + fecha.obtenerHora() + "): ";
		
		consola.append(cola + mensaje);
		
		//Se guarda el mensaje en la base de datos
		objMensaje = new Mensaje(usuarioHilo, mensaje, fecha.getFecha(), id_chat);	
		mensajeDAO = new MensajeDAO();
		mensajeDAO.insert(objMensaje);
		
		
		for(int i = 0; i < Consola.lista_usuarios.size(); i++){
			
			//Se envia el mensaje a todos los clientes excepto al que lo envio
			if(!Consola.lista_usuarios.get(i).equals(usuarioHilo)){
			
				salidaHilo = new PrintWriter(Consola.lista_sockets.get(i).getOutputStream(), true);
				
				salidaHilo.println(Broadcast.BROADCAST_MENSAJE_IO);
				salidaHilo.println(cola + mensaje);
			}
			
		}
		
	}
	
	
	/**
	 * Metodo que informa a todos los clientes que un usuario se ha desconectado
	 * 
	 * @throws IOException
	 */
	private synchronized void AbandonarChat() throws IOException{
		
		//Se envia un mensaje a todos los clientes informando que se ha abandonado el chat
		for(int i = 0; i < Consola.lista_sockets.size(); i++){
			
			salidaHilo = new PrintWriter(Consola.lista_sockets.get(i).getOutputStream(), true);
			
			salidaHilo.println(Broadcast.BROADCAST_MENSAJE_BORRAR_USUARIO);
			salidaHilo.println(nickUsuario);
			
			salidaHilo.println(Broadcast.BROADCAST_MENSAJE_IO);
			salidaHilo.println(nickUsuario + " se ha dseconectado.");
		
		}
		
		consola.append(nickUsuario + "Ha dejado el chat");
		consola.append("\n");
		
		//Se borra el nick del usuario y el socket de sus respectivas listas
		Consola.lista_sockets.remove(Consola.lista_usuarios.indexOf(nickUsuario));
		Consola.lista_usuarios.remove(Consola.lista_usuarios.indexOf(nickUsuario));
		
	}
	
	/**
	 * Metodo que informa a todos los clientes que un usuario se ha conectado
	 * 
	 * @throws IOException
	 */
	private synchronized void anunciarConexion() throws IOException{
		
		//Se envia un mensaje a todos los clientes informando que se ha conectado el chat
		for(int i = 0; i < Consola.lista_sockets.size(); i++){
			
			salidaHilo = new PrintWriter(Consola.lista_sockets.get(i).getOutputStream(), true);
			
			salidaHilo.println(Broadcast.BROADCAST_MENSAJE_IO);
			salidaHilo.println(nickUsuario + " se ha conectado.");
			
			
			//Se actualiza la lista de usuarios de cada cliente
			for(int j = 0; j < Consola.lista_sockets.size(); j++){
				
				salidaHilo.println(Broadcast.BROADCAST_MENSAJE_ACTUALIZAR_LISTA_USUARIOS);
				salidaHilo.println(Consola.lista_usuarios.get(j));
				
			}
		}
		
	}

}
