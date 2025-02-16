<%-- 
    Document   : addLevelModal
    Created on : Feb 13, 2025, 1:42:12 PM
    Author     : alejandro
--%>

<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!--Modal de agregar nivel-->
<div class="modal fade" id="add-level-modal" tabindex="-1" aria-labelledby="addModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addModalLabel">Add Level</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <sf:form method="POST" modelAttribute="addLevel">
                    <div class="mb-3">
                        <select class="form-select form-control" id="level-id" name="level-id">
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