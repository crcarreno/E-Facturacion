package com.hosnag.mil.ec.e.facturacion;

import com.hosnag.mil.ec.e.utilidades.ViewPDF;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.hosnag.mil.ec.e.conexion.ConsultasReportes;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.themes.Reindeer;
import java.io.File;

public class Frm_display_comprobante{
 
	private AbsoluteLayout mainLayout;
	private Label lblCentMedico, lblNoFactura, lblCedula, lblMensaje, lblTipDoc, lblAnio, lblVisitors;
	private TextField txtLocalidad, txtPuntoEmision, txtNoComprobante, txtCedula;
	private Button btnIniciar, btnCancelar, btnAyuda;
	private Window WinLogon;
	private Link lnkAyuda;
        private ComboBox comboEmpresa, comboAnio;
        private OptionGroup group;
        private Link linkAyuda;
	//private Consultas cons;	
	private Embedded icon;
        private int tipoCedulaIssfa = 0;
        
        private ConsultasReportes cons;
	//Cedula = 0
	//ISSFA = 1
	//Contrase�a no coincide con el �ltimo examen = 2
	
public Window subWinLogon(){
		
	WinLogon = new Window("App - E-Facturación Ver. 1.0");
	WinLogon.setContent(buildMainLayout());
	        
		return WinLogon;
}
			
	private AbsoluteLayout buildMainLayout() {
			
            cons = new ConsultasReportes();       
            
		WinLogon.center();
		WinLogon.setResizable(false);
		WinLogon.setDraggable(false);
		WinLogon.setWidth("360px");
		WinLogon.setHeight("420px");
		WinLogon.setModal(true);
		WinLogon.setClosable(false);	
	
		
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("350px");
		mainLayout.setHeight("370px");
				
					
		// btnIniciar
		btnIniciar = new Button();
		btnIniciar.setCaption("Ver Comprobante");
		btnIniciar.setImmediate(false);
		btnIniciar.setWidth("160px");
		btnIniciar.setHeight("-1px");
		btnIniciar.setClickShortcut(KeyCode.ENTER);
                btnIniciar.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {	
                 
                String identificacion_comprobador = txtCedula.getValue().trim();
                String localidad = txtLocalidad.getValue().trim();
                String puntoEmision = txtPuntoEmision.getValue().trim();
                String noComprobante = txtNoComprobante.getValue().trim();
                Object cmbEmpresa = comboEmpresa.getValue();
                String cmbAnio = comboAnio.getValue().toString();
                                
                String cod_doc = "";
                
                if(group.getValue().equals("Factura"))
                cod_doc = "01";
                else if(group.getValue().equals("Nota de Crédito"))
                cod_doc = "04";                
                                
                if(!identificacion_comprobador.equals("") &&
                    !localidad.equals("") &&
                    !puntoEmision.equals("") &&
                    !noComprobante.equals("") &&
                     cmbEmpresa != null &&
                    !cmbAnio.equals("")){

            StreamResource resource = null;
                    int intEmpresa = Integer.parseInt(cmbEmpresa.toString());
        try {     
            
            resource = new StreamResource( new ViewPDF(identificacion_comprobador, cod_doc, localidad, puntoEmision, noComprobante, intEmpresa, Integer.parseInt(cmbAnio)), 
            "Factura_"+localidad+"-"+puntoEmision+"-"+noComprobante+".pdf");
                
            int size = resource.getStreamSource().getStream().available();
                    
            cons.visitorsPrint(identificacion_comprobador,
                                intEmpresa,
                                puntoEmision, 
                                localidad, 
                                noComprobante);
            
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Frm_display_comprobante.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            if(size > 100){

                    resource.setMIMEType("application/pdf");
                    
                    Page.getCurrent().open(resource,"_blank", false);	
                    
                txtCedula.setValue("");
                txtLocalidad.setValue("");
                txtPuntoEmision.setValue("");
                txtNoComprobante.setValue("");
                    
            }else{
                Notification.show("Atención", "No encontró ningún comprobante. ", Notification.Type.WARNING_MESSAGE);
            }   
            
            } catch (IOException ex) {
                Notification.show("Error", ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            }

        //window.addComponent(e);
        //getMainWindow().addWindow(window);                    
	}else{
            Notification.show("Atención", "Ningún campo puede quedar vacio.", Notification.Type.WARNING_MESSAGE);   
                }
              }                
            });
                
