

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class listaAlumnosAsignatura extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public listaAlumnosAsignatura() {
        super();
    }
    
    private static final String preface = "<!DOCTYPE html>\n"
    		+ "<html lang=\"es\">\n"
    		+ "\n"
    		+ "<head>\n"
    		+ "    <title>Notas OnLine</title>\n"
    		+ "    <meta charset=\"utf-8\">\n"
    		+ "    <meta grupo=\"DEW G4\">\n"
    		+ "    <link rel=\"stylesheet\" href=\"estilo.css\">\n"
    		+ "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor\" crossorigin=\"anonymous\">\n"
    		+ "	<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2\" crossorigin=\"anonymous\"></script>\n"
    		+ "\n"
    		+ "</head>\n"
    		+ "\n"
    		+ "<body>\n"
    		+ "    <script src=\"https://code.jquery.com/jquery-3.5.1.min.js\"\n"
    		+ "        integrity=\"sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=\" crossorigin=\"anonymous\"></script>\n"
    		+ "\n"
    		+ "    <img src=\"https://a-static.besthdwallpaper.com/chica-lofi-estudiando-papel-pintado-3200x900-81037_74.jpg\"\n"
    		+ "        style=\"width: 100%; max-height: 190px;\" alt=\"chicaEstudiando\">\n"
    		+ "    <div class=\"texto\">\n";

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
		
		String reqURL=request.getRequestURI();
		String asig=reqURL.substring(reqURL.lastIndexOf("/") + 1);
		
		out.println(preface);
		
		out.println("<h1>Lista alumnos <strong>" + asig + "</strong></h1>");
		
		out.println("</div>\n"
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
				+ "            <button type=\"button\" class=\"btn btn-danger\" style=\"margin-top:10px;background-color:brown;\"><a style=\"text-decoration:none; color:white;\" href=\"/CentroEducativo/logout\"> <i class=\"bi bi-door-open\"></i> Cerrar Sesión</a></button>\n"
				+ "        </div>\n"
				+ "	    <div class=\"texto\" style=\"width:1000px;\">\n"
				+ "	        <ul class=\"nav nav-tabs\">\n"
				+ "	            <li class=\"nav-item\" style=\"margin-left:2%;\">");
        
		
        out.println("<a class=\"nav-link active\" data-bs-toggle=\"tab\" href=\"#" + asig + "\">" + asig + "</a>");
        
        out.println("</li>\n"
        		+ "	        </ul>\n"
        		+ "	\n"
        		+ "	        <!-- Tab panes -->\n"
        		+ "	        <div class=\"tab-content\">\n"
        		+ "	            <div class=\"tab-pane container active\" id=\""+ asig + "\">\n"
        		+ "	            	<div class=\"container\" style=\"text-align:left\">\n"
        		+ "	            		<div class=\"row\" style=\"margin-top:20px;\">");
		
        //FIRST CONNECTION: to get the details of an asignatura
        String RequestURL = "http://"+ request.getServerName() + ":9090/CentroEducativo/asignaturas/" + asig + "?key=" + key;
		
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
        
        //we save the subject
		JSONObject jsonAsig = null;
		try {
			jsonAsig = new JSONObject(answer.toString());

			//we print the JSON parameters
			String acronimo=jsonAsig.getString("acronimo");
			String nombreAsig=jsonAsig.getString("nombre");
			String curso=jsonAsig.get("curso").toString();
			String cuatrimestre=jsonAsig.getString("cuatrimestre");
			String creditos=jsonAsig.get("creditos").toString();
			out.println("<div class=\"col-6\"><b>Nombre de la asignatura</b></div>\n"
					+ "	            			<div class=\"col\"><b>Cuatrimestre</b></div>\n"
					+ "	            			<div class=\"col\"><b>Créditos</b></div>\n"
					+ "	            			<div class=\"w-100\"></div>"
					+ "	            			<div class=\"col-6\">"+ nombreAsig + "</div>\n"
					+ "	            			<div class=\"col\">"+ cuatrimestre + "</div>\n"
					+ "	            			<div class=\"col\">"+ creditos + "</div>");
		}catch (JSONException e) {
				e.printStackTrace();
		}
		
		con.disconnect();
		
		//Second connection: get the list of students
		RequestURL = "http://"+ request.getServerName() + ":9090/CentroEducativo/asignaturas/" + asig + "/alumnos?key=" + key;
		con = (HttpURLConnection) new URL(RequestURL).openConnection();
		
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
        
        out.println("</div>\n"
        		+ "	            		<div class=\"row\" style=\"margin-top:20px;\">\n"
        		+ "	            			<div class=\"col\"><b>Matriculados</b></div>\n"
        		+ "	            			<div class=\"col\"><b>Apobados</b></div>\n"
        		+ "	            			<div class=\"col\"><b>Suspendidos</b></div>\n"
        		+ "	            			<div class=\"col\"><b>Media</b></div>\n"
        		+ "	            			<div class=\"w-100\"></div>\n"
        		+ "	            			<div class=\"col\">3</div>\n"
        		+ "	            			<div class=\"col\">3</div>\n"
        		+ "	            			<div class=\"col\">0</div>\n"
        		+ "	            			<div class=\"col\">8.33</div>\n"
        		+ "	            		</div>\n"
        		+ "	            		<hr>\n"
        		+ "	            		<table class=\"table table-striped\">\n"
        		+ "	            			<thead>\n"
        		+ "	            				<tr>\n"
        		+ "	            					<th scope=\"col\">#</th>\n"
        		+ "	            					<th scope=\"col\">DNI</th>\n"
        		+ "	            					<th scope=\"col\">Nota</th>\n"
        		+ "	            					<th scope=\"col\"></th>\n"
        		+ "	            				</tr>\n"
        		+ "	            			</thead>\n"
        		+ "	            			<tbody>");
        
        //I have the answer in string format so we save it into a JSON object
        JSONArray jsonAlum = null;
		try {
			jsonAlum = new JSONArray(answer.toString());

			//we print the JSON objects
			for(int i=0; i<jsonAsig.length(); i++) {
				String alumno=jsonAlum.getJSONObject(i).getString("alumno");
				String nota=jsonAlum.getJSONObject(i).getString("nota");
				if(nota=="")
					nota="Sin calificacion";
				out.println("<tr>\n"
						+ "	            			<th scope=\"row\">" + (i+1) + "</th>\n"
						+ "	            			<td>" + alumno + "</td>\n"
						+ "	            			<td>" + nota + "</td>\n"
						+ "	            			<td>");
				out.println("<form action='detallesProfesor' method='GET'>" );
				out.println("<input type='hidden' name='dni' value='" + alumno + "'/>"); 
				out.println("<input type='hidden' name='nota' value='" + nota + "'/>"); 
				out.println("<button type=\"submit\" class=\"btn btn-primary\" style=\"background-color: brown;\">Modificar</button></form>"
						+ "</td>\n"
						+ "	            	</tr>");
			}
		}catch (JSONException e) {
				e.printStackTrace();
		}
		
		out.println("</tbody>\n"
				+ "	            		</table>\n"
				+ "	            	</div>\n"
				+ "	            </div>\n"
				+ "	        </div>\n"
				+ "	    </div>\n"
				+ "	</div>\n"
				+ "</body>\n"
				+ "</html>");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
