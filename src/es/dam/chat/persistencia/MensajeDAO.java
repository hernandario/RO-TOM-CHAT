package es.dam.chat.persistencia;

import java.sql.SQLException;
import java.util.Date;

import es.dam.chat.modelo.Mensaje;

public class MensajeDAO {
	
	private String sqlStatement;
	private ConexionSQL conexion;
	private int inserts;
	private int nuevo_id;
	
	public int insert(Mensaje mensaje){
		
		sqlStatement = "INSERT INTO "
				+ Mensaje.TABLE 
				+ "(" + Mensaje.KEY_NICK + ", "
				+ Mensaje.KEY_MENSAJE + ", "
				+ Mensaje.KEY_FECHA + ", "
				+ Mensaje.KEY_ID_CHAT +")"
				+" VALUES(?, ?, ?, ?)";
		
		conexion = new ConexionSQL();
		conexion.abrirConexion(sqlStatement);
		
		try{
			
			conexion.sentencia.setString(1, mensaje.getNick());
			conexion.sentencia.setString(2, mensaje.getMensaje());
			
			java.sql.Date fecha = (java.sql.Date) new java.sql.Date(mensaje.getFecha().getTime());
			conexion.sentencia.setDate(3, fecha);
			
			conexion.sentencia.setInt(4, mensaje.getId_chat());
			
			
			
			inserts = conexion.sentencia.executeUpdate();
			
			conexion.resultadoConsulta = conexion.sentencia.getGeneratedKeys();
			
			nuevo_id = 0;
			
			while(conexion.resultadoConsulta.next())
				nuevo_id = conexion.resultadoConsulta.getInt(1);
			
			
			conexion.cerrarConexion();
			
			return nuevo_id;
			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
		
		
	}


}
