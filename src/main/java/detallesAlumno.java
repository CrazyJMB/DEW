

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class detallesAlumno
 */
public class detallesAlumno extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public detallesAlumno() {
        super();
        // TODO Auto-generated constructor stub
    }

    private static final String preface = "<!DOCTYPE html>\n<html lang=\"es-es\">\n" +
      		 "<head><meta charset=\"utf-8\" />" +
      		 "<title>Detalles DEW</title>\n</head>\n<body>\n";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession(true);
		String key=(String)session.getAttribute("key");
		String dni=(String)session.getAttribute("dni");
		List<HttpCookie> cookies=(List<HttpCookie>)session.getAttribute("cookie");
		
		//I create a cookieManager and I add the cookie saved in the session
		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);
		cookieManager.getCookieStore().add(null, cookies.get(0));
		
		//I DONT KNOW HOW TO PASS THE ACRONIMO OF THE ASIGNATURA
		String reqURL=request.getRequestURI();
		String asig=reqURL.substring(reqURL.lastIndexOf("/") + 1);
		
		//First connection: get the name and surname of the student
		String RequestURL = "http://"+ request.getServerName() + ":9090/CentroEducativo/asignaturas/" + asig + "?key=" + key;
		HttpURLConnection con = (HttpURLConnection) new URL(RequestURL).openConnection();
		
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
        String acronimo = "";
        String creditos = "";
        String cuatrimestre = "";
        String curso = "";
        String nombre = "";
		try {
			json = new JSONObject(answer.toString());
			nombre = json.getString("nombre");
			acronimo = json.getString("acronimo");
			creditos = json.get("creditos").toString();
			cuatrimestre = json.get("cuatrimestre").toString();
			curso = json.get("curso").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		out.println("<h2>" + asig + " detalles</h2>");
		out.println("<br><h3>Acronimo: " + acronimo + "</h3>");
		out.println("<br><h3>Creditos: " + creditos + "</h3>");
		out.println("<br><h3>Cuatrimestre: " + cuatrimestre + "</h3>");
		out.println("<br><h3>Curso: " + curso + "</h3>");
		out.println("<br><h3>nombre: " + nombre + "</h3>");
		out.println("</body></html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
