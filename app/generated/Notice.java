//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.27 at 01:43:39 AM MESZ 
//
package generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="api-key" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="notifier" type="{}notifier"/>
 *         &lt;element name="error" type="{}error"/>
 *         &lt;element name="request" type="{}request" minOccurs="0"/>
 *         &lt;element name="server-environment" type="{}serverEnvironment"/>
 *       &lt;/all>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {})
@XmlRootElement(name = "notice")
public class Notice {

    @XmlElement(name = "api-key", required = true)
    protected String apiKey;
    @XmlElement(required = true)
    protected Notifier notifier;
    @XmlElement(required = true)
    protected Error error;
    protected Request request;
    @XmlElement(name = "server-environment", required = true)
    protected ServerEnvironment serverEnvironment;
    @XmlAttribute(required = true)
    protected String version;

    public Notice() {
    }

    /**
     * Gets the value of the apiKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Sets the value of the apiKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApiKey(String value) {
        this.apiKey = value;
    }

    /**
     * Gets the value of the notifier property.
     * 
     * @return
     *     possible object is
     *     {@link Notifier }
     *     
     */
    public Notifier getNotifier() {
        return notifier;
    }

    /**
     * Sets the value of the notifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link Notifier }
     *     
     */
    public void setNotifier(Notifier value) {
        this.notifier = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link Error }
     *     
     */
    public Error getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link Error }
     *     
     */
    public void setError(Error value) {
        this.error = value;
    }

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link Request }
     *     
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link Request }
     *     
     */
    public void setRequest(Request value) {
        this.request = value;
    }

    /**
     * Gets the value of the serverEnvironment property.
     * 
     * @return
     *     possible object is
     *     {@link ServerEnvironment }
     *     
     */
    public ServerEnvironment getServerEnvironment() {
        return serverEnvironment;
    }

    /**
     * Sets the value of the serverEnvironment property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServerEnvironment }
     *     
     */
    public void setServerEnvironment(ServerEnvironment value) {
        this.serverEnvironment = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }
}
