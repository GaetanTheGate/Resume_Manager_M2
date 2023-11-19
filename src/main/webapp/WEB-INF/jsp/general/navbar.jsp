<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <a class="navbar-brand" href="${home}">LOGO</a>  
    <a class="navbar-brand dropdown-toggle" id="navbarDropdownMenuLink" href="#" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Rechercher</a>
    <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
        <a class="dropdown-item" href="#" v-on:click="openSearchBarFor('person')">Personnes</a>
        <a class="dropdown-item" href="#" v-on:click="openSearchBarFor('cv')">CVs</a>
        <a class="dropdown-item" href="#" v-on:click="openSearchBarFor('activity')">Activitees</a>
    </div>        
    <a class="navbar-brand" href="#" v-on:click="openLogin()">Se connecter</a>
    <a class="navbar-brand" href="#" v-on:click="openSignup()">S'inscrire</a>
    <a class="navbar-brand d-inline-block align-top" href="${home}">Se deconnecter</a>
</nav>