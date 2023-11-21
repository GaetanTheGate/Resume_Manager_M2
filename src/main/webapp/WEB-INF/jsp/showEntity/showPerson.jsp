<h2><b>{{person.name}}</b> {{person.firstname}}</h2>
<hr class="light"/>
<p v-if="person.mail"><u>Adresse electronique :</u> {{person.mail}}</p>
<p v-if="person.website"><u>Site web personnel :</u> <a :href="'http://' + person.website">{{person.website}}</a></p>
<p v-if="person.birthdate"><u>Date de naissance :</u> {{person.birthdate}}</p>
                    