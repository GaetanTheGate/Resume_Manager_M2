<%@ include file="/WEB-INF/jsp/header.jsp"%>

<c:url var="app" value="/app.js" />

<div id="myApp">
    <!-- Nav bar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <a class="navbar-brand" href="${home}">LOGO</a>  
        <a class="navbar-brand dropdown-toggle" id="navbarDropdownMenuLink" href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Rechercher</a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
            <a class="dropdown-item" href="#" v-on:click="openSearchBarFor('person')">Personnes</a>
            <a class="dropdown-item" href="#" v-on:click="openSearchBarFor('cv')">CVs</a>
            <a class="dropdown-item" href="#" v-on:click="openSearchBarFor('activity')">Activitees</a>
        </div>        
        <a class="navbar-brand" href="#" v-on:click="openLogin()">Se connecter</a>
        <a class="navbar-brand d-inline-block align-top" href="${home}">Se deconnecter</a>
    </nav>

    <!-- Login section -->
    <div ref="test" class="form-popup" v-if="login">
        <form class="form-container">
          <h1>Se connecter</h1>
      
          <label for="email"><b>Identifiant</b></label>
          <input type="text" placeholder="identifiant" name="email" class="form-control" required>
      
          <label for="psw"><b>Mot de passe</b></label>
          <input type="password" placeholder="mot de passe" name="psw" class="form-control" required>
      
          <button class="btn">Login</button>
          <button class="btn cancel" v-on:click="closeLogin()">Close</button>
        </form>
    </div>

    <div class="container margin_small">

        <!-- Display search bar -->
        <div v-if="search != null">
            <div v-if="search == 'person'">
                <h1>Rechercher des personnes :</h1>
                <div class="form-group">
                    <label>Prenom :</label>
                    <input v-model.trim="elem1" class="form-control"/>
                </div>
                <div class="form-group">
                    <label>Nom :</label>
                    <input v-model.trim="elem2" class="form-control"/>
                </div>
                <div class="form-group">
                    <button v-on:click="searchPerson(elem1, elem2)" class="btn btn-primary">Rechercher</button>
                    <button v-on:click="closeSearchBar()" class="btn btn-secondary">Fermer recherche</button>
                </div>
            </div>

            <div v-if="search == 'cv'">
                <h1>Rechercher des CVs :</h1>
                <div class="form-group">
                    <label>Titre du CV :</label>
                    <input v-model.trim="elem1" class="form-control"/>
                </div>
                <div class="form-group">
                    <label>Description du CV :</label>
                    <input v-model.trim="elem2" class="form-control"/>
                </div>
                <div class="form-group">
                    <button v-on:click="searchCV(elem1, elem2)" class="btn btn-primary">Rechercher</button>
                    <button v-on:click="closeSearchBar()" class="btn btn-secondary">Fermer recherche</button>
                </div>
            </div>
            
            <div v-if="search == 'activity'">
                <h1>Rechercher des activitees :</h1>
                <div class="form-group">
                    <label>Titre de l'activitee :</label>
                    <input v-model.trim="elem1" class="form-control"/>
                </div>
                <div class="form-group">
                    <label>Annee minimum de l'activitee :</label>
                    <input v-model.trim="elem2" class="form-control"/>
                </div>
                <div class="form-group">
                    <button v-on:click="searchActivity(elem1, elem2)" class="btn btn-primary">Rechercher</button>
                    <button v-on:click="closeSearchBar()" class="btn btn-secondary">Fermer recherche</button>
                </div>
            </div>
            <hr class="margin_small thick"/>
        </div>

        <!-- Display information about something -->
        <div>
            <!-- If there is no actity -->
            <div v-if="!activity">

                <!-- If there is no cv -->
                <div v-if="!cv">

                    <!-- If there is no person -->
                    <div v-if="!person">

                    </div>

                    <!-- If there is a person -->
                    <div v-if="person">
                        <h2><b>{{person.name}}</b> {{person.firstname}}</h2>
                        <hr class="light"/>
                        <p v-if="person.mail"><u>Adresse electronique :</u> {{person.mail}}</p>
                        <p v-if="person.website"><u>Site web personnel :</u> <a :href="'http://' + person.website">{{person.website}}</a></p>
                    </div>
                </div>

                <!-- If there is a cv -->
                <div v-if="cv">
                    <h2><b>{{cv.title}}</b></h2>
                    <hr class="light"/>
                    <p v-if="cv.description" class="secondary_text">{{cv.description}}</p>
                </div>
            </div>

            <!-- If there is an actity -->
            <div v-if="activity">
                <h2><b>{{activity.title}}</b></h2>
                <hr class="light"/>
                <p v-if="activity.year"><u>Annee :</u> {{activity.year}}</p>
                <p v-if="activity.website"><u>Site web liee :</u> <a :href="'http://' + activity.website">{{activity.website}}</a></p>
                <p v-if="activity.description"><u>Description de l'activite :</u></p>
                <p v-if="activity.description" class="secondary_text">{{activity.description}}</p>
            </div>
        </div>
        
        <!-- Display the list and its informations -->
        <div>
            <!-- If there is Element to list -->
            <div v-if="(list != null)">
                <hr class="margin_small thick"/>
                <div class="margin_big">
                    <!-- If list isn't empty -->
                    <div v-if="(show != null)">
                        <table class="table">
                            <tr class ="thead-dark">
                                <th v-for="(item, key, index) in show.fields">{{item}}</th>
                            </tr>
                            <tr v-for="element in list" v-on:click="show.onClick(element.id)" class="clickable">
                                <td v-for="(item, key, index) in show.fields">{{element[key]}}</td>
                            </tr>
                        </table>
                    </div>
                    <!-- If list is empty -->
                    <div v-if="show == null">
    
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>


<script src="${app}" type="module"></script>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>