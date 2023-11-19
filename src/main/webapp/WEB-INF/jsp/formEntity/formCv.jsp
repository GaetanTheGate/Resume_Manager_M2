<p><label>Titre :</label><input type="text" v-model.trim="cv.title" class="form-control"/></p>
<p><label>Description :</label><input type="text" v-model.trim="cv.description" class="form-control"/></p>
<p><button v-on:click="saveCv()" class="btn btn-primary">Enregistrer</button>
<button v-on:click="deleteCv()" class="btn btn-danger">Supprimer ce CV</button></p>
<button v-on:click="createActivity()" class="btn btn-warning">Ajouter une nouvelle activite</button>