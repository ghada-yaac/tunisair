/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author ghada
 */
public class Equipage {
    private StringProperty code;
    private StringProperty pilote;
    private StringProperty copilote;
    private StringProperty hotesses1;
    private StringProperty hotesse2;
    private StringProperty hotesse3;
    public Equipage(String code, String pilote, String copilote, String hotesses1, String hotesse2, String hotesse3) {
        this.code = new SimpleStringProperty(code);
        this.pilote = new SimpleStringProperty(pilote);
        this.copilote = new SimpleStringProperty(copilote);
        this.hotesses1 = new SimpleStringProperty(hotesses1);
        this.hotesse2 = new SimpleStringProperty(hotesse2);
        this.hotesse3 = new SimpleStringProperty(hotesse3);
    }

    // Getters
    public String getCode() {
        return code.get();
    }

    public String getPilote() {
        return pilote.get();
    }

    public String getCopilote() {
        return copilote.get();
    }

    public String getHotesses1() {
        return hotesses1.get();
    }

    public String getHotesse2() {
        return hotesse2.get();
    }

    public String getHotesse3() {
        return hotesse3.get();
    }

    // Property getters (utile pour data binding dans JavaFX)
    public StringProperty codeProperty() {
        return code;
    }

    public StringProperty piloteProperty() {
        return pilote;
    }

    public StringProperty copiloteProperty() {
        return copilote;
    }

    public StringProperty hotesses1Property() {
        return hotesses1;
    }

    public StringProperty hotesse2Property() {
        return hotesse2;
    }

    public StringProperty hotesse3Property() {
        return hotesse3;
    }

    // Setters
    public void setCode(String code) {
        this.code.set(code);
    }

    public void setPilote(String pilote) {
        this.pilote.set(pilote);
    }

    public void setCopilote(String copilote) {
        this.copilote.set(copilote);
    }

    public void setHotesses1(String hotesses1) {
        this.hotesses1.set(hotesses1);
    }

    public void setHotesse2(String hotesse2) {
        this.hotesse2.set(hotesse2);
    }

    public void setHotesse3(String hotesse3) {
        this.hotesse3.set(hotesse3);
    }
}