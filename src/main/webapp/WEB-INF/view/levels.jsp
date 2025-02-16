<%-- 
    Document   : Levels
    Created on : Feb 12, 2025, 8:45:06 PM
    Author     : alejandro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html>
<html>
    <tiles:insertDefinition name="header" />
    <body>
        
        <c:set var="urlPrefix" value="${navUrlPrefix}" scope="request"/>
        <tiles:insertDefinition name="nav" />
        
        <div class="row justify-content-center">
            <h1>Levels</h1>
        </div>
        
        <c:if test="${not empty errorMessage}">
            <div class="form-group row justify-content-center">
                <div class="col-lg-4">
                    <div class="alert alert-danger" role="alert">
                        ${errorMessage}
                    </div>
                </div>
            </div>
        </c:if>

        <c:if test="${not empty successMessage}">
            <div class="form-group row justify-content-center">
                <div class="col-lg-4" >
                    <div class="alert alert-success">
                        ${successMessage}
                    </div>
                </div>
            </div>
        </c:if>
        
        <div class="modal fade" id="add-level-modal" tabindex="-1" aria-labelledby="addModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="addModalLabel">Add Level</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close">
                        </button>     
                    </div>
                    <div class="modal-body">
                        <sf:form method="POST" modelAttribute="addLevelForm">
                            <div class="mb-3">
                                <select class="form-select form-control" id="levelType" name="levelType">
                                    <c:forEach items="${levelIds}" var="levelId">
                                        <option value="${levelId}">${levelId}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                <button type="submit" class="btn btn-primary">Add</button>
                            </div>
                        </sf:form>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="container">
            <!--Barra de busqueda de niveles-->
            <form class="row search-filter" method="POST">
                <div class="col-6 mx-3 my-4">
                    <div class="input-group">
                        <input type="text" id="level-filter" class="form-control" placeholder="Search level" />
                        <button type="submit" class="btn btn-primary">
                            <img src="${pageContext.request.contextPath}/static/img/search.svg" width="34" height="34" style="border-radius: 1rem;" title="Search level" />
                        </button>
                        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#add-level-modal">
                            <img src="${pageContext.request.contextPath}/static/img/add.svg" width="34" height="34" style="border-radius: 1rem;" title="Add level" />
                        </button>
<!--                        <a href="${pageContext.request.contextPath}/levels/add">
                            <button type="button" class="btn btn-primary">
                                <img src="${pageContext.request.contextPath}/static/img/add.svg" width="34" height="34" fill="white" class="table-button" style="border-radius: 1rem;" title="Agregar nivel" />
                            </button>
                        </a>-->
                    </div>
                </div>
            </form>


            <c:choose>
                <c:when test="${levels.size() == 0}">
                    <div class="d-flex justify-content-center my-4">
                        <div class="alert alert-danger" role="alert">No levels found</div>
                    </div>
                </c:when>
                <c:otherwise>
                    <!--Tabla de niveles-->
                    <div class="container table-container">
                        <!--Centrado verticalmente-->
                        <table class="table table-striped table-hover align-middle">
                            <!--header de la tabla-->
                            <thead>
                                <tr>
                                  <th>Id</th>
                                  <th>Num. nivel</th>
                                  <th>Acción</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${levels}" var="level">
                                    <!--Filas de la tabla-->
                                    <tr>
                                        <td>${level.getId()}</td>
                                        <td>${level.getLevelType()}</td>
                                        <td>
                                            <!--Boton de listado de teoremas asociados -->
                                            <a href="#">
                                              <button type="button" class="btn btn-primary">
                                                <img src="${pageContext.request.contextPath}/static/img/detail.svg" width="24" height="24" fill="white" class="table-button" style="border-radius: 5px;" title="Ver teoremas" />
                                              </button>
                                            </a>

                                            <!--Boton de motificar-->
                                            <button type="button" id="edit'${level.getId()}'" class="btn btn-primary my-2 edit-client" data-bs-toggle="modal">
                                              <img src="${pageContext.request.contextPath}/static/img/edit.svg" width="24" height="24" fill="white" class="table-button" value="submit" style="border-radius: 5px;" title="Modificar nivel" />
                                            </button>

                                            <!--Boton de eliminar-->
                                            <button id="delete'${level.getId()}'" type="button" class="btn btn-primary delete-level" data-bs-toggle="modal">
                                              <img src="${pageContext.request.contextPath}/static/img/delete.svg" width="24" height="24" fill="white" class="table-button" value="submit" style="border-radius: 5px;" title="Eliminar nivel" />
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                           </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>
