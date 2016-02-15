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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import es.dam.chat.modelo.Usuario;
import es.dam.chat.servicios.Servicio;

public class Consola extends JFrame {
	
	
	private static JTextArea consola;
	private static Scanner entrada;
	private static PrintWriter salida;
	private static Socket socketCliente;
	private static ServerSocket socketServidor;
	private static String nickUsuario;
	private static int puerto = 1988;
	private static Servicio servicio;
	
	public static ArrayList<Socket> lista_sockets = new ArrayList<Socket>();
	public static ArrayList<String> lista_usuarios = new ArrayList<String>();
	
	public static void main(String[] args){
		
		Consola c = new Consola();
		ejecutarServidor();
		
	}
	
	public Consola(){
		
		super("CONSOLA");
		consola = new JTextArea(">...");
		DefaultCaret caret = (DefaultCaret) consola.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		consola.setLineWrap(true);
		consola.setWrapStyleWord(true);
		
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

			@Override
			public void windowClosing(WindowEvent e) {
				for( int i = 0; i < lista_sockets.size(); i++){
					try{
						
						PrintWriter salida = new PrintWriter(lista_sockets.get(i).getOutputStream(), true);
						salida.println(Chat.BROADCAST_MENSAJE_ERROR_SERVIDOR);
						
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
	
	public static void ejecutarServidor(){
		
		try{
			
			socketServidor = new ServerSocket(puerto);
			escribirEnConsola("Esperando");
			
			while(true){
				
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
	
	
	public static void agregarAChat(Socket socketCliente){
		
		try {
			
			entrada = new Scanner(socketCliente.getInputStream());
			nickUsuario = entrada.nextLine();
			
			if(lista_usuarios.contains(nickUsuario)){
				
				escribirEnConsola("Ya hay un usuario con el nick \"" + nickUsuario + "\". Intentelo con otro nombre" );
				salida = new PrintWriter(socketCliente.getOutputStream(), true);
				salida.println(Chat.BROADCAST_MENSAJE_ERROR_USUARIO);
				socketCliente.close();
				
			}
			
			else{
				
				escribirEnConsola("Agregando usuario nuevo");
				lista_sockets.add(socketCliente);
				lista_usuarios.add(nickUsuario);
				escribirEnConsola(nickUsuario + " se ha conectado al char.");
				
				servicio = new Servicio(nickUsuario, socketCliente, consola);
				Thread hiloServicio = new Thread(servicio);
				hiloServicio.start();
				
			}
			
		} catch (IOException ex) {
			
			ex.printStackTrace();
		}
		
	}
	
	public static void escribirEnConsola(String linea){
		
		consola.append(linea);
		consola.append("\n");
		
	}
	


}
