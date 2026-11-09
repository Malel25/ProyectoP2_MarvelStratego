package excepciones;

public class PasswordInvalidoException extends Exception{
    public PasswordInvalidoException(String mensaje){
        super(mensaje);
    }
}
