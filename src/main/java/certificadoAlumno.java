

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class certificadoAlumno extends HttpServlet {
	private static 	final long serialVersionUID = 1L;
       
    public certificadoAlumno() {
        super();
    }
    
	private static final String preface = "<!DOCTYPE html>\r\n"
			+ "<html lang=\"es\">\r\n"
			+ "\r\n"
			+ "<head>\r\n"
			+ "  <title>Certificado</title>\r\n"
			+ "  <meta charset=\"utf-8\">\r\n"
			+ "  <meta grupo=\"DEW G4\">\r\n"
			+ "  <link rel=\"stylesheet\" href=\"estilo.css\">\r\n"
			+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
			+ "  <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css\">\r\n"
			+ "  <script src=\"https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.slim.min.js\"></script>\r\n"
			+ "  <script src=\"https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js\"></script>\r\n"
			+ "  <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js\"></script>\r\n"
			+ "  <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css\"\r\n"
			+ "    integrity=\"sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2\" crossorigin=\"anonymous\">\r\n"
			+ "</head>\r\n"
			+ "\r\n"
			+ "<body>\r\n"
			+ "  <script src=\"https://code.jquery.com/jquery-3.5.1.min.js\"\r\n"
			+ "    integrity=\"sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=\" crossorigin=\"anonymous\"></script>\r\n"
			+ "    \r\n"
			+ "\r\n"
			+ "  <img src=\"https://a-static.besthdwallpaper.com/chica-lofi-estudiando-papel-pintado-3200x900-81037_74.jpg\"\r\n"
			+ "    style=\"width: 100%; max-height: 190px;\" alt=\"chicaEstudiando\">\r\n"
			+ "    <div class=\"container-fluid\" >\r\n"
			+ "      <h1 class=\"text-center\" >Certificado sin validez academica</h1>\r\n"
			+ "      <div class=\"row\" style = \"margin-top: 30px;\">";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(preface);
		
		HttpSession session = request.getSession(true);
		String dni = (String)session.getAttribute("dni");
		String nombre = request.getParameter("nombre");
		String apellidos = request.getParameter("apellidos");
		String stringAsig = request.getParameter("asigJson");
		
		// Mensaje certificado
				out.println("      <div class=\"col-sm-6\" style=\"border: 1px solid black;\r\n"
						+ "          padding: 25px 50px 75px 100px;\r\n"
						+ "          background-color:hwb(8 80% 0%);margin-left: 280px;\">");
				out.println("            <Strong>DEW - Centro Educativo</Strong> certifica que D/Dª <Strong>" + nombre + " " + apellidos + "</Strong>, con DNI " + dni + ", matriculado/a en el curso 2021/2022, ha obtenido las siguientes calificaciones:");
				out.println("      </div>");
				
		
		// Cargamos la img
		String currentDir = getServletContext().getRealPath("WEB-INF/img/");
		Path path_img = Path.of( currentDir + dni + ".pngb64");
		String base64String = Files.readString(path_img) ;
		
		// Mostramos la imagen
		out.println("        <div>\r\n"
			+ "          <img src=\"data:image/png;base64, " + base64String + " alt=\"Fotografía\" />\r\n"
			+ "          </div>");
				
		
		// Mostramos la tabla de notas
				out.println("      </div>\r\n"
						+ "    </div>\r\n"
						+ "  </div>\r\n"
						+ "  <div class=\"container\" style=\"margin-top: 80px;\">\r\n"
						+ "\r\n"
						+ "    <table class=\"table table-bordered\">\r\n"
						+ "      <thead>\r\n"
						+ "        <tr class=\"bg-danger text-white\">\r\n"
						+ "          <th>Acrónimo</th>\r\n"
						+ "          <th>Asignatura</th>\r\n"
						+ "          <th>Calificación</th>\r\n"
						+ "        </tr>\r\n"
						+ "      </thead>\r\n"
						+ "      <tbody>");
				
		// Nos traemos la key de la session
		String key = (String)session.getAttribute("key");
		
		//First connection: get the name and surname of the student
		String RequestURL = "http://"+ request.getServerName() + ":9090/CentroEducativo/asignaturas?key=" + key;
		HttpURLConnection con = (HttpURLConnection) new URL(RequestURL).openConnection();
				
		//add reuqest header
        con.setRequestMethod("GET");

        // Send get request
        con.setDoInput(true);

        int responseCode = con.getResponseCode();
        
        StringBuilder answer = new StringBuilder();
        if(responseCode>299) {
        	out.println(preface);
        	out.println("<br>Codigo certificadoAlumno<br>");
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
        JSONArray json = null;
		try {
			json = new JSONArray(answer.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		//we disconnect from that URL
		con.disconnect();
		
		// Rellenamos la tabla
		JSONArray jsonAsig = null;
		try {
			jsonAsig = new JSONArray(stringAsig.toString());

			//we print the JSON objects
			for(int i=0; i<jsonAsig.length(); i++) {
				String asigAcro=jsonAsig.getJSONObject(i).getString("asignatura");
				String asigName = "Error en el nombre";
				for(int j = 0; j < json.length(); j++) {
					String searchAcro = json.getJSONObject(j).getString("acronimo");
					if (searchAcro.equals(asigAcro)) asigName = (String) json.getJSONObject(j).getString("nombre");
				}
				String nota=jsonAsig.getJSONObject(i).getString("nota");
				if(nota=="")
					nota="Sin calificacion";
				
				out.println("        <tr>\n"
						+ "          <td class=\"bg-danger text-white\"><strong>" + asigAcro + "</strong></td>\n"
						+ "          <td>" + asigName + "</td>\n"
						+ "          <td>" + nota + "</td>\n"
						+ "        </tr>");
				
			}
	
		}catch (JSONException e) {
				e.printStackTrace();
		}
		out.println("      </tbody>\n"
				+ "    </table>\n"
				+ "  </div>\n"
				+ "  <div class=\"col\" style=\"margin-top: 280px;\">\n"
				+ "    <p style=\"margin-left: 180px;\">En Valencia, a " + java.time.LocalDate.now().getDayOfMonth() + "/" + java.time.LocalDate.now().getMonthValue() + "/"  + java.time.LocalDate.now().getYear() +  "</p>\n"
				+ "    <p style=\"margin-left: 180px;\">Firmado por el secretario</p>\n"
				+ "    <p style=\"margin-left: 180px;\"><img\n"
				+ "        src=https://buclee.com/wp-content/uploads/2015/12/las-mejores-fotos-del-2015-42.jpg\n"
				+ "        style=\"max-width: 60px; max-height: 40px;\" alt=\"fototomcat\"></p>\n"
				+ "    <p style=\"margin-left: 180px;\">[Tomcat 9.0.41]</p>\n"
				+ "  </div>\n"
				+ "\n"
				+ "</body>\n"
				+ "\n"
				+ "</html>");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
