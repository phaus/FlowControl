//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.27 at 01:43:39 AM MESZ 
//


package generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _VarVar_QNAME = new QName("", "var");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Var }
     * 
     */
    public Var createVar() {
        return new Var();
    }

    /**
     * Create an instance of {@link Notifier }
     * 
     */
    public Notifier createNotifier() {
        return new Notifier();
    }

    /**
     * Create an instance of {@link Notice }
     * 
     */
    public Notice createNotice() {
        return new Notice();
    }

    /**
     * Create an instance of {@link Backtrace }
     * 
     */
    public Backtrace createBacktrace() {
        return new Backtrace();
    }

    /**
     * Create an instance of {@link Backtrace.Line }
     * 
     */
    public Backtrace.Line createBacktraceLine() {
        return new Backtrace.Line();
    }

    /**
     * Create an instance of {@link VarList }
     * 
     */
    public VarList createVarList() {
        return new VarList();
    }

    /**
     * Create an instance of {@link Error }
     * 
     */
    public Error createError() {
        return new Error();
    }

    /**
     * Create an instance of {@link Request }
     * 
     */
    public Request createRequest() {
        return new Request();
    }

    /**
     * Create an instance of {@link ServerEnvironment }
     * 
     */
    public ServerEnvironment createServerEnvironment() {
        return new ServerEnvironment();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Var }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "var", scope = Var.class)
    public JAXBElement<Var> createVarVar(Var value) {
        return new JAXBElement<Var>(_VarVar_QNAME, Var.class, Var.class, value);
    }

}