package es.dam.chat.modelo;

public class Chat {
	
	public static final String TABLE = "chat";
	
	public static final String KEY_ID = "id";
	public static final String KEY_IP = "ip";
	public static final String KEY_PUERTO = "puerto";
	
	private int id;
	private String ip;
	private int puerto;
	
	
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
	
	

}
