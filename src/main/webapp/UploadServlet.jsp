<%@ page import="java.io.*,java.util.*,javax.servlet.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="org.apache.commons.fileupload.disk.*"%>
<%@ page import="org.apache.commons.fileupload.servlet.*"%>
<%@ page import="org.apache.commons.io.output.*"%>
<%@ page import="org.apache.commons.lang.*"%>
<%@ page import="org.jbpm.migration.JbpmMigration"%>

<%
    File file;
    int maxSize = 5000 * 1024;
    ServletContext context = pageContext.getServletContext();

    // Verify the content type
    String contentType = request.getContentType();
    if ((contentType.indexOf("multipart/form-data") >= 0)) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // Maximum size that will be stored in memory.
        factory.setSizeThreshold(maxSize);

        // Create a new file upload handler.
        ServletFileUpload upload = new ServletFileUpload(factory);

        // Maximum file size to be uploaded.
        upload.setSizeMax(maxSize);
        try {
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);

            // Process the uploaded file items
            Iterator i = fileItems.iterator();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>JSP File upload</title>");
            out.println("</head>");
            out.println("<body>");
            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                if (!fi.isFormField()) {
                    // Get the uploaded file parameters
                    String fieldName = fi.getFieldName();
                    String fileName = fi.getName();
                    boolean isInMemory = fi.isInMemory();
                    long sizeInBytes = fi.getSize();
                    
                    // format an introduction.
                    
                    // now show the input jPDL.
                    out.println("<p><h2>Input file jBPM jPDL:</h2> </p>");
                    out.println("<pre>" + StringEscapeUtils.escapeHtml(fi.getString()) + "</pre>");
                    
                    // now the final output BPMN2.
                    out.println("<p><h2>Output file BPMN2:</h2> </p>");
                    out.println("<pre>" + StringEscapeUtils.escapeHtml(JbpmMigration.transform(fi.getString())) + "</pre>");
                }
            }
            out.println("</body>");
            out.println("</html>");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    } else {
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet upload</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>No file uploaded</p>");
        out.println("</body>");
        out.println("</html>");
    }
%>
