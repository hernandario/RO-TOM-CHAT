package es.dam.chat.persistencia;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

public class ConexionSQL {
	
	private String maquina = "localhost";
	private String bbdd = "ro_to_chat";
	private String usuario = "root";
	private String pass = "";
	public static  Connection conexion;
	public static PreparedStatement sentencia;
	public static ResultSet resultadoConsulta;
	
	public void abrirConexion(String sqlStatement){
		
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
	
	public static void cerrarConexion(){
		
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
