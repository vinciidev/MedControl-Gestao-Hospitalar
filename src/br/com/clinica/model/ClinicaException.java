package br.com.clinica.model;


public class ClinicaException extends Exception {


    public ClinicaException(String message) {
        super(message);
    }


    public ClinicaException(String message, Throwable cause) {
        super(message, cause);
    }
}