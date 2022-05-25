

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.*;

/**
 * Servlet Filter implementation class LoginFilter
 */
public class LoginFilter implements Filter {
	
	private static final String preface = "<!DOCTYPE html>\n<html lang=\"es-es\">\n" +
	   		 "<head><meta charset=\"utf-8\" />" +
	   		 "<title>PruebaPOST</title>\n</head>\n<body>\n";
	
	//Map for associations User Tomcat - CentroEducativo User and Password
	HashMap<String, User> usuarios = new HashMap<String,User>();
    /**
     * Default constructor. 
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		usuarios.put("profesor", new User("23456733H","123456"));
		usuarios.put("alumno", new User("12345678W","123456"));
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);
        String userTomcat = req.getRemoteUser();
	        if(session.getAttribute("key") == null) {
	        	
	        	//I want to get the dni and password associated to the user of Tomcat inserted in the login
	        	User u=usuarios.get(userTomcat);
	            String dni = u.getDni();
	            String pass = u.getPassword();
	            
	            String jsonInputString = "{\"dni\": \"" + dni + "\", \"password\": \"" + pass + "\"}";
	            
	            //cookies
	            CookieManager cookieManager = new CookieManager();
				CookieHandler.setDefault(cookieManager);
				List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
				
				//create the connection
	            String CentroURL="http://"+ request.getServerName() + ":9090/CentroEducativo/login";
	            HttpURLConnection con = (HttpURLConnection) new URL(CentroURL).openConnection();

	            //add reuqest header
	            con.setRequestMethod("POST");
	            con.setRequestProperty("Content-Type", "application/json");

	            // Send post request with json object
	            con.setDoOutput(true);
	            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
	                wr.writeBytes(jsonInputString);
	                wr.flush();
	            }
	            
	            //get the response in the StringBuilder answer
	            int responseCode = con.getResponseCode();
	            
	            StringBuilder answer = new StringBuilder();
	            if(responseCode>299) {
	            	res.sendRedirect("login-failed.html");
	            }
	            else {
	            	try (BufferedReader in = new BufferedReader(
	                        new InputStreamReader(con.getInputStream()))) {

	                    String line;
	                    
	                    while ((line = in.readLine()) != null) {
	                        answer.append(line);
	                    }
	                    //print result
	                    System.out.println(res.toString());
	            	}
	            }
	            
	            String key = answer.toString();
	            
	            if(key=="") {
	            	res.sendRedirect("login-failed.html");
	            }
	            
	            
	            //the out.println() have to be deleted, they are just for testing if everything is working
	            String setCookie = cookies.get(0).getValue();

	            session.setAttribute("dni", dni);
	            session.setAttribute("password", pass);
	            session.setAttribute("key", key);
	            session.setAttribute("cookie", cookies);
	            
	            if(req.isUserInRole("rolalu")) {
	            	//response.sendRedirect("loginAlumno.html");
	            }
        		if(req.isUserInRole("rolpro")) {

                }
	        }
	        else
	        {
	        	if(req.isUserInRole("rolalu") || req.getRequestURI().equals(req.getContextPath() + "/ListaAsignaturas.html")) {
	        		res.sendRedirect("notAuthorized");

                }
	        	if(req.isUserInRole("rolpro") || req.getRequestURI().equals(req.getContextPath() + "/alumno")) {
	        		res.sendRedirect("notAuthorized");
	            	
	        	}	        	
	        	
	        }

	        chain.doFilter(request, response);

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
