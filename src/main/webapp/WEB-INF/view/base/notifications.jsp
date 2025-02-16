<%-- 
    Document   : notifications
    Created on : Feb 13, 2025, 5:48:07 PM
    Author     : alejandro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String flashMessage = (String) session.getAttribute("flashMessage");
    if (flashMessage != null) {
        session.removeAttribute("flashMessage"); // Eliminar mensaje después de mostrarlo
%>
    <div class="toast position-absolute bottom-0 end-0 mx-3 my-3" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="toast-header">
            <svg class="rounded me-2" width="20" height="20" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid slice" focusable="false" role="img">
                <rect fill="#007aff" width="100%" height="100%" />
            </svg>
            <strong class="me-auto">CalcLogic</strong>
            <small class="text-muted">Justo ahora</small>
            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body"><%= flashMessage %></div>
    </div>
<%
    }
%>

