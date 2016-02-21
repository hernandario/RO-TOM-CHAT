package es.dam.chat.modelo;

import java.util.Date;

/**
 * @author Hernán Darío Villamil y Elizabeth Gordon
 * @version 1.0
 * @since 21/02/2016
 */
public class Fecha {
	
	private Date fecha;
	
	public Fecha(){
		
		this.fecha = new Date();
		
	}

	@Override
	public String toString() {
		
		String fechaConFormato ="";
		
		return fechaConFormato.format("%te %tB del %tY a las %tl:%tM %tp%n", fecha, fecha, fecha, fecha, fecha, fecha);
	}
	
	/**
	 * Este metodo formatea un objeto de tipo Date para mostrarlo de una manera más amigable al usuario
	 * @return String con la hora (hora, mes y pm/am)
	 */
	public String obtenerHora(){
		
		String horaConFormato ="";
		
		return horaConFormato.format("%tl:%tM %tp", fecha, fecha, fecha);
		
	}
	
	/**
	 * Este metodo formatea un objeto de tipo Date para mostrarlo de una manera más amigable al usuario
	 * @return String con la fecha (dia, mes y año)
	 */
	public String obtenerDia(){
		
		String diaConFormato ="";
		
		return diaConFormato.format("%te %tB del %tY ", fecha, fecha, fecha);
	}
	
	/*
	public static void main(String[] args){
		
		Fecha f = new Fecha();
		
		System.out.print(f.toString());
		System.out.print(f.obtenerDia());
		System.out.print(f.obtenerHora());
		System.out.print(f.obtenerDia());
		
	}*/

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	
	

}
