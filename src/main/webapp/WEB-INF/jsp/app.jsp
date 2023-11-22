<%@ include file="/WEB-INF/jsp/general/header.jsp"%>

<c:url var="app" value="/app.js" />

<div id="myApp">
    <!-- Nav bar -->
    <%@ include file="/WEB-INF/jsp/general/navbar.jsp"%>

    <!-- Login section -->
    <div ref="test" class="form-popup" v-if="show_login">
        <%@ include file="/WEB-INF/jsp/popUp/popUpSignIn.jsp"%>
    </div>

    <!-- Signup section -->
    <div ref="test" class="form-popup" v-if="show_signup">
        <%@ include file="/WEB-INF/jsp/popUp/popUpSignUp.jsp"%>
    </div>

    <div class="page">
        <!-- Display search bar -->
        <div class="margin_small search" v-if="search != null">
            <div v-if="search == 'person'">
                <%@ include file="/WEB-INF/jsp/searchEntity/searchPerson.jsp"%>
            </div>

            <div v-if="search == 'cv'">
                <%@ include file="/WEB-INF/jsp/searchEntity/searchCv.jsp"%>
            </div>
            
            <div v-if="search == 'activity'">
                <%@ include file="/WEB-INF/jsp/searchEntity/searchActivity.jsp"%>
            </div>
        </div>
    
        <div class="container margin_small">
            
    
            <!-- Display the forms for modifying something -->
            <div v-if="pageType == 'modifying'">
                <button v-if="person" class="btn btn-primary" v-on:click="goBack()">Retour arriere</button>
                <button v-on:click="setPageType('showing')" class="btn btn-secondary">Retour a la normal</button>
                <!-- If there is no actity -->
                <div v-if="!activity">
                    <!-- If there is no cv -->
                    <div v-if="!cv">
                        <!-- If there is no person -->
                        <div v-if="!person">
                        </div>
    
                        <!-- If there is a person -->
                        <div v-if="person">
                            <%@ include file="/WEB-INF/jsp/formEntity/formPerson.jsp"%>
                        </div>
                    </div>
    
                    <!-- If there is a cv -->
                    <div v-if="cv">
                        <%@ include file="/WEB-INF/jsp/formEntity/formCv.jsp"%>
                    </div>
                </div>
    
                <!-- If there is an actity -->
                <div v-if="activity">
                    <%@ include file="/WEB-INF/jsp/formEntity/formActivity.jsp"%>
                </div>
            </div>
            
            <!-- Display information about something -->
            <div v-if="pageType == 'showing'">
                <button v-if="person" class="btn btn-primary" v-on:click="goBack()">Retour arriere</button>
                <button v-if="show_modify_button" class="btn btn-primary" v-on:click="setPageType('modifying')">Modifier</button>
                <!-- If there is no actity -->
                <div v-if="!activity">
                    <!-- If there is no cv -->
                    <div v-if="!cv">
                        <!-- If there is no person -->
                        <div v-if="!person">
                        </div>
    
                        <!-- If there is a person -->
                        <div v-if="person">
                            <%@ include file="/WEB-INF/jsp/showEntity/showPerson.jsp"%>
                        </div>
                    </div>
    
                    <!-- If there is a cv -->
                    <div v-if="cv">
                        <%@ include file="/WEB-INF/jsp/showEntity/showCv.jsp"%>
                    </div>
                </div>
    
                <!-- If there is an actity -->
                <div v-if="activity">
                    <%@ include file="/WEB-INF/jsp/showEntity/showActivity.jsp"%>
                </div>
            </div>
            
            <!-- Display the list and its informations -->
            <div>
                <!-- If there is Element to list -->
                <div v-if="(list != null)">
                    <hr class="margin_small thick"/>
                    <div class="margin_big">
                        <%@ include file="/WEB-INF/jsp/general/listElement.jsp"%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<script src="${app}" type="module"></script>

<%@ include file="/WEB-INF/jsp/general/footer.jsp"%>