		mainLayout.addComponent(btnIniciar, "top:295.0px;left:180.0px;");
		
		// btnRegistrar
		btnCancelar = new Button();
		btnCancelar.setCaption("Cancelar");
		btnCancelar.setImmediate(false);
		btnCancelar.setWidth("155px");
		btnCancelar.setHeight("-1px");
		btnCancelar.addClickListener(new ClickListener() {
		public void buttonClick(ClickEvent event) {
		WinLogon.getUI().getPage().open("http://www.hosnag.armada.mil.ec", "_parent");		
			}});
		mainLayout.addComponent(btnCancelar, "top:295.0px;left:15.0px;");
		
		// lblAyuda
		btnAyuda = new Button();
		btnAyuda.setImmediate(true);
		btnAyuda.setWidth("-1px");
		btnAyuda.setHeight("-1px");
		btnAyuda.setCaption("Ayuda");
                btnAyuda.addStyleName(Reindeer.BUTTON_LINK);
                //btnAyuda.setContentMode(ContentMode.HTML);
		btnAyuda.addClickListener(new ClickListener() {
                    public void buttonClick(ClickEvent event) {
                       
                        String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
                         
                       System.out.println("RUTA: " + basepath + "/ayuda.pdf");
                        
                    Window window = new Window();
                    window.setModal(true);
                    window.setResizable(false);
                    window.setWidth("800");
                    window.setHeight("600");
                    window.center();

                File pdfFile = new File(basepath + "/ayuda.pdf");
                
                Embedded pdf = new Embedded("", new FileResource(pdfFile));
                pdf.setMimeType("application/pdf");
                pdf.setType(Embedded.TYPE_BROWSER);
                pdf.setSizeFull();

                    window.setContent(pdf);
                    WinLogon.getUI().addWindow(window);
                        
			}});
                mainLayout.addComponent(btnAyuda, "top:335.0px;left:250.0px;");
		
                // lblVisitors
		lblVisitors = new Label();
		lblVisitors.setImmediate(true);
		lblVisitors.setWidth("-1px");
		lblVisitors.setHeight("-1px");
		lblVisitors.setValue("Visitantes: " + cons.getVisitors());
                lblVisitors.setContentMode(ContentMode.HTML);
		mainLayout.addComponent(lblVisitors, "top:345.0px;left:15.0px;");
                
                // lblISSFA
		lblCedula = new Label();
		lblCedula.setImmediate(true);
		lblCedula.setWidth("-1px");
		lblCedula.setHeight("-1px");
		lblCedula.setValue("Cédula");
		mainLayout.addComponent(lblCedula, "top:175.0px;left:15.0px;");
                
		// lblNoFactura
		lblNoFactura = new Label();
		lblNoFactura.setImmediate(true);
		lblNoFactura.setWidth("-1px");
		lblNoFactura.setHeight("-1px");
		lblNoFactura.setValue("No. Comp.");
		mainLayout.addComponent(lblNoFactura, "top:230.0px;left:15.0px;");
		
		// textField_1
		txtCedula = new TextField();
		txtCedula.setImmediate(true);
		txtCedula.setWidth("215px");
		txtCedula.setHeight("-1px");
		txtCedula.setMaxLength(10);
		txtCedula.setInputPrompt("Número de Cédula");
                //txtCedula.setValue("0903410298");
		mainLayout.addComponent(txtCedula, "top:175.0px;left:120.0px;");
		
                // textField_Localidad
		txtLocalidad = new TextField();
		txtLocalidad.setImmediate(true);
		txtLocalidad.setWidth("50px");
		txtLocalidad.setHeight("-1px");
		txtLocalidad.setMaxLength(3);
		txtLocalidad.setInputPrompt("000");
                //txtLocalidad.setValue("001");
                mainLayout.addComponent(txtLocalidad, "top:230.0px;left:120.0px;");
                              
