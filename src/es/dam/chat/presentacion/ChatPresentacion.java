package es.dam.chat.presentacion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;

import es.dam.chat.modelo.Broadcast;
import es.dam.chat.modelo.Chat;

public class ChatPresentacion extends JFrame {
	
	private static final int THEME_USABILICHAT = 1;
	private static final int THEME_WHACHAT = 2;
	
	private static Socket socketConexion;
	private static String ipServidor;
	private static int puerto = 1988;
	
	private static PrintWriter salida;
	private static Scanner entrada;

	private static String numeroUsuariosConectados = "Usuarios conectados ( )";
	private static String nickUsuario;
	private static String mensaje;
	
	public static ArrayList<String> lista_usuarios = new ArrayList<String>();

	private static JTabbedPane panelTabs;
	
	private JPanel panelLista_usuarios;
	private JPanel panelEnviar;
	private JPanel panelTitulo;
	private JPanel panelChat;
	
	private static JList jlLista_usuarios;
	private static JTextField tfMensajes;
	private static JLabel lblNickUsuario;
	private static JButton btnEnviar;
	private static JTextArea taChat;
	private static JLabel lblIp;
	private static JLabel lblPista;
	
	private BufferedImage biUsuario;
	private JLabel lblImagenUsuario;
	
	private JRadioButtonMenuItem rbmiUsabilichat;
	private JRadioButtonMenuItem rbmiWhachat; 
	
	
	private JMenuItem miSalir;
	private JMenuBar menuBar;
	private JMenu menu;
	
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
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menu = new JMenu("Opciones");
		menuBar.add(menu);
		
		ButtonGroup bgTemas = new ButtonGroup();
		
		rbmiWhachat = new JRadioButtonMenuItem("Whachat");
		bgTemas.add(rbmiWhachat);
		menu.add(rbmiWhachat);
		
		rbmiUsabilichat = new JRadioButtonMenuItem("Usabilichat");
		bgTemas.add(rbmiUsabilichat);
		menu.add(rbmiUsabilichat);
		
		menu.addSeparator();
		
		miSalir = new JMenuItem("Salir");
		menu.add(miSalir);
		
