package me.snaipe.cookie.xml.sax.annotated;

public enum HandlerType {
    OPEN(true, false),
    CLOSE(false, true),
    BOTH(true, true);

    HandlerType(boolean open, boolean close) {
        this.open = open;
        this.close = close;
    }

    public final boolean open;
    public final boolean close;
}
