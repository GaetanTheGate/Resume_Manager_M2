<p><label>Prenom : </label><input type="text" v-model.trim="person.firstname" class="form-control"/></p>
<p><label>Nom : </label><input type="text" v-model.trim="person.name" class="form-control"/></p>
<p><label>Mail : </label><input type="text" v-model.trim="person.mail" class="form-control"/></p>
<p><label>Site web : </label><input type="text" v-model.trim="person.website" class="form-control"/></p>
<p><label>Date de naissance : </label><input type="date" v-model.trim="person.birthday" class="form-control"/></p>
<p><button v-on:click="savePerson()" class="btn btn-primary">Enregistrer</button></p>
<button v-on:click="createCv()" class="btn btn-warning">Ajouter un nouveau CV</button>