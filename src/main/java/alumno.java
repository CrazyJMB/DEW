

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
    
    private static final String preface = "<!DOCTYPE html>\n"
    		+ "<html lang=\"es\">\n"
    		+ "\n"
    		+ "<head>\n"
    		+ "    <title>Bienvenido</title>\n"
    		+ "    <meta charset=\"utf-8\">\n"
    		+ "    <meta grupo=\"DEW G4\">\n"
    		+ "    <link rel=\"stylesheet\" href=\"estilo.css\">\n"
    		+ "    <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css\"\n"
    		+ "        integrity=\"sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2\" crossorigin=\"anonymous\">\n"
    		+ "</head>\n"
    		+ "<body>\n"
    		+ "    <script src=\"https://code.jquery.com/jquery-3.5.1.min.js\"\n"
    		+ "        integrity=\"sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=\" crossorigin=\"anonymous\"></script>\n"
    		+ "    <img src=\"https://a-static.besthdwallpaper.com/chica-lofi-estudiando-papel-pintado-3200x900-81037_74.jpg\"\n"
    		+ "        style=\"width: 100%; max-height: 190px;\" alt=\"chicaEstudiando\">\n"
    		+ "    <div class=\"texto\" style=\"margin-left: 80px;\">";

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
		out.println("<h1><strong>Bienvenido <strong>" + nombre + " " + apellidos + "</strong></h1>");
		
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
        
        out.println("<div class=\"container\">\n"
        		+ "                <div class=\"jumbotron\">\n"
        		+ "                  <h1>Estas son tus asignaturas matriculadas</h1>\n"
        		+ "                  <p>¿Que notas deseas ver?</p>\n"
        		+ "                  <div class=\"container-fluid\">");
        
        //we save the subjects in an array of JSON objects
        JSONArray jsonAsig = null;
		try {
			jsonAsig = new JSONArray(answer.toString());

			//we print the JSON objects
			for(int i=0; i<jsonAsig.length(); i++) {
				String asig=jsonAsig.getJSONObject(i).getString("asignatura");
				out.println("<p><a href=\"detallesAlumno/"+ asig + "\" class=\"text-decoration-none\"\">" + asig + "</a></p>");
				String nota=jsonAsig.getJSONObject(i).getString("nota");
				if(nota=="")
					nota="Sin calificacion";
				out.println("<p>Nota: " + nota + "</p>");
			}
		}catch (JSONException e) {
				e.printStackTrace();
		}
		out.println("</div></div></div><div>");
		out.println("<form action='certificadoAlumno' method='GET'>" );
		out.println("<input type='hidden' name='nombre' value='" + nombre + "'/>"); 
		out.println("<input type='hidden' name='apellidos' value='" + apellidos + "'/>"); 
		out.println("<input type='hidden' name='asigJson' value='" + answer + "'/>"); 
		out.println("<button type=\"submit\" class=\"btn btn-primary\" style=\"background-color: brown;\">Imprimir certificado</button>" + "</form>");
		out.println("</body></html>");
		out.println("</div>\n"
				+ "    </div>\n"
				+ "    <div style=\"display: flex;\">\n"
				+ "        <div class=\"grupo\">\n"
				+ "            <h6 id=\"titulo\"><strong>Creado por el grupo G4</strong></h6>\n"
				+ "            <ul class=\"list-group list-group-flush\" id=\"listaGrupo\">\n"
				+ "                <li class=\"list-group-item\">David Sanfélix Enguídanos</li>\n"
				+ "                <li class=\"list-group-item\">Jorge Martín Barreto</li>\n"
				+ "                <li class=\"list-group-item\">Alejandro Duque Segura</li>\n"
				+ "                <li class=\"list-group-item\">Vicent Ivorra Espasa</li>\n"
				+ "                <li class=\"list-group-item\">Davide Romano</li>\n"
				+ "            </ul>\n"
				+ "        </div>\n"
				+ "    </div>\n"
				+ "    </div>\n"
				+ "</body></html>");
		
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
