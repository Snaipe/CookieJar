package me.snaipe.cookie.xml.sax.annotated;

import me.snaipe.cookie.reflection.Reflect;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class AnnotatedXMLHandler extends DefaultHandler {

    private final Socket socket;
    private final Map<String, Map<HandlerType, Method>> elementHandlers = new HashMap<>();

    private OutputStream out;

    private final Deque<Map<String, String>> currentAttributes = new LinkedList<>();
    private final Deque<String> currentElement = new LinkedList<>();
    private final Deque<StringBuilder> currentValue = new LinkedList<>();

    private final Method missingOpenHandlerMethod;
    private final Method missingCloseHandlerMethod;

    public AnnotatedXMLHandler(Socket socket) {
        this.socket = socket;

        for (Method m : Reflect.on(this).method().withAnnotations(Handler.class).all()) {
            m.setAccessible(true);
            Handler handler = m.getAnnotation(Handler.class);

            Map<HandlerType, Method> map = elementHandlers.getOrDefault(handler.value(), new HashMap<>());
            if (handler.type().open)
                map.put(HandlerType.OPEN, m);
            if (handler.type().close)
                map.put(HandlerType.CLOSE, m);
            elementHandlers.putIfAbsent(handler.value(), map);
        }

        Method openHandler = Reflect.on(this)
                .method()
                .withName("onMissingOpenHandler")
                .one();

        this.missingOpenHandlerMethod = openHandler;

        Method closeHandler = Reflect.on(this)
                .method()
                .withName("onMissingCloseHandler")
                .one();
        this.missingCloseHandlerMethod = closeHandler;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        try {
            this.out = socket.getOutputStream();
        } catch (IOException ex) {
            throw new SAXException(ex);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        this.currentValue.peek().append(ch, start, length);
    }

    protected void reply(Object reply, Object... objects) {
        try {
           new OutputStreamWriter(out, "UTF8").append(MessageFormat.format(reply.toString(), objects)).flush();
        } catch (IOException ex) {
            throw new RuntimeException("Could not write to client", ex);
        }
    }

    public static <T> T parseObject(String s, Class<T> clazz) throws Exception {
        return clazz.getConstructor(new Class[] { String.class }).newInstance(s);
    }

    public void dispatchHandler(HandlerType type) throws SAXException {
        try {
            Map<HandlerType, Method> map = this.elementHandlers.get(this.currentElement.peek());
            Method handler = map != null ? map.get(type)
                    : (HandlerType.OPEN.equals(type) ? missingOpenHandlerMethod : missingCloseHandlerMethod);

            callHandler(handler,
                    this.currentElement.peek(),
                    this.currentAttributes.peek(),
                    HandlerType.OPEN.equals(type) ? null : this.currentValue.toString());

        } catch (SAXException ex) {
            throw new SAXException(ex);
        } catch (Throwable ex) {
            this.onUnhandledException(ex);
        }
    }

    private static Annotation[] getParametersAnnotation(Method m, Class<?>... annotationClasses) {
        Annotation[][] annotations = m.getParameterAnnotations();

        Annotation[] result = new Annotation[annotations.length];
        int i = 0;
        for (Annotation[] parameterAnnotations : annotations) {
            upper:
            for (Annotation a : parameterAnnotations) {
                for (Class<?> clazz : annotationClasses) {
                    if (a.annotationType().isAssignableFrom(clazz)) {
                        result[i] = a;
                        break upper;
                    }
                }
            }
            ++i;
        }

        return result;
    }

    public void callHandler(Method handler, String name, Map<String, String> attr, String value) throws Throwable {
        if (handler == null)
            return;

        if (handler.equals(this.missingOpenHandlerMethod) || handler.equals(this.missingCloseHandlerMethod))
            attr.put("__element__", name);

        try {
            Class<?>[] params = handler.getParameterTypes();
            Object[] callParams = new Object[params.length];
            if (params.length > 0) {
                Annotation[] paramsAnnotation = getParametersAnnotation(handler, Param.class, Value.class);

                for (int i = 0; i < params.length; ++i) {
                    Annotation a = paramsAnnotation[i];
                    String val = null;
                    if (a instanceof Param) {
                        val = attr.get(((Param) paramsAnnotation[i]).value());
                    } else if (a instanceof Value) {
                        val = value;
                    }
                    if (val == null)
                        continue;
                    callParams[i] = parseObject(val, params[i]);
                }
            }

            handler.invoke(this, callParams);
        } catch (InvocationTargetException ex) {
            throw ex.getTargetException();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    public Map<String, String> attributesToMap(Attributes attributes) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < attributes.getLength(); ++i) {
            map.put(attributes.getLocalName(i), attributes.getValue(i));
        }
        return map;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        this.currentElement.push(localName);
        this.currentAttributes.push(attributesToMap(attributes));
        this.currentValue.push(new StringBuilder());

        dispatchHandler(HandlerType.OPEN);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        dispatchHandler(HandlerType.CLOSE);

        this.currentAttributes.pop();
        this.currentElement.pop();
        this.currentValue.pop();
    }

    protected void onUnhandledException(Throwable ex) {}

}
