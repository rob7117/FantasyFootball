/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.League;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Bob Naessens
 */
@Stateless
public class LeagueFacade extends AbstractFacade<League> {
    @PersistenceContext(unitName = "FantasyFootballPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LeagueFacade() {
        super(League.class);
    }
    
}
