package es.dam.chat.modelo;

import java.util.Date;

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
	
	public String obtenerHora(){
		
		String horaConFormato ="";
		
		return horaConFormato.format("%tl:%tM %tp%n", fecha, fecha, fecha);
		
	}
	
	public String obtenerDia(){
		
		String diaConFormato ="";
		
		return diaConFormato.format("%te %tB del %tY ", fecha, fecha, fecha);
	}
	
	public static void main(String[] args){
		
		Fecha f = new Fecha();
		
		System.out.println(f.toString());
		System.out.println(f.obtenerDia());
		System.out.println(f.obtenerHora());
		
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	
	

}
