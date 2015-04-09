/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hosnag.mil.ec.e.utilidades;

import com.hosnag.mil.ec.e.conexion.ConsultasReportes;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Notification;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ViewPDF implements StreamSource{
    
   private final ByteArrayOutputStream getOS = new ByteArrayOutputStream();
  
        public ViewPDF(String identificacion_comprobador, String cod_doc, String localidad, String puntoEmision, String numeroFactura, int cmbEmpresa, int anio) throws IOException {
                        
            ConsultasReportes report = new ConsultasReportes();
            
            byte [] file = report.getFileByteBD(identificacion_comprobador, cod_doc, localidad, puntoEmision, numeroFactura, cmbEmpresa, anio);
            
        if(file != null){
           
            ByteArrayOutputStream os = new ByteArrayOutputStream(file.length);
                   
            os.write(file, 0, (int)file.length);
            
            os.writeTo(getOS);
                        
            Document document = null;

            try {
                document = new Document(PageSize.A4, 50, 50, 50, 50);
                PdfWriter.getInstance(document, os);
                document.open();

                document.add(new Paragraph("Generado por Cristhian Carreño A.!"));
            } catch (Exception e) {
                Notification.show("Error", e.getMessage(), Notification.Type.ERROR_MESSAGE);
            } finally {
                if (document != null) {
                    document.close();
                }
            }
        }else{

            Notification.show("Atención", "Ningún campo puede quedar vacio.", Notification.Type.WARNING_MESSAGE);   
        }  
            
        }

        public InputStream getStream() {
            // Here we return the pdf contents as a byte-array
            return new ByteArrayInputStream(getOS.toByteArray());
        }  
}


/**
 *
 * @author ccarreno
 */
/*public class ViewPDF  extends HttpServlet{
     
    public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        
         OutputStream out = null;
         Conexion conn = new Conexion();
         ConsultasReportes conReport = new ConsultasReportes();
        //DB1 db = new DB1();
        //Connection conn=db.dbConnect("jdbc:jtds:sqlserver://localhost:1433/smpp","sa","");
        
        try {
                       
            response.setContentType("application/pdf");
            out = response.getOutputStream();
            byte[] b = conReport.getPDFData(conn.conectar());
            out.write(b,0,b.length);
            out.close();
            
        } catch (Exception e) {
            throw new ServletException("Exception in Excel Sample Servlet", e);
        } finally {
            if (out != null)
                out.close();
        }
    }
    
    public void doPost(HttpServletRequest request,HttpServletResponse response)throws IOException, ServletException {
        doGet(request, response);
    }
    
}*/
