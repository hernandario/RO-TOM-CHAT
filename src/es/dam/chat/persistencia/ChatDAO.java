package es.dam.chat.persistencia;

import java.sql.SQLException;

import es.dam.chat.modelo.Chat;

public class ChatDAO {
	
	private String sqlStatement;
	private ConexionSQL conexion;
	private int inserts;
	private int nuevo_id;
	
	public int insert(Chat chat){
		
		sqlStatement = "INSERT INTO "
				+ Chat.TABLE + " VALUES(?,?)";
		
		conexion = new ConexionSQL();
		conexion.abrirConexion(sqlStatement);
		
		try{
			
			conexion.sentencia.setString(1, chat.getIp());
			conexion.sentencia.setInt(2, chat.getPuerto());
			
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
