<div class="form-container">
    <h1>Creer un compte</h1>

    <label><b>Identifiant</b></label>
    <input v-model="username" type="text" placeholder="identifiant" class="form-control" required>

    <label><b>Mot de passe</b></label>
    <input v-model="password" type="password" placeholder="mot de passe" class="form-control" required>

    <label><b>Verifier votre mot de passe</b></label>
    <input v-model="password2" type="password" placeholder="mot de passe" class="form-control" required>

    <button class="btn" v-on:click="signup(username, password, password2)">S'inscrire</button>
    <button class="btn cancel" v-on:click="closeLog()">Fermer</button>
</div>