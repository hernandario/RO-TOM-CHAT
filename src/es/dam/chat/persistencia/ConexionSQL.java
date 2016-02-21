package es.dam.chat.persistencia;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

/**
 *Esta clase es la encargada de gestionar las conexiones entre la base de datos ro_tom_chat y la aplicación.
 * 
 * @author Hernan Darío Villamil y Elizabeth Gordon
 * @version 1.0
 * @since 21/02/2016
 */
public class ConexionSQL {
	
	private String maquina = "localhost";
	private String bbdd = "ro_tom_chat";
	private String usuario = "root";
	private String pass = "";
	public static  Connection conexion;
	public static PreparedStatement sentencia;
	public static ResultSet resultadoConsulta;
	
	
	/**
	 * Este metodo abre una conexión entre la base de datos y la aplicación
	 * 
	 * @param sqlStatement contiene la conculta SQL
	 */
	public void abrirConexion(String sqlStatement){
		
		//Se abre la conexión
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			
			conexion = (Connection) DriverManager.getConnection("jdbc:mysql://" + maquina + "/" + bbdd, usuario, pass);
			
			sentencia = conexion.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
			
		}catch (SQLException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			
		}
		catch (ClassNotFoundException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * Este metodo abre una conexión entre la base de datos y la aplicación
	 */
	public static void cerrarConexion(){
		
		//Se cierra la conexión
		try {
			
			if(!(resultadoConsulta == null))
				resultadoConsulta.close();
			
			sentencia.close();
			conexion.close();
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		
	}


}
