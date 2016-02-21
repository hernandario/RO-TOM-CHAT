package es.dam.chat.modelo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat {
	
	public static final String TABLE = "chat";
	
	public static final String KEY_ID = "id";
	public static final String KEY_IP = "ip";
	public static final String KEY_PUERTO = "puerto";
	
	private int id;
	private String ip;
	private int puerto;
	
	
	
	
	public Chat(String ip, int puerto) {
		super();
		this.ip = ip;
		this.puerto = puerto;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPuerto() {
		return puerto;
	}
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	
	public static boolean comprobarIP(String ip){
		boolean correcto = false;
		
		Pattern patron = Pattern.compile("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
		Matcher coincidencia = patron.matcher(ip);
		
		if(coincidencia.matches() || ip.equals("localhost"))
			correcto = true;
		
		else
			correcto = false;
			
		
		
		return correcto;
	}
	
	public static boolean comprobarNick(String nick){
		boolean correcto = false;
		
		Pattern patron = Pattern.compile("^[a-zA-Z0-9._-]{3,}$");
		Matcher coincidencia = patron.matcher(nick);
		
		if(coincidencia.matches())
			correcto = true;
		
		else
			correcto = false;
		
		
		return correcto;
	}
	

}
