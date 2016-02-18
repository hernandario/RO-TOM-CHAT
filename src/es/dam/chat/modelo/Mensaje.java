package es.dam.chat.modelo;

import java.util.Date;

public class Mensaje {
	
	public static final String TABLE = "mensaje";
	
	public static final String KEY_ID = "id";
	public static final String KEY_NICK = "nick";
	public static final String KEY_MENSAJE = "mensaje";
	public static final String KEY_FECHA = "fecha";
	public static final String KEY_ID_CHAT = "id_chat";

	private int id;
	private String nick;
	private String mensaje;
	private Date fecha;
	private int id_chat;
	
	public Mensaje(String nick, String mensaje, Date fecha, int id_chat){
		
		this.nick = nick;
		this.mensaje = mensaje;
		this.fecha = fecha;
		this.id_chat = id_chat;
		
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public int getId_chat() {
		return id_chat;
	}
	public void setId_chat(int id_chat) {
		this.id_chat = id_chat;
	}
	
	
	
	

}
