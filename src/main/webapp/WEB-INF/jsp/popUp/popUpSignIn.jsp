<div class="form-container">
    <h1>Se connecter</h1>

    <label><b>Identifiant</b></label>
    <input v-model="username" type="text" placeholder="identifiant" class="form-control" required>

    <label><b>Mot de passe</b></label>
    <input v-model="password" type="password" placeholder="mot de passe" class="form-control" required>

    <button class="btn" v-on:click="login(username, password)">Se connecter</button>
    <button class="btn cancel" v-on:click="closeLog()">Fermer</button>
  </div>