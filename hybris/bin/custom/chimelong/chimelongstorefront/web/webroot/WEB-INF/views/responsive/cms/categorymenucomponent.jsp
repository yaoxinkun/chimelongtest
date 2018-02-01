<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<section id="chimelong_category_menu" class="content-block homepage-category_menu">
	<div class="container">
			<div class="row">
                <div class="" >
                    <ul>
                    	<c:if test="${ not empty categoryMenuDatas }">
                    		<c:forEach var="category" items="${categoryMenuDatas }">
                    			<c:if test="${category.parentCategoryName == null}">
                    				<li>
			                            <a href="javascript:;" class="cl-leftMenu-tit-ly">${category.name}</a>
			                            <ul class="cl-menu">
			                            	<c:forEach var="subCategory" items="${categoryMenuDatas }">
			                            		<c:if test="${subCategory.parentCategoryName eq category.name}">
			                                 		<li class=""><a  href="">${subCategory.name }</a></li>
			                                 	</c:if>
			                                </c:forEach>
			                             </ul>
			                        </li>
                    			</c:if>
                        	</c:forEach>
                        </c:if>
                    </ul>
                </div>
            </div>
	</div>
</section>