                // textField_Punto_emision
		txtPuntoEmision = new TextField();
		txtPuntoEmision.setImmediate(true);
		txtPuntoEmision.setWidth("50px");
		txtPuntoEmision.setHeight("-1px");
		txtPuntoEmision.setMaxLength(3);
		txtPuntoEmision.setInputPrompt("000");
                //txtPuntoEmision.setValue("001");
                mainLayout.addComponent(txtPuntoEmision, "top:230.0px;left:175.0px;");
                                
		// textField_Comprobante
		txtNoComprobante = new TextField();
		txtNoComprobante.setImmediate(true);
		txtNoComprobante.setWidth("105px");
		txtNoComprobante.setHeight("-1px");
		txtNoComprobante.setMaxLength(9);
		txtNoComprobante.setInputPrompt("000000000");
                //txtNoComprobante.setValue("000000031");
		mainLayout.addComponent(txtNoComprobante, "top:230.0px;left:230.0px;");
				
		// Link Ayuda
		lnkAyuda = new Link("Ayuda: Dónde busco mi No. Comprobante?",
                new ThemeResource("../popups/popup_html.html"));
		lnkAyuda.setTargetName("_blank");
		lnkAyuda.setTargetBorder(Link.TARGET_BORDER_NONE);
		lnkAyuda.setTargetHeight(435);
		lnkAyuda.setTargetWidth(835);
		//mainLayout.addComponent(lnkAyuda, "top:225.0px;left:15.0px;");
		
		// lblCentMedico
		lblCentMedico = new Label();
		lblCentMedico.setImmediate(true);
		lblCentMedico.setWidth("-1px");
		lblCentMedico.setHeight("-1px");
		lblCentMedico.setValue("Cent. Médico");
		mainLayout.addComponent(lblCentMedico, "top:15.0px;left:15.0px;");
		
                comboEmpresa = new ComboBox();
                comboEmpresa = cons.llenarCmbEmpresa();
                comboEmpresa.setImmediate(true);
                comboEmpresa.setWidth("215px");
		comboEmpresa.setHeight("-1px");
                comboEmpresa.setNullSelectionAllowed(false);
                mainLayout.addComponent(comboEmpresa, "top:10.0px;left:120.0px;");
                                
                // Año
		lblAnio = new Label();
		lblAnio.setImmediate(true);
		lblAnio.setWidth("-1px");
		lblAnio.setHeight("-1px");
		lblAnio.setValue("Año");
		mainLayout.addComponent(lblAnio, "top:70.0px;left:15.0px;");
                
                comboAnio = new ComboBox();
                comboAnio = cons.llenarCmbAnio();
                comboAnio.setImmediate(true);
                comboAnio.setWidth("215px");
		comboAnio.setHeight("-1px");
                comboAnio.setNullSelectionAllowed(false);
                comboAnio.select("2015");
                mainLayout.addComponent(comboAnio, "top:65.0px;left:120.0px;");
                
      		// Documento
		lblTipDoc = new Label();
		lblTipDoc.setImmediate(true);
		lblTipDoc.setWidth("-1px");
		lblTipDoc.setHeight("-1px");
		lblTipDoc.setValue("Documento");
		mainLayout.addComponent(lblTipDoc, "top:130.0px;left:15.0px;");
                
                group = new OptionGroup();
                group.setNullSelectionAllowed(false);
                group.addItem("Factura");
                group.addItem("Nota de Crédito");
                group.select("Factura");
                mainLayout.addComponent(group, "top:110.0px;left:120.0px;");
		/*icon = new Embedded(null,new ThemeResource("../images/laboratory.png"));
		icon.setWidth("60px");
		icon.setHeight("60px");
		mainLayout.addComponent(icon, "top:0.0px;left:250.0px;");*/
		
		return mainLayout;
	}	

}

