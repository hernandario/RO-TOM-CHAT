package es.dam.chat.persistencia;

import java.sql.SQLException;
import java.util.Date;

import es.dam.chat.modelo.Mensaje;

/**
 *Esta clase es la encargada de gestionar las consultas SQL relacionadas con la clase Mensaje
 * 
 * @author Hernan Darío Villamil y Elizabeth Gordon
 * @version 1.0
 * @since 21/02/2016
 */
public class MensajeDAO {
	
	private String sqlStatement;
	private ConexionSQL conexion;
	private int inserts;
	private int nuevo_id;
	
	/**
	 * Este metodo insertará un objeto de tipo Mensaje en la base de datos ro_tom_chat
	 * 
	 * @param mensaje objeto del tipo Mensaje
	 * @return entero con el id del último objeto guardado en la base de datos
	 * @see es.dam.chat.modelo.Mensaje
	 */
	public int insert(Mensaje mensaje){
		
		//String con la consulta SQL
		sqlStatement = "INSERT INTO "
				+ Mensaje.TABLE 
				+ "(" + Mensaje.KEY_NICK + ", "
				+ Mensaje.KEY_MENSAJE + ", "
				+ Mensaje.KEY_FECHA + ", "
				+ Mensaje.KEY_ID_CHAT +")"
				+" VALUES(?, ?, ?, ?)";
		
		//Objeto de tipo ConexionSQL para inciiar una conexión con la base de datos
		conexion = new ConexionSQL();
		conexion.abrirConexion(sqlStatement);
		
		
		try{
			
			//Se asignan los valores al preparedStatement
			conexion.sentencia.setString(1, mensaje.getNick());
			conexion.sentencia.setString(2, mensaje.getMensaje());
			
			java.sql.Date fecha = (java.sql.Date) new java.sql.Date(mensaje.getFecha().getTime());
			conexion.sentencia.setDate(3, fecha);
			
			conexion.sentencia.setInt(4, mensaje.getId_chat());
	
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
