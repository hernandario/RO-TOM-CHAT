package es.dam.chat.presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import es.dam.chat.modelo.Broadcast;

public class ChatPresentacion extends JFrame {
	
	
	private static String ipServidor;
	private static int puerto = 1988;
	private static Socket socketConexion;
	
	private static PrintWriter salida;
	private static Scanner entrada;
	
	private static String nickUsuario;
	private static String mensaje;
	
	public static ArrayList<String> lista_usuarios = new ArrayList<String>();

	private JPanel panelEnviar;
	private JPanel panelChat;
	private JPanel panelTitulo;
	
	private static JTextArea taChat;
	private static JLabel lblTitulo;
	
	private JTextField tfMensajes;
	private JButton btnEnviar;
	private JLabel lblPista;
	
	public static void main(String[] args){
		
		ChatPresentacion c = new ChatPresentacion();
		iniciarChat();
		
		conexion();
		
		while(true){
			broadCastReceiver();
		}
		
	}
	
	public ChatPresentacion(){
		super("RO-TOM CHAT");
		
		
		lblTitulo = new JLabel();
		lblPista = new JLabel("Escribe aquí un mensaje...");
		
		taChat = new JTextArea();
		taChat.setColumns(25);
		DefaultCaret caret = (DefaultCaret) taChat.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		taChat.setEditable(false);
		taChat.setBorder(javax.swing.BorderFactory.createMatteBorder(3,3,3,3,new Color(25,10,80)));
		taChat.setLineWrap(true);
		taChat.setWrapStyleWord(true);
		
		tfMensajes = new JTextField(30);
		tfMensajes.requestFocus();
		
		btnEnviar = new JButton("ENVIAR");
		btnEnviar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				enviarMensaje();
				
			}
			
		});
		
		
		panelEnviar = new JPanel();
		panelEnviar.setLayout(new BorderLayout());
		panelEnviar.add(lblPista, BorderLayout.NORTH);
		panelEnviar.add(tfMensajes, BorderLayout.CENTER);
		panelEnviar.add(btnEnviar, BorderLayout.EAST);
		
		panelTitulo = new JPanel();
		panelTitulo.setLayout(new BorderLayout());
		//TODO menu
		panelTitulo.add(lblTitulo, BorderLayout.CENTER);
		
		panelChat = new JPanel();
		panelChat.setLayout(new BorderLayout());
		panelChat.add(panelTitulo, BorderLayout.NORTH);
		panelChat.add(new JScrollPane(taChat), BorderLayout.CENTER);
		panelChat.add(panelEnviar, BorderLayout.SOUTH);
		
		
		
		setLayout(new BorderLayout());
		add(panelChat, BorderLayout.CENTER);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 900);
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowListener(){

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				
				salida.println(Broadcast.BROADCAST_MENSAJE_ACTUALIZAR_LISTA_USUARIOS);
				
				try{
					
					socketConexion.close();
					System.exit(0);
					
				}catch(IOException ex){
					
					//Mensaje error
					ex.printStackTrace();
					
				}
				
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

	public static void iniciarChat(){
		
		pedirIp();
		pedirNick();
		lblTitulo.setText("Hola " + nickUsuario + "\nConectado a: " + ipServidor);
	
		
	}
	
	
	public static void pedirIp(){
		
		String ip = "";
		
		do{
			
			ip = JOptionPane.showInputDialog("Introduzca la IP del servidor al que se quiere conectar: ");
			
			if(ip.trim().equals(""))
				JOptionPane.showMessageDialog(null, "No puede introducir una IP vacia","IP Vacia",
						JOptionPane.ERROR_MESSAGE);
			
		}
		while(ip.trim().equals(""));
		
		ipServidor = ip;
		
	}
	
	public static void pedirNick(){
		
		String nick = "";
		
		do{
			
			nick = JOptionPane.showInputDialog("Introduzca su nick:");
			
			if(nick.trim().equals(""))
				JOptionPane.showMessageDialog(null, "No puede introducir un nick vacio","Nick Vacio",
						JOptionPane.ERROR_MESSAGE);
			
		}
		while(nick.trim().equals(""));
		
		nickUsuario = nick;
		
	}
	
	public static void broadCastReceiver(){
		
		if(entrada.hasNext()){
			
			int bcast = Integer.parseInt(entrada.nextLine());
			
			switch(bcast){
			
			case Broadcast.BROADCAST_MENSAJE_ACTUALIZAR_LISTA_USUARIOS:
				
				//TODO: implmentar.
				String nombreUsuario = entrada.nextLine();
				if(!lista_usuarios.contains(nombreUsuario)){
					lista_usuarios.add(nombreUsuario);
					
				}
				
				break;
				
			case Broadcast.BROADCAST_MENSAJE_IO:
				
				mensaje = entrada.nextLine();
				taChat.append(mensaje);
				taChat.append("\n");
				
				break;
				
			case Broadcast.BROADCAST_MENSAJE_BORRAR_USUARIO:
				
				String usuario = entrada.nextLine();
				
				lista_usuarios.remove(lista_usuarios.indexOf(usuario));
				//taChat.append(usuario + " ha abandonado el chat");
				
				break;
				
			case Broadcast.BROADCAST_MENSAJE_ERROR_SERVIDOR:
				
				JOptionPane.showMessageDialog(null, "ERROR: NO ES POSIBLE ENCONTRAR EL SERVIDOR, INTENTELO MÁS TARDE",
						"Error De Conexión",JOptionPane.ERROR_MESSAGE);
	    		System.exit(0);
				
				break;
				
			case Broadcast.BROADCAST_MENSAJE_ERROR_USUARIO:
				
				try{
					socketConexion.close();
				}catch(IOException ex){
					
					//TODO Mensaje de error
					ex.printStackTrace();
					
				}
				
				JOptionPane.showMessageDialog(null, "ERROR: Ese nick ya se está utilizando","Nick En Uso",JOptionPane.ERROR_MESSAGE);
				
				break;
			
			
			}
			
		}
		
	}
	
	public static void actualizarListaUsuarios(){
		
		//TODO
	
		
	}
	
	public static void conexion(){
		
		try{
			
			socketConexion = new Socket(ipServidor, puerto);
			
			salida = new PrintWriter(socketConexion.getOutputStream(), true);
			entrada = new Scanner(socketConexion.getInputStream());
			salida.println(nickUsuario);
			
		}catch(UnknownHostException ex){
			
			//Mensaje de error
			ex.printStackTrace();
			
		} catch (IOException ex) {
			
			JOptionPane.showMessageDialog(null, ("ERROR:NO HAY NINGÚN SERVIDOR EJECUTANDOSE EN LA IP: " + ipServidor + "."),
					"Error de conexión",JOptionPane.ERROR_MESSAGE);
			
			ex.printStackTrace();
			System.exit(0);
		}
		
	}
	
	public static void mostrarMensaje(String linea){
		//TODO
	}
	
	public void enviarMensaje(){
		
		mensaje = tfMensajes.getText();
		System.out.println(mensaje);
		
		if(mensaje.trim().equals(""))
			JOptionPane.showMessageDialog(null, "No se pueden enviar mensajes vacios","Mensaje vacio",JOptionPane.ERROR_MESSAGE);
		
		else{
			
			tfMensajes.setText("");
			salida.println(Broadcast.BROADCAST_MENSAJE_IO);
			salida.println(nickUsuario);
			//fecha
			salida.println(mensaje );
			//salida.println("\n");
			
		}
		
	}
	

}