		miSalir.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
		});
		
		rbmiWhachat.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				cambiarTema(THEME_WHACHAT);
				
			}
			
		});
		
		/*
		rbmiWhachat.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				cambiarTema(THEME_WHACHAT);
			}
			
		});
		*/
		
		rbmiUsabilichat.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				cambiarTema(THEME_USABILICHAT);
				
			}
			
		});
		
		
		rbmiWhachat.setSelected(true);
		
		lblNickUsuario = new JLabel();
		lblIp = new JLabel();
		lblPista = new JLabel("Escribe aquí un mensaje...");
		
		try {
			biUsuario = ImageIO.read(new File("." + File.separator + "img" + File.separator + "user.png"));
			lblImagenUsuario = new JLabel(new ImageIcon(biUsuario));
			
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
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
		tfMensajes.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				enviarMensaje();
			}
			
		});
		
		jlLista_usuarios = new JList();
		try {
			
			File f = new File("." + File.separator + "img" + File.separator+ "send.jpg");
			
			BufferedImage biEnviar = ImageIO.read(f);
		
			btnEnviar = new JButton(new ImageIcon(biEnviar));
			btnEnviar.setBorder(BorderFactory.createEmptyBorder());
			btnEnviar.setContentAreaFilled(false);
			btnEnviar.setBounds(350, 10, 50, 25);
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		btnEnviar.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				enviarMensaje();
				
			}
			
		});
		
		panelTabs = new JTabbedPane();
		
		panelEnviar = new JPanel();
		panelEnviar.setLayout(new BorderLayout());
		panelEnviar.add(lblPista, BorderLayout.NORTH);
		panelEnviar.add(tfMensajes, BorderLayout.CENTER);
		panelEnviar.add(btnEnviar, BorderLayout.EAST);
		
		panelTitulo = new JPanel();
		panelTitulo.setLayout(new BorderLayout());
		JPanel flowPanel = new JPanel();
		flowPanel.setLayout(new FlowLayout());
		flowPanel.add(lblNickUsuario);
		flowPanel.add(lblIp);
		panelTitulo.add(lblImagenUsuario, BorderLayout.WEST);
		panelTitulo.add(flowPanel, BorderLayout.CENTER);
		
		
		panelChat = new JPanel();
		panelChat.setLayout(new BorderLayout());
		//panelChat.add(panelTitulo, BorderLayout.NORTH);
		panelChat.add(new JScrollPane(taChat), BorderLayout.CENTER);
		panelChat.add(panelEnviar, BorderLayout.SOUTH);
		
		panelLista_usuarios = new JPanel();
		panelLista_usuarios.setLayout(new BorderLayout());
		panelLista_usuarios.add(jlLista_usuarios, BorderLayout.CENTER);
		
		panelTabs.add("Conversacion", panelChat);
		panelTabs.add(numeroUsuariosConectados, panelLista_usuarios);
		
		
		setLayout(new BorderLayout());
		add(panelTitulo, BorderLayout.NORTH);
		add(panelTabs, BorderLayout.CENTER);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 720);
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
					
				}catch(IOException ex){
					
					//Mensaje error
					ex.printStackTrace();
					
				}
				finally{
					System.exit(0);
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
		lblNickUsuario.setText("Hola " + nickUsuario); 
		lblIp.setText("\tEstas conectado onectado a: " + ipServidor);
	
		
	}
	
	
	public static void pedirIp(){
		
		String ip = "";
		
		do{
			
			ip = JOptionPane.showInputDialog("Introduzca la IP del servidor al que se quiere conectar: ");
			
			if(ip == null){
				System.exit(0);
			}
			
			if(ip.trim().equals("")){
				JOptionPane.showMessageDialog(null, "No puede introducir una IP vacia","IP Vacia",
						JOptionPane.ERROR_MESSAGE);
			}
			
			if(!Chat.comprobarIP(ip)){
				JOptionPane.showMessageDialog(null, "introduzca una ip valida (xxx.xxx.xxx.xxx) o intententelo con \"localhost\"","IP Erronea",
						JOptionPane.ERROR_MESSAGE);
			}
			
		}
		while(ip.trim().equals("") || !Chat.comprobarIP(ip));
		
		
		ipServidor = ip;
		
	}
	
	public static void pedirNick(){
		
		String nick = "";
		
		do{
			
			nick = JOptionPane.showInputDialog("Introduzca su nick:");
			
			if(nick == null)
				System.exit(0);
			
			if(nick.trim().equals("")){
				JOptionPane.showMessageDialog(null, "No puede introducir un nick vacio","Nick Vacio",
						JOptionPane.ERROR_MESSAGE);

			}
			
			if(!Chat.comprobarNick(nick)){
				JOptionPane.showMessageDialog(null, "El nick debe tener al menos 3 caracteres alfadecimales","Nick erroneo",
						JOptionPane.ERROR_MESSAGE);

			}
			
		}
		while(nick.trim().equals("") || ! Chat.comprobarNick(nick));
		
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

					actualizarListaUsuarios();
					
				}
				
				break;
				
			case Broadcast.BROADCAST_MENSAJE_IO:
				
				mensaje = entrada.nextLine();
				taChat.append("\n");
				taChat.append(mensaje);
				taChat.append("\n");
				
				
				
				break;
				
			case Broadcast.BROADCAST_MENSAJE_BORRAR_USUARIO:
				
				String usuario = entrada.nextLine();
				
				lista_usuarios.remove(lista_usuarios.indexOf(usuario));
				//taChat.append(usuario + " ha abandonado el chat");
				actualizarListaUsuarios();
				
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
				System.exit(0);
				
				break;
			
			
			}
			
		}
		
	}
	
	public static void cambiarTema(int tema){
		
		Font fuenteUsabilichat = new Font("Fuente usable", Font.BOLD, 20);
		Font fuenteWhachat = new Font("Fuente chat", Font.PLAIN, 14);
		
		if(tema == THEME_WHACHAT){
			
			System.out.println("whachat");
			jlLista_usuarios.setBackground(Color.WHITE);
			jlLista_usuarios.setFont(fuenteWhachat);
			jlLista_usuarios.setForeground(Color.BLACK);
			
			taChat.setBackground(Color.WHITE);
			taChat.setFont(fuenteWhachat);
			taChat.setForeground(Color.BLACK);
			
			tfMensajes.setBackground(Color.WHITE);
			tfMensajes.setFont(fuenteWhachat);
			tfMensajes.setForeground(Color.BLACK);
			
			lblPista.setFont(fuenteWhachat);
			lblPista.setForeground(Color.BLACK);
			
			lblNickUsuario.setFont(fuenteWhachat);
			lblNickUsuario.setForeground(Color.BLACK);
			
			lblIp.setFont(fuenteWhachat);
			lblIp.setForeground(Color.BLACK);
			
						
		}
		
		if(tema == THEME_USABILICHAT){
			
			System.out.println("usabilichat");
			jlLista_usuarios.setBackground(Color.BLACK);
			jlLista_usuarios.setFont(fuenteUsabilichat);
			jlLista_usuarios.setForeground(Color.WHITE);
			
			taChat.setBackground(Color.BLACK);
			taChat.setFont(fuenteUsabilichat);
			taChat.setForeground(Color.WHITE);
			
			tfMensajes.setBackground(Color.BLACK);
			tfMensajes.setFont(fuenteUsabilichat);
			tfMensajes.setForeground(Color.WHITE);
			
			lblPista.setFont(fuenteUsabilichat);
			lblPista.setForeground(Color.BLUE);
			
			lblNickUsuario.setFont(fuenteUsabilichat);
			lblNickUsuario.setForeground(Color.BLUE);
			
			lblIp.setFont(fuenteUsabilichat);
			lblIp.setForeground(Color.BLUE);

		}
		
	}
	
	public static void actualizarListaUsuarios(){
		
		
		numeroUsuariosConectados = "Usuarios conectados (" + lista_usuarios.size() +")";
		panelTabs.setTitleAt(1, numeroUsuariosConectados);
		
		
		for(int i = 0; i < lista_usuarios.size(); i++){
			
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					jlLista_usuarios.setModel(new AbstractListModel(){

						@Override
						public int getSize() {
							// TODO Auto-generated method stub
							return lista_usuarios.size();
						}

						@Override
						public Object getElementAt(int index) {
							// TODO Auto-generated method stub
							return lista_usuarios.get(index);
						}
						
					});
					
				}
				
			});
			
			
			
		}
	
		
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
			System.exit(0);
			
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
			
			//TODO que lo que escriba el usuario aparezca a la derecha
			
			JTextField tfMensaje = new JTextField(mensaje);
			
			taChat.append("\n");
			taChat.append("yo: " + mensaje);
			taChat.append("\n");
			
			
			tfMensajes.setText("");
			salida.println(Broadcast.BROADCAST_MENSAJE_IO);
			salida.println(nickUsuario);
			//fecha
			salida.println(mensaje );
			//salida.println("\n");
			
		}
		
	}
	

}
