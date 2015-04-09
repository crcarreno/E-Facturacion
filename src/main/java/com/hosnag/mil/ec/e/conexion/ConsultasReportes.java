/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hosnag.mil.ec.e.conexion;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ccarreno
 */
public class ConsultasReportes {
    
      public byte[] getFileByteBD(String identificacion_comprobador, String cod_doc, String localidad, String puntoEmision, String numeroFactura, int intEmpresa, int anio){
        
        Connection conn = Connect.conectar();
        
        byte[] imgBytes = null;
        
        try{
        
        PreparedStatement ps = conn.prepareStatement(" select archivo_pdf " +
                                " from publico.factbt_cabecera " +
                                " where localidad = ? " + 
                                " and punto_de_emision = ? " +
                                " and numero_factura = ? " + 
                                " and identificacion_comprobador = ?" +
                                " and empresa = ? " +
                                " and cod_doc = ? " + 
                                " and anio_factura = ? ");
        
        ps.setString(1, localidad);
        ps.setString(2, puntoEmision);
        ps.setString(3, numeroFactura);
        ps.setString(4, identificacion_comprobador);
        ps.setInt(5, intEmpresa);
        ps.setString(6, cod_doc);
        ps.setInt(7,anio);
        
        ResultSet rs = ps.executeQuery();
     
        while (rs.next()) {    
            imgBytes = rs.getBytes(1);    
    }
                
        rs.close();
        ps.close();
        conn.close();
        
        return imgBytes;
        
        }catch(SQLException e){
                Notification.show("Error", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                return null;
        }
    }
      
      public  ComboBox llenarCmbEmpresa(){
		
		ComboBox cmb = new ComboBox();
		              
            PreparedStatement ps = null;
	    ResultSet rs = null;
	            
	    Connection conn = Connect.conectar();

	      String sql =  "SELECT distinct empresa, razon_social FROM publico.factbt_cabecera;"; 
	    		  
	   try{                    
	       ps = conn.prepareStatement(sql);
	       rs = ps.executeQuery();
	    	
	       while(rs.next()){          	
	    	   cmb.addItem(rs.getString("empresa"));
	    	   cmb.setItemCaption(rs.getString("empresa"), rs.getString("razon_social").trim());
	       }
	       
	    conn.close();
	    	        
	    }catch(Exception e){
	    	        Notification.show("Error", e.getMessage(), Notification.Type.ERROR_MESSAGE);
	    	        }

	     return cmb;
	}    
      
      
      public  ComboBox llenarCmbAnio(){
		
		ComboBox cmb = new ComboBox();
		              
            PreparedStatement ps = null;
	    ResultSet rs = null;
	            
	    Connection conn = Connect.conectar();

	      String sql =  "SELECT distinct anio_factura FROM publico.factbt_cabecera;"; 
	    		  
	   try{                    
	       ps = conn.prepareStatement(sql);
	       rs = ps.executeQuery();
	    	
	       while(rs.next()){          	
	    	   cmb.addItem(rs.getString("anio_factura"));
	    	   //cmb.setItemCaption(rs.getString("empresa"), rs.getString("razon_social").trim());
	       }
	       
	    conn.close();
	    	        
	    }catch(Exception e){
	    	        Notification.show("Error", e.getMessage(), Notification.Type.ERROR_MESSAGE);
	    	        }

	     return cmb;
	}    
      
      
       public void visitorsPrint(String cedula, 
                                 int empresa, 
                                 String punto_emision, 
                                 String localidad, 
                                 String num_factura){
        
        Connection conn = Connect.conectar();
               
        try{
        
   String sql = "{ call publico.funcion_visitor_print(?,?,?,?,?) }";

        System.out.println("SQL: " + sql);
        
        CallableStatement cs = conn.prepareCall(sql);
        
        cs.setString(1, cedula);
        cs.setInt(2, empresa);
        cs.setString(3, punto_emision);
        cs.setString(4, localidad);
        cs.setString(5, num_factura);  
        
        if(cs.execute())
            System.out.println("Grabado satisfactoriamente...");
        else
            System.out.println("No se ejecuto correctamente...");
        
        cs.close();
        conn.close();
        
            System.out.println("Finalizado...");

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
       
        public String getVisitors(){
			              
            PreparedStatement ps = null;
	    ResultSet rs = null;
	            
	    Connection conn = Connect.conectar();

	      String sql =  "SELECT max(id) as cont FROM publico.factbt_visitors;"; 
	    	
              String visitors = null;
              
	   try{                    
	       ps = conn.prepareStatement(sql);
	       rs = ps.executeQuery();
	    	
	       while(rs.next()){          	

                   visitors = String.valueOf(rs.getInt("cont"));

	       }
	       
	    conn.close();
	    	        
	    }catch(Exception e){
	    	        Notification.show("Error", e.getMessage(), Notification.Type.ERROR_MESSAGE);
	    	        }

	     return visitors;
	}    
      
}
