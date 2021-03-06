<%-- 
    Document   : funcionarioListar
    Created on : 09/06/2018, 20:03:37
    Author     : ArtVin
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript">
        function confirmar(link, id) {
            if (confirm("Deseja remover esse cliente?")) {
                 link.href = "FuncionarioServlet?action=removeFuncionario&id="+id;
             }
        }    
        </script>
        <title>Always Together</title>
    </head>
    <body>
        <%@include file="headerLogged.jsp"%>
        
        <section class="text-center">
            <div class="container">
                <div class="tittle-agileinfo">
                    <h3>Lista de Funcionários</h3>
                </div>
            </div>
        </section>
        
        <div class="container">
            <div class="sim-button button12" style="margin-top: 0px; float: right; background: rgb(65, 131, 154); margin-bottom: 15px;">
                <a href="FuncionarioServlet?action=funcionarioForm">Novo</a>
            </div>
            <div class="clearfix"> </div>
        </div>
        
        <div class="container">
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>Email</th>
                        <th>CPF</th>
                        <th>Cargo</th>
                        <th>Data de Nascimento</th>
                        <th>Ação</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${listaFuncionarios}" var="funcionario">
                        <tr>
                            <td>${funcionario.idFuncionario}</td>
                            <td>${funcionario.nome}</td>
                            <td>${funcionario.email}</td>
                            <td>${funcionario.cpf}</td>
                            <td>${funcionario.cargo}</td>
                            <td>${funcionario.dataNasc}</td>
                            <td style="width: 125px;">
                                <a href="#" data-toggle="modal" data-target="#exampleModal${funcionario.idFuncionario}">
                                    <i class="fa fa-eye"></i>
                                </a>
                                <a href="FuncionarioServlet?action=formUpdateFuncionario&id=<c:out value="${funcionario.idFuncionario}" />">
                                    <i class="fa fa-pencil"></i>
                                </a>
                                <a href="" onclick="confirmar(this,<c:out value="${funcionario.idFuncionario}" />">
                                    <i class="fa fa-times"></i>
                                </a>
                            </td>
                        </tr>
                    <%@include file="modal.jsp"%>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        
        <%@include file="footer.jsp"%>
    </body>
</html>
