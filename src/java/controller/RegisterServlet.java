/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

/**
 *
 * @author Bob Naessens
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    @PersistenceUnit
    //The emf corresponding to
    private EntityManagerFactory emf; 
    
    @Resource
    private UserTransaction utx;
 
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        assert emf != null;  //Make sure injection went through correctly.
        EntityManager em = null;

        try {  
            //Get the data from registerForm
            String email     = (String) request.getParameter("emailaddress");
            String firstName = (String) request.getParameter("firstname");
            String surname   = (String) request.getParameter("surname");
            String password  = (String) request.getParameter("password");
            
            String gender = "NA";
            Date now = new Date();
            
            //Create a user object
            User user = new User(firstName, surname, email, password, gender,now);
            
            //begin a transaction
            utx.begin();
            em = emf.createEntityManager();

            // Used to check if an email is already used
            Query q = em.createQuery("SELECT COUNT(u.email) FROM User u WHERE u.email = :email");
            q.setParameter("email", email);

            long result = -1;
            try {
                result = (long) q.getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // No user already exists with this email
            if(result == 0)
            {
                em.persist(user);
                utx.commit();
                response.sendRedirect("index");
            }
            else {
                // Redirect user to the index page
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {            
            //close the em to release any resources held up by the persistebce provider
            if(em != null) {
                em.close();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
