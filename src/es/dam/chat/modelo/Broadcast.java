package es.dam.chat.modelo;

/**
 * Clase con los atributos enteros para permitir la comunicación entre el servidor y los clientes
 * 
 * @author Hernán Darío Villamil y Elizabeth Gordon
 * @version 1.0
 * @since 21/02/2016
 */

public class Broadcast {
	
	public static final int BROADCAST_MENSAJE_ACTUALIZAR_LISTA_USUARIOS = 1;
	public static final int BROADCAST_MENSAJE_IO = 2;
	public static final int BROADCAST_MENSAJE_BORRAR_USUARIO = 3;
	public static final int BROADCAST_MENSAJE_ERROR_SERVIDOR = 4;
	public static final int BROADCAST_MENSAJE_ERROR_USUARIO = 5;

}
