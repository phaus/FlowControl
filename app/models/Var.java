/**
 * Var
 * 17.05.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.JAXBElement;
import org.apache.commons.lang.StringUtils;
import play.db.jpa.Model;

@Entity
@Table(name = "vars")
public class Var extends Model {

    @Lob
    public String content;
    @Column(name = "var_key")
    public String key;
    @ManyToOne
    public VarList list;

    public Var(generated.Var var) {
        this.key = var.getKey();
        this.setContent(var.getContent());
    }
    
    public Var(generated.Var var, String content) {
        this.key = var.getKey();
        this.content = content.trim();
    }
    
    public List<Serializable> getContent() {
        List<Serializable> contentList = new ArrayList();
        contentList.addAll(Arrays.asList(content.split("####")));
        return contentList;
    }

    public List<String> getContentAsList(){
        List<String> contentList = new ArrayList<String>();
        for(Serializable o : getContent()){
            if(!o.toString().isEmpty()){
                          contentList.add((String) o);
            }

        }
        return contentList;
    }

    public void setContent(List<Serializable> contentList) {
        content = StringUtils.join(cleanContent(contentList), "####");
    }

    private static List<Serializable> cleanContent(List<Serializable> contentList) {
        List<Serializable> cList = new ArrayList<Serializable>();
        for (Serializable o : contentList) {
            if (o instanceof JAXBElement) {
                JAXBElement<generated.Var> vEle = (JAXBElement<generated.Var>) o;
                Var v = new Var(vEle.getValue());
                cList.add(v.toString());
            } else if (o != null || !o.toString().isEmpty()) {
                cList.add(o.toString().trim());
            }
        }
        return cList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(key).append(" ").append(getContent().toString());
        return sb.toString();
    }
}
