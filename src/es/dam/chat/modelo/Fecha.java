package es.dam.chat.modelo;

import java.util.GregorianCalendar;

public class Fecha {
	
	private GregorianCalendar fecha;
	
	public Fecha(){
		
		this.fecha = new GregorianCalendar();
		
	}

	@Override
	public String toString() {
		
		String fechaConFormato ="";
		
		return fechaConFormato.format("%te %tB del %tY a las %tl:%tM %tp%n", fecha, fecha, fecha, fecha, fecha, fecha);
	}
	
	public static void main(String[] args){
		
		Fecha f = new Fecha();
		
		System.out.println(f.toString());
		
	}
	
	

}
