<h2><b>{{activity.title}}</b></h2>
<hr class="light"/>
<p v-if="activity.year"><u>Annee :</u> {{activity.year}}</p>
<p v-if="activity.website"><u>Site web liee :</u> <a :href="'http://' + activity.website">{{activity.website}}</a></p>
<p v-if="activity.description"><u>Description de l'activite :</u></p>
<p v-if="activity.description" class="secondary_text">{{activity.description}}</p>