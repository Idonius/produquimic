/*
 *********************************************************************
 Objetivo: Servicio que implementa interface ArchivoService
 ********************************************************************** 
 MODIFICACIONES
 FECHA                     AUTOR             RAZON
 01-Sep-2016             D. Jácome        Emision Inicial
 ********************************************************************** 
 */
package dj.comprobantes.offline.service;

import dj.comprobantes.offline.dao.XmlComprobanteDAO;
import dj.comprobantes.offline.dto.Comprobante;
import dj.comprobantes.offline.dto.XmlComprobante;
import dj.comprobantes.offline.enums.ParametrosSistemaEnum;
import dj.comprobantes.offline.enums.TipoComprobanteEnum;
import dj.comprobantes.offline.enums.TipoImpuestoIvaEnum;
import dj.comprobantes.offline.exception.GenericException;
import dj.comprobantes.offline.reporte.DetalleReporte;
import dj.comprobantes.offline.reporte.GenerarReporte;
import dj.comprobantes.offline.reporte.ReporteDataSource;
import dj.comprobantes.offline.util.UtilitarioCeo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.IOUtils;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.InputSource;

/**
 *
 * @author diego.jacome
 */
@Stateless
public class ArchivoServiceImp implements ArchivoService {

    @EJB
    private XmlComprobanteDAO sriComprobanteDao;

    private final UtilitarioCeo utilitario = new UtilitarioCeo();

