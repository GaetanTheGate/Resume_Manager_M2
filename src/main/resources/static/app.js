const myApp = {
    
    // Préparation des données
    data() {
        console.log("Data");
        return {
            axios:      null,
            axiosLogin: null,

            pageType:   null,
            
            person:     null,
            cv:         null,
            activity:   null,

            search:     null,

            list:       null,
            show:       null,
            backList:   null,

            show_login: null,
            show_signup:null,
        }
    },

    // Mise en place de l'application
    mounted() {
        console.log("Mounted");

        this.axios = axios.create({
            baseURL: 'http://localhost:8081/api/',
            timeout: 1000,
            headers: { 'Content-show': 'application/json' },
        });

        this.axiosLogin = axios.create({
            baseURL: 'http://localhost:8081/secu-users',
            timeout: 1000,
            headers: { 'Authorization': 'Bearer ' },
        });

        this.setPageType("showing");
        this.closeLog();
        if(this.person != null) console.log("TESSSSSSST");
        else this.setNothing();
    },

    methods: {
        setPageType: function(type){
            this.pageType = type;
        },

        setListAndShow: function(l, s){
            this.list = l;
            if(this.list.length != 0) this.show = s;
            else this.show = null;
        },

        getPersonShow: function(){
            let show = {};

            show.onClick = (id) => this.setPerson(id);
            
            show.fields = {
                name: "Nom",
                firstname: "Prenom",
                mail: "Mail",
            };

            return show;
        },

        getCVShow: function(){
            let show = {};

            show.onClick = (id) => this.setCV(id);
            
            show.fields = {
                title: "Titre",
                description: "Description",
            };

            return show;
        },

        getActivityShow: function(){
            let show = {};

            show.onClick = (id) => this.setActivity(id);
            
            show.fields = {
                title: "Titre",
                year: "Année",
            };

            return show;
        },

        resetPerson: function(){
            this.person = null;
            this.resetCV();
        },

        resetCV: function(){
            this.cv = null;
            this.resetActivity();
        },

        resetActivity: function(){
            this.activitie = null;
        },

        setNothing: function(){
            this.axios.get("persons").then(l => {
                this.resetPerson();
                this.setListAndShow(l.data, this.getPersonShow());
            });
        },

        setPerson: function(id){
            console.log("SetPerson");
            this.axios.get("persons/"+id).then(p => {
                this.resetCV();
                this.person = p.data;
                this.setListAndShow(this.person.cvs, this.getCVShow());
            });
        },

        setCV: function(id){
            console.log("SetCv");
            this.axios.get("cvs/"+id).then(c => {
                this.resetActivity();
                this.cv = c.data;
                this.setListAndShow(this.cv.activities, this.getActivityShow());
            });
        },

        setActivity: function(id){
            console.log("SetActivity");
            this.axios.get("activities/"+id).then(a => {
                this.activity = a.data;
                this.setListAndShow([], null);
            });
        },

        openLogin: function() {
            this.show_login = true;
            this.show_signup = false;
        },

        openSignup: function() {
            this.show_login = false;
            this.show_signup = true;
        },

        closeLog: function() {
            this.show_login = false;
            this.show_signup = false;
        },

        openSearchBarFor: function(name) {
            this.search = name;

            this.elem1=  '';
            this.elem2=  '';
        },

        closeSearchBar: function() {
            this.search = null
        },

        searchPerson: function(firstname, name) {
            this.axios.get("persons?firstname="+firstname+"&name="+name).then(l => {
                this.setListAndShow(l.data, this.getPersonShow());
            });
        },

        searchCV: function(title, description) {
            this.axios.get("cvs?title="+title+"&description="+description).then(l => {
                this.setListAndShow(l.data, this.getCVShow());
            });
        },

        searchActivity: function(title, year) {
            if(year == '') year = 0;
            this.axios.get("activities?title="+title+"&year="+year).then(l => {
                this.setListAndShow(l.data, this.getActivityShow());
            });
        },

        login: function(username, password){
            this.axiosLogin.post("/login?username="+username+"&password="+password, {}).then(s => {
                console.log(s);
            });
        },

        logout: function(){
            this.axiosLogin.get("/logout");
        },

        signup: function(username, password, password2){
            if(password != password2){
                return;
            }

            this.axiosLogin.post("/signup", {username:username, password:password, self:null}).then(s =>{
                console.log(s);
            });
        },

        isMySelf: function(person){
            return true;
        },

        isMyCv: function(cv){
            return true;
        },

        isMyActivity: function(activity){
            return true;
        },

        savePerson: function(){
            this.axios.put("persons", this.person).then(p => {
                this.person = p.data;
            });
        },

        saveCv: function(){
            this.axios.put("cv", this.cv).then(c => {
                this.cv = c.data;
            });
        },

        saveActivity: function(){
            this.axios.put("activities", this.activity).then(a => {
                this.activity = a.data;
            });
        },

        createPerson: function(){
            this.axios.post("cvs/", {}).then(p => {
                this.person = p.data;
            });
        },

        createCv: function(){
            this.axios.post("cvs/", {}).then(c => {
                this.cv = c.data;
            });
        },

        createActivity: function(){
            this.axios.post("activites/", {}).then(a => {
                this.activity = a.data;
            });
        },

        deletePerson: function(){
            if(confirm('Êtes-vous sûr de vouloir supprimer définitivement cette personne ?')){
                this.axios.delete("persons/"+this.person.id);
                this.person = null;
                console.log("SUPPRIMER")
            }
        },

        deleteCv: function(){
            if(confirm('Êtes-vous sûr de vouloir supprimer définitivement ce CV ?')){
                this.axios.delete("cvs/"+this.cv.id);
                this.cv = null;
                console.log("SUPPRIMER")
            }
        },

        deleteActivity: function(){
            if(confirm('Êtes-vous sûr de vouloir supprimer définitivement cette activité ?')){
                this.axios.delete("activities/"+this.activity.id);
                this.activity = null;
                console.log("SUPPRIMER")
            }
        },
    }
}

const app = Vue.createApp(myApp);
app.mount('#myApp');