package asia.wemap.androidsdk.exceptions;

public class CalledFromWorkerThreadException extends RuntimeException {

  public CalledFromWorkerThreadException(String message) {
    super(message);
  }
}
