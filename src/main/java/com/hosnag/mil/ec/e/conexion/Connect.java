/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hosnag.mil.ec.e.conexion;

import com.vaadin.ui.Notification;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Calendar;

/**
 *
 * @author ccarreno
 */
public class Connect {
               
    public static Connection conectar(){
        		
		Calendar Cal= Calendar.getInstance(); 
		String fecha = Cal.get(Cal.DATE)+"/"+(Cal.get(Cal.MONTH)+1)+"/"+Cal.get(Cal.YEAR)+" "+Cal.get(Cal.HOUR_OF_DAY)+":"+Cal.get(Cal.MINUTE)+":"+Cal.get(Cal.SECOND); 
		
		boolean conexion = false;
		Connection con = null;        
        try{
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://10.129.100.5:5432/db_EFacturacion_","cajas", "cajas_facturacion");
            
            System.out.println("Conectado a la BD fecha: " + fecha);
            conexion = true;
            
        }catch(Exception e){
        	conexion = false;
        	Notification.show("Error", e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
        return con;
    }
    
}
