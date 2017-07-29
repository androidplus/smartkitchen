package driver;

public class UsbManagerNotInitedException extends RuntimeException {
    private static final long serialVersionUID = 10203040506070L;

    public UsbManagerNotInitedException(String str) {
        super(str);
    }

    public UsbManagerNotInitedException(String str, Throwable th) {
        super(str, th);
    }

    public UsbManagerNotInitedException(Throwable th) {
        super(th);
    }
}
