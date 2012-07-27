/**
 * VarList
 * 17.05.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import play.db.jpa.Model;

@Entity
public class VarList extends Model {

    @OneToMany(mappedBy = "list", cascade = CascadeType.REMOVE)
    public List<Var> var;

    public VarList(generated.VarList varList) {
        var = new ArrayList<Var>();
        for (generated.Var v : varList.getVar()) {
            var.add(new Var(v));
        }
    }

    public String summarize() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < var.size() && i < 10; i++) {
            sb.append("\n\t").append(var.get(i));
        }
        return sb.toString();
    }

    public void saveVars(){
        for (Var v : var) {
            v.list =this;
            v.save();
        }
    }
}