    @Override
    public byte[] getXml(Comprobante comprobante) throws GenericException {
        try {
            XmlComprobante sriComprobante = sriComprobanteDao.getSriComprobanteActual(comprobante);
            if (sriComprobante == null) {
                return null;
            }
            // Crea el archivo y escribe el XML
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new StringReader(sriComprobante.getXmlcomprobante()));
            Format format = Format.getPrettyFormat();
            format.setEncoding("ISO-8859-1"); // Por las tildes
            XMLOutputter serializerxml = new XMLOutputter(format);
            String xmlInvoice = serializerxml.outputString(doc);
            Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xmlInvoice.getBytes())));
            StreamResult res = new StreamResult(new ByteArrayOutputStream());
            serializer.transform(xmlSource, res);
            InputStream myInputStream = new ByteArrayInputStream(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());
            byte[] bpdf = IOUtils.toByteArray(myInputStream);
            myInputStream.close();
            return bpdf;
        } catch (GenericException | JDOMException | IOException | IllegalArgumentException | TransformerException e) {

            throw new GenericException("ERROR. al crear XML", e);
        }

    }

    @Override
    public byte[] getPdf(Comprobante comprobante) throws GenericException {
        XmlComprobante sriComprobante = sriComprobanteDao.getSriComprobanteActual(comprobante);
        if (sriComprobante == null) {
            return null;
        }
        String cadenaXML = sriComprobante.getXmlcomprobante();
        Map<String, Object> parametros = getParametrosComunes(cadenaXML, comprobante);
        // Detalles
        String cadenaDetalles = utilitario.getValorEtiqueta(cadenaXML, "detalles");
        // //Forma un arreglo de todos los detalles
        String strDetalles[] = cadenaDetalles.split("</detalle>");
        List<DetalleReporte> lisDetalle = new ArrayList<>();
        // Info Adicional infoAdicional
        String cadenainfoAdicional = utilitario.getValorEtiqueta(cadenaXML, "infoAdicional");
        String strcampoAdicional[] = cadenainfoAdicional.split("</campoAdicional>");
        // Crea Collection de info Adicional
        try {
            Collection<String> col_name = null;
            for (String strcampoAdicional1 : strcampoAdicional) {
                if (col_name == null) {
                    col_name = new ArrayList<>();
                }
                String strcampo = strcampoAdicional1;
                String name = strcampo.substring((strcampo.indexOf("nombre=\"") + 8), (strcampo.lastIndexOf(">") - 1));
                String valor = strcampo.substring(strcampo.lastIndexOf(">") + 1);
                col_name.add(name + " : " + valor);
            }
            parametros.put("col_name", col_name);
        } catch (Exception e) {
        }
        // Recorre todos los detalles
        String columnas[] = {"codigoPrincipal", "codigoAuxiliar", "cantidad", "descripcion", "precioUnitario", "precioTotalSinImpuesto"};
        for (String strDetalleActual : strDetalles) {
            Object valores[] = {
                (utilitario.getValorEtiqueta(strDetalleActual, "codigoPrincipal").isEmpty() ? utilitario.getValorEtiqueta(strDetalleActual,
                "codigoInterno") : utilitario.getValorEtiqueta(strDetalleActual, "codigoPrincipal")),
                (utilitario.getValorEtiqueta(strDetalleActual, "codigoAuxiliar").isEmpty() ? utilitario.getValorEtiqueta(strDetalleActual,
                "codigoAdicional") : utilitario.getValorEtiqueta(strDetalleActual, "codigoAuxiliar")),
                utilitario.getValorEtiqueta(strDetalleActual, "cantidad"), utilitario.getValorEtiqueta(strDetalleActual, "descripcion"),
                utilitario.getValorEtiqueta(strDetalleActual, "precioUnitario"),
                utilitario.getValorEtiqueta(strDetalleActual, "precioTotalSinImpuesto")};
            lisDetalle.add(new DetalleReporte(columnas, valores));
        }
        // Genera el reporte en PDF
        ReporteDataSource data = new ReporteDataSource(lisDetalle);
        GenerarReporte generarReporte = new GenerarReporte();
        generarReporte.setDataSource(data);
        File reporte = null;
        if (comprobante.getCoddoc().equals(TipoComprobanteEnum.FACTURA.getCodigo())) {
            reporte = generarReporte.crearPDF(parametros, "factura.jasper", parametros.get("CLAVE_ACC") + "");

        } else if (comprobante.getCoddoc().equals(TipoComprobanteEnum.NOTA_DE_CREDITO.getCodigo())) {
            reporte = generarReporte.crearPDF(parametros, "notaCredito.jasper", parametros.get("CLAVE_ACC") + "");
        }

        try {
            InputStream myInputStream = new FileInputStream(reporte);
            byte[] bpdf = IOUtils.toByteArray(myInputStream);
            myInputStream.close();
            // Borra el archivo cuando termina el proceso
            reporte.delete();
            return bpdf;
        } catch (Exception e) {
            throw new GenericException("ERROR. al crear PDF", e);
        }
    }

    /**
     * Construye el arreglo de parametros comunes para los reportes de los
     * comprobantes electrónicos
     *
     * @param cadenaXML
     * @param comprobante
     * @return
     */
    private Map<String, Object> getParametrosComunes(String cadenaXML, Comprobante comprobante) {
        Map<String, Object> parametros = new HashMap<>();
        try {
            parametros.put("SUBREPORT_DIR", ParametrosSistemaEnum.RUTA_SISTEMA.getCodigo());
            parametros.put("RUC", utilitario.getValorEtiqueta(cadenaXML, "ruc"));
            parametros.put("NUM_AUT", comprobante.getNumAutorizacion());
            if (comprobante.getFechaautoriza() != null) {
                parametros.put("FECHA_AUT", utilitario.getFormatoFecha(comprobante.getFechaautoriza(), "dd 'de' MMMM 'del' yyyy  HH:mm"));
            }
            parametros.put("TIPO_EMISION", utilitario.getValorEtiqueta(cadenaXML, "tipoEmision"));
            parametros.put("CLAVE_ACC", utilitario.getValorEtiqueta(cadenaXML, "claveAcceso"));
            parametros.put("RAZON_SOCIAL", utilitario.getValorEtiqueta(cadenaXML, "razonSocial"));
            parametros.put("DIR_MATRIZ", utilitario.getValorEtiqueta(cadenaXML, "dirMatriz"));
            parametros.put("DIR_SUCURSAL", utilitario.getValorEtiqueta(cadenaXML, "dirEstablecimiento"));
            parametros.put("CONT_ESPECIAL", utilitario.getValorEtiqueta(cadenaXML, "contribuyenteEspecial"));
            parametros.put("LLEVA_CONTABILIDAD", utilitario.getValorEtiqueta(cadenaXML, "obligadoContabilidad"));
            parametros.put("RS_COMPRADOR", utilitario.getValorEtiqueta(cadenaXML, "razonSocialComprador"));
            parametros.put("RUC_COMPRADOR", utilitario.getValorEtiqueta(cadenaXML, "identificacionComprador"));

            parametros.put("NUM_DOC_MODIFICADO", utilitario.getValorEtiqueta(cadenaXML, "numDocModificado"));
            parametros.put("DOC_MODIFICADO", TipoComprobanteEnum.getDescripcion(utilitario.getValorEtiqueta(cadenaXML, "codDocModificado")));
            parametros.put("FECHA_EMISION_DOC_SUSTENTO", utilitario.getValorEtiqueta(cadenaXML, "fechaEmisionDocSustento"));
            parametros.put("RAZON_MODIF", utilitario.getValorEtiqueta(cadenaXML, "motivo"));

            if (comprobante.getFechaemision() != null) {
                parametros.put("FECHA_EMISION", utilitario.getFormatoFecha(comprobante.getFechaemision(), "dd 'de' MMMM 'del' yyyy"));
            }
            parametros.put("VALOR_TOTAL", utilitario.getFormatoNumero(comprobante.getImportetotal()));
            parametros.put("DESCUENTO", utilitario.getValorEtiqueta(cadenaXML, "totalDescuento"));
            String cadenaTotalImpuesto = utilitario.getValorEtiqueta(cadenaXML, "totalImpuesto");
            parametros.put("IVA", utilitario.getValorEtiqueta(cadenaTotalImpuesto, "valor"));
            parametros.put("IVA_0", "0,00");
            parametros.put("IVA_12", utilitario.getValorEtiqueta(cadenaTotalImpuesto, "baseImponible"));
            parametros.put("SUBTOTAL", utilitario.getValorEtiqueta(cadenaXML, "totalSinImpuestos"));
            parametros.put("NUM_FACT", utilitario.getValorEtiqueta(cadenaXML, "estab") + "-" + utilitario.getValorEtiqueta(cadenaXML, "ptoEmi") + "-"
                    + utilitario.getValorEtiqueta(cadenaXML, "secuencial"));
            parametros.put("TOTAL_DESCUENTO", utilitario.getValorEtiqueta(cadenaXML, "totalDescuento"));
            parametros.put("AMBIENTE", utilitario.getValorEtiqueta(cadenaXML, "ambiente"));
            parametros.put("NOM_COMERCIAL", utilitario.getValorEtiqueta(cadenaXML, "nombreComercial"));
            // Porcentaje iva
            double dou_base_no_objeto_iva = 0; // No aplica  
            double dou_base_tarifa0 = 0;
            double dou_base_grabada = 0;
            try {
                dou_base_tarifa0 = comprobante.getSubtotal0().doubleValue();
            } catch (Exception e) {
            }
            try {
                dou_base_grabada = comprobante.getSubtotal().doubleValue();
            } catch (Exception e) {
            }
            double totalSinImpuestos = dou_base_no_objeto_iva + dou_base_tarifa0 + dou_base_grabada;
            double dou_porcentaje_iva = 0;
            try {
                dou_porcentaje_iva = (comprobante.getIva().doubleValue() * 100) / totalSinImpuestos;
            } catch (Exception e) {
            }
            parametros.put("TARIFA_IVA", TipoImpuestoIvaEnum.getPorcentaje(dou_porcentaje_iva));
        } catch (Exception e) {
        }
        return parametros;
    }

}
