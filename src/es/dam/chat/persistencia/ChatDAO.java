package es.dam.chat.persistencia;

import java.sql.SQLException;

import es.dam.chat.modelo.Chat;

/**
 *Esta clase es la encargada de gestionar las consultas SQL relacionadas con la clase Chat
 * 
 * @author Hernan Darío Villamil y Elizabeth Gordon
 * @version 1.0
 * @since 21/02/2016
 */
public class ChatDAO {
	
	private String sqlStatement;
	private ConexionSQL conexion;
	private int inserts;
	private int nuevo_id;
	
	/**
	 * Este metodo insertará un objeto de tipo Chat en la base de datos ro_tom_chat
	 * 
	 * @param chat objeto del tipo Chat
	 * @return entero con el id del último objeto guardado en la base de datos
	 * @see es.dam.chat.modelo.Chat
	 */
	public int insert(Chat chat){
		
		//String con la consulta SQL
		sqlStatement = "INSERT INTO "
				+ Chat.TABLE 
				+ "(" + Chat.KEY_IP + ", "
				+ Chat.KEY_PUERTO + ")"
				+" VALUES(?,?)";
		
		//Objeto de tipo ConexionSQL para inciiar una conexión con la base de datos
		conexion = new ConexionSQL();
		conexion.abrirConexion(sqlStatement);
		
		try{
			
			//Se asignan los valores al preparedStatement
			conexion.sentencia.setString(1, chat.getIp());
			conexion.sentencia.setInt(2, chat.getPuerto());
			
			inserts = conexion.sentencia.executeUpdate(); //Ejecución de la consulta
			
			conexion.resultadoConsulta = conexion.sentencia.getGeneratedKeys();
			
			nuevo_id = 0;
			
			while(conexion.resultadoConsulta.next())
				nuevo_id = conexion.resultadoConsulta.getInt(1); //Se guarda el último id añadido a la base de datos
			
			
			conexion.cerrarConexion(); //Se cierra la conexíon
			
			return nuevo_id; //Sí todo ha ido bien, devuelve el último id añadido a la base de datos.
			
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0; //Si ocurre algún error se devuelve 0 para indicar el error.
		
		
	}

}
