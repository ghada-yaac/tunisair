/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 *
 * @author ghada
 */
public class Agent {

    @Override
    public String toString() {
        return "Agent{" + "email=" + email + ", mdp=" + mdp + '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public Agent() {
    }

    public Agent(String email, String mdp) {
        this.email = email;
        this.mdp = mdp;
    }

    private String email;
    private String mdp;
}
