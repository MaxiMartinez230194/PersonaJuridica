/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ccpm.dpj.utilidad;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Matias Zakowicz
 * Clase para envío de mails usando hilos
 */
public class MailThread extends Thread {

    boolean enviar;
    String correoDest;
    String asunto;
    String textoMensaje;
    String pathArchivo;

    public boolean isEnviar() {
        return enviar;
    }

    public void setEnviar(boolean enviar) {
        this.enviar = enviar;
    }

    public String getCorreoDest() {
        return correoDest;
    }

    public void setCorreoDest(String correoDest) {
        this.correoDest = correoDest;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getTextoMensaje() {
        return textoMensaje;
    }

    public void setTextoMensaje(String textoMensaje) {
        this.textoMensaje = textoMensaje;
    }

    public String getPathArchivo() {
        return pathArchivo;
    }

    public void setPathArchivo(String pathArchivo) {
        this.pathArchivo = pathArchivo;
    }

    public MailThread(boolean enviar, String correoDest, String asunto, String textoMensaje, String pathArchivo) {
        this.enviar = enviar;
        this.correoDest = correoDest;
        this.asunto = asunto;
        this.textoMensaje = textoMensaje;
        this.pathArchivo = pathArchivo;
    }

    @Override
    public void run() {

        try {
            this.enviarMail(this.isEnviar(), this.getCorreoDest(), this.getAsunto(), this.getTextoMensaje(), this.getPathArchivo());
        } catch (Exception ex) {
            Logger.getLogger(MailThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void esperarXsegundos(int segundos) {
        try {
            Thread.sleep(segundos * 1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public void enviarMail(boolean enviar, String correoDest, String asunto, String textoMensaje, String pathArchivo) throws Exception {
        String correoEmisor = "ccpmisiones@gmail.com";
        String pass = "aguantejavaypostgres";
        try {

            if (enviar) {

                // Propiedades de la conexión
                Properties props = new Properties();
                props.setProperty("mail.smtp.host", "smtp.gmail.com");
                props.setProperty("mail.smtp.starttls.enable", "true");
                props.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
                props.setProperty("mail.smtp.port", "587");
                props.setProperty("mail.smtp.user", correoEmisor);    // MODIFICAR
                props.setProperty("mail.smtp.auth", "true");

                // Preparamos la sesion
                Session session = Session.getDefaultInstance(props);

                // Construimos el mensaje
                MimeMessage message = new MimeMessage(session);
                message.addHeader("Disposition-Notification-To", correoEmisor);
                message.setFrom(new InternetAddress(correoEmisor));
                message.addRecipient(
                        Message.RecipientType.TO,
                        new InternetAddress(correoDest));
                message.setSubject(asunto);
                message.setText(textoMensaje);

                //Adjunto
                //Texto del Adjuto
                BodyPart texto = new MimeBodyPart();
                texto.setText(textoMensaje);
                //Cargando la imagen
                BodyPart adjunto = new MimeBodyPart();
                adjunto.setDataHandler(new DataHandler(new FileDataSource(pathArchivo)));
                adjunto.setFileName(new File(pathArchivo).getName());
                MimeMultipart multiParte = new MimeMultipart();

                multiParte.addBodyPart(texto);
                multiParte.addBodyPart(adjunto);
                //Se agrega la la imagen al message
                message.setContent(multiParte);

                ///Fin Adjunto
                //  message.setText(textoMensaje + " <a href=google.com>Enlace</a>","ISO-8859-1", "html");
                Transport t = session.getTransport("smtp");

                // Lo enviamos.
                t.connect(correoEmisor, pass);
                if (t.isConnected()) {
                    t.sendMessage(message, message.getAllRecipients());
                    System.out.println("Se ha enviado el mail");
                } else {
                    System.out.println("No se ha podido enviar el mail");
                }

                // Cierre.
                t.close();

            } else {
                System.out.println("El destinatario no es correcto o no tiene direccion disponible. ");
            }
        } catch (Exception e) {
            throw new Exception(e.toString());
        }

    }
}
