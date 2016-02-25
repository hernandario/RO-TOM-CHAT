package es.dam.chat.presentacion;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import es.dam.chat.modelo.Broadcast;
import es.dam.chat.modelo.Chat;
import es.dam.chat.persistencia.ChatDAO;
import es.dam.chat.servicios.Servicio;

/**
 *Esta clase es la encargada de gestionar El servidor al que se conectaran los clientes.
 * 
 * @author Hernan Darío Villamil y Elizabeth Gordon
 * @version 1.0
 * @since 21/02/2016
 */
public class Consola extends JFrame {
	
	
	private static JTextArea consola;
	private static Scanner entrada;
	private static PrintWriter salida;
	private static Socket socketCliente;
	private static ServerSocket socketServidor;
	private static String nickUsuario;
	private static int puerto = 1988;
	private static Servicio servicio;
	private static int id_chat;
	
	private static Chat chat;
	private static ChatDAO chatDAO;
	
	public static ArrayList<Socket> lista_sockets = new ArrayList<Socket>();
	public static ArrayList<String> lista_usuarios = new ArrayList<String>();
	
	public static void main(String[] args){
		
		Consola c = new Consola();
		ejecutarServidor();
		
	}
	
	/**
	 * Constructor que inicializa la GUI del servidor
	 * 
	 * @exception IOException si existe algún problema con la lectura de los mensajes BROADCAST
	 */
	public Consola(){
		
		super("CONSOLA"); 
		consola = new JTextArea(">...");
		DefaultCaret caret = (DefaultCaret) consola.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		consola.setLineWrap(true);
		consola.setWrapStyleWord(true);
		consola.setEditable(false);
		
		add(new JScrollPane(consola));
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 400);
		setLocationRelativeTo(null);
		
		
		addWindowListener(new WindowListener(){

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			/**
			 *@param e indica el tipo de evento. 
			 *@Override
			*/
			public void windowClosing(WindowEvent e) {
				for( int i = 0; i < lista_sockets.size(); i++){
					try{
						//Se envia un mensaje de error a todos los clientes para informarles que no pueden conectarse al servidor
						PrintWriter salida = new PrintWriter(lista_sockets.get(i).getOutputStream(), true);
						salida.println(Broadcast.BROADCAST_MENSAJE_ERROR_SERVIDOR); 
						
					}catch(IOException ex){
						
					}
				}
				System.exit(0);
				
			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});

	}
	
	/**
	 * Este etodo inicia la ejecución del servidor
	 * 
	 * @exception IOException si existe algún problema con la lectura de los mensajes BROADCAST
	 * @exception InterrumptedException Si la ejecucíon del servidor falla
	 */
	public static void ejecutarServidor(){
		
		try{
			
			socketServidor = new ServerSocket(puerto); //Se inicializa el nuevo ServerSocket
			//escribirEnConsola(String.valueOf(socketServidor.getInetAddress().getHostAddress()));
			
			escribirEnConsola("Esperando a que se conecte un usuario...");
			
			chat = new Chat("localhost", puerto); //Nuevo objeto de tipo Chat
			chatDAO = new ChatDAO(); //Nuevo objeto de tipo ChatDAO
			
			try{
			
			id_chat = chatDAO.insert(chat); //Llamada al metodo inset de la clase ChatDAO
			
			}catch(Exception ex){
				
				JOptionPane.showMessageDialog(null, ("ERROR:la conexión con la base de datos ha fallado: "),
						"Error de base de datos",JOptionPane.ERROR_MESSAGE);

				
			}
			
			
			while(true){
				//Si se detectan nuevos cleintes se añaden al chat
				TimeUnit.SECONDS.sleep(1);
				socketCliente = socketServidor.accept();
				agregarAChat(socketCliente);
				
			}
			
		}catch(IOException ex){
			
			//Mensaje de error
			ex.printStackTrace();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo que añade los clientes al chat
	 * 
	 * @param socketCliente
	 * @exception IOException si existe algún problema con la lectura de los mensajes BROADCAST
	 */
	public static void agregarAChat(Socket socketCliente){
		
		try {
			
			
			entrada = new Scanner(socketCliente.getInputStream()); //Se abre un inputStream con el cliente para recoger la información
			nickUsuario = entrada.nextLine(); //Se recoge el nick enviado por el usuaio
			
			//Si el nick ya existe no se añde y se informa al usaurio
			if(lista_usuarios.contains(nickUsuario)){
				
				escribirEnConsola("Ya hay un usuario con el nick \"" + nickUsuario + "\". Intentelo con otro nombre" );
				salida = new PrintWriter(socketCliente.getOutputStream(), true);
				salida.println(Broadcast.BROADCAST_MENSAJE_ERROR_USUARIO);
				socketCliente.close();
				
			}
			
			//Si el nick aún n existe se ñade a la lista de usaurios y el socket se guarda tambien.
			else{
				
				escribirEnConsola("Agregando usuario nuevo");
				lista_sockets.add(socketCliente);
				lista_usuarios.add(nickUsuario);
				escribirEnConsola(nickUsuario + " se ha conectado al chat.");
				
				servicio = new Servicio(nickUsuario, socketCliente, consola, id_chat );
				Thread hiloServicio = new Thread(servicio);
				hiloServicio.start();
				
			}
			
		} catch (IOException ex) {
			
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * Este metodo escribe en la consola la información.
	 * 
	 * @param linea String con la inforamación que se va a agregar a la consola.
	 */
	public static void escribirEnConsola(String linea){
		
		consola.append(linea);
		consola.append("\n");
		
	}
	


}
