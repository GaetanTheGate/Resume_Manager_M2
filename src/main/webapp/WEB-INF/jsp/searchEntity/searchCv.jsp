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