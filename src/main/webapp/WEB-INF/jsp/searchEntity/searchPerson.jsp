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