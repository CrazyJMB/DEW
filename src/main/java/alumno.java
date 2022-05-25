

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.*;

/**
 * Servlet implementation class login
 */
public class alumno extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public alumno() {
        super();
    }
    
    private static final String preface = "<!DOCTYPE html>\n<html lang=\"es-es\">\n" +
   		 "<head><meta charset=\"utf-8\" />" +
   		 "<title>PruebaPOST</title>\n</head>\n<body>\n";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		//get session attributes
		HttpSession session = request.getSession(true);
		String key=(String)session.getAttribute("key");
		String dni=(String)session.getAttribute("dni");
		List<HttpCookie> cookies=(List<HttpCookie>)session.getAttribute("cookie");
		
		//I create a cookieManager and I add the cookie saved in the session
		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);
		cookieManager.getCookieStore().add(null, cookies.get(0));
		
		//First connection: get the name and surname of the student
		String RequestURL = "http://"+ request.getServerName() + ":9090/CentroEducativo/alumnos/" + dni + "?key=" + key;
		HttpURLConnection con = (HttpURLConnection) new URL(RequestURL).openConnection();

        //add reuqest header
        con.setRequestMethod("GET");

        // Send get request
        con.setDoInput(true);

        int responseCode = con.getResponseCode();
        
        StringBuilder answer = new StringBuilder();
        if(responseCode>299) {
        	out.println(preface);
        	out.println("<br>CODIGO SERVLET alumno<br>");
            System.out.println("\nSending 'GET' request to URL : " + RequestURL);
    		out.println("dni" + session.getAttribute("dni"));
    		out.println("password" + session.getAttribute("password"));
    		out.println("key" + session.getAttribute("key"));
    		out.println("<br>Something is wrong! Error code:" + responseCode);
        	out.println("</body></html>");
        }
        else {
        	try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                
                while ((line = in.readLine()) != null) {
                    answer.append(line);
                }
                
        	}
        }
        
        //I have the answer in string format so we save it into a JSON object
        JSONObject json;
        String nombre = "";
        String apellidos = "";
		try {
			json = new JSONObject(answer.toString());
			nombre = json.getString("nombre");
	        apellidos = json.getString("apellidos");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		//we disconnect from that URL
		con.disconnect();
		
		out.println(preface);
		out.println("<h2>Benvenuto " + nombre + " " + apellidos + "</h2>");
		
		//second request for getting the subjects
		RequestURL = "http://"+ request.getServerName() + ":9090/CentroEducativo/alumnos/" + dni + "/asignaturas?key=" + key;
		
		con = (HttpURLConnection) new URL(RequestURL).openConnection();

        //add reuqest header
        con.setRequestMethod("GET");

        // Send get request
        con.setDoInput(true);

        responseCode = con.getResponseCode();
        
        answer = new StringBuilder();
        if(responseCode>299) {
        	out.println(preface);
        	out.println("<br>CODIGO SERVLET alumno<br>");
            System.out.println("\nSending 'GET' request to URL : " + RequestURL);
    		out.println("dni" + session.getAttribute("dni"));
    		out.println("password" + session.getAttribute("password"));
    		out.println("key" + session.getAttribute("key"));
    		out.println("<br>Something is wrong! Error code:" + responseCode);
        	out.println("</body></html>");
        	//out.println(preface + "<h2>Usuario y password inválidos</h2></body></html>");
        }
        else {
        	try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                
                while ((line = in.readLine()) != null) {
                    answer.append(line);
                }
                
                
        	}
        }
        
        //we save the subjects in an array of JSON objects
        JSONArray jsonAsig = null;
		try {
			jsonAsig = new JSONArray(answer.toString());

			//we print the JSON objects
			for(int i=0; i<jsonAsig.length(); i++)
				out.println("<br><h2>" + jsonAsig.getJSONObject(i).getString("asignatura") + "</h2>");
	
		}catch (JSONException e) {
				e.printStackTrace();
		}
		out.println("</body></html>");
		
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
