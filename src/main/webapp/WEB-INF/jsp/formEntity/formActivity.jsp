<p><label>Titre :</label><input type="text" v-model.trim="activity.title" class="form-control"/></p>
<p><label>Type :</label><input type="text" v-model.trim="activity.type" class="form-control"/></p>
<p><label>Annee :</label><input type="number" inputmode="numeric" v-model.trim="activity.year" class="form-control"/></p>
<p><label>Site web :</label><input type="text" v-model.trim="activity.website" class="form-control"/></p>
<p><label>Description :</label><input type="text" v-model.trim="activity.description" class="form-control"/></p>
<p><button v-on:click="saveActivity()" class="btn btn-primary">Enregistrer</button>
<button v-on:click="deleteActivity()" class="btn btn-danger">Supprimer cette activite</button></p>