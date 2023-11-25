const myApp = {
    // watch: {
    //     person() { this.updateUri() },
    //     cv() { this.updateUri() },
    //     activity() { this.updateUri() },
    // },
    
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

            show_modify_button: null,

            token:   null,
        }
    },

    // Mise en place de l'application
    mounted() {
        console.log("Mounted");

        this.axios = axios.create({
            baseURL: 'http://localhost:8081/api/',
            timeout: 5000,
            headers: { 'Content-show': 'application/json' },
        });

        this.axiosLogin = axios.create({
            baseURL: 'http://localhost:8081/secu-users/',
            timeout: 5000,
        });

        this.setPageType("showing");
        this.closeLog();
        this.setNothing();
    },

    methods: {
        setPageType: function(type){
            this.pageType = type;
        },

        goBack: function(){
            if(this.activity)
                this.setCV(this.cv.id);
            else if(this.cv)
                this.setPerson(this.person.id);
            else if(this.person)
                this.setNothing();
        },

        setListAndShow: function(l, s){
            this.list = l;
            if(this.list.length != 0) this.show = s;
            else this.show = null;
        },

        getPersonShow: function(){
            let show = {};
            show.title = "Liste d'individus";

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
            show.title = "Liste de CV";

            show.onClick = (id) => this.setCV(id);
            
            show.fields = {
                title: "Titre",
                description: "Description",
            };

            return show;
        },

        getActivityShow: function(){
            let show = {};
            show.title = "Liste d'activité";

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
            this.activity = null;
        },

        setNothing: function(){
            this.axios.get("persons").then(l => {
                this.resetPerson();
                this.setListAndShow(l.data, this.getPersonShow());
                
                this.show_modify_button = false;
            });
        },

        setPerson: function(id){
            this.axios.get("persons/"+id).then(p => {
                this.resetPerson();
                this.person = p.data;
                this.setListAndShow(this.person.cvs, this.getCVShow());

                this.computeShowModifyButton();
            });
        },

        setCV: function(id){
            this.axios.get("cvs/"+id).then(c => {
                this.resetActivity();
                this.cv = c.data;
                this.setListAndShow(this.cv.activities, this.getActivityShow());
                
                this.computeShowModifyButton();
            });
        },

        setActivity: function(id){
            this.axios.get("activities/"+id).then(a => {
                this.activity = a.data;
                this.setListAndShow([], null);
                
                this.computeShowModifyButton();
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

            this.elem1='';
            this.elem2='';
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
            try {
                this.axiosLogin.post("login?username="+username+"&password="+password, {}).then(s => {
                    this.token = s.data;

                    this.closeLog();
                    this.computeShowModifyButton();
                });
            } catch (error) {
                console.log(error);
            }
            
        },

        logout: function(){
            this.axiosLogin.get("logout", { Authorization:"Bearer " + this.token }).then(() => {
                this.token = null;

                this.computeShowModifyButton();
            });
        },

        signup: async function(username, password, password2){
            if(password != password2){
                return;
            }

            const s = await this.axiosLogin.post("signup", {"username":username, "password":password});

            this.token = s.data;
            this.closeLog();

            const u = await this.axiosLogin.get("me", { headers : { Authorization:"Bearer " + this.token }});
            this.setPerson(u.data.self.id);
        },

        savePerson: function(){
            this.axios.put("persons", this.person, { headers : { Authorization:"Bearer " + this.token }}).then(p => {
                this.person = p.data;
            });
        },

        saveCv: function(){
            this.axios.put("cvs", this.cv, { headers : { Authorization:"Bearer " + this.token }}).then(c => {
                this.cv = c.data;
            });
        },

        saveActivity: function(){
            this.axios.put("activities", this.activity, { headers : { Authorization:"Bearer " + this.token }}).then(a => {
                this.activity = a.data;
            });
        },

        createPerson: function(){
            let person = {
                "name":"default",
                "firstname":"default",
                "cvs":[],
                "self": this.person,
            };

            this.axios.post("persons", person, { headers : { Authorization:"Bearer " + this.token }}).then(p => {
                this.setPerson(p.data.id)
            });
        },

        createCv: function(){
            let cv = {
                "title":"default",
                "description":"default",
                "activities":[],
                "owner": this.person,
            };

            this.axios.post("cvs", cv, { headers : { Authorization:"Bearer " + this.token }}).then(c => {
                this.setCV(c.data.id);
            });
        },

        createActivity: function(){
            let activity = {
                "year":2000,
                "type":"none",
                "title":"default",
                "description":"default",
                "cv": this.cv,
            };

            this.axios.post("activities", activity, { headers : { Authorization:"Bearer " + this.token }}).then(a => {
                this.setActivity(a.data.id);
            });
        },

        deletePerson: async function(){
            if(confirm('Êtes-vous sûr de vouloir supprimer définitivement cette personne ?')){
                await Promise.all(this.person.cvs.map( element =>
                    this.axios.delete("cvs/"+element.id, { headers : { Authorization:"Bearer " + this.token }})
                ));

                this.axios.delete("persons/"+this.person.id, { headers : { Authorization:"Bearer " + this.token }}).then(() => {
                    this.setNothing();
                });
            }
        },

        deleteCv: async function(){
            if(confirm('Êtes-vous sûr de vouloir supprimer définitivement ce CV ?')){
                await Promise.all(this.cv.activities.map( element => 
                    this.axios.delete("activities/"+element.id, { headers : { Authorization:"Bearer " + this.token }})
                ));

                this.axios.delete("cvs/"+this.cv.id, { headers : { Authorization:"Bearer " + this.token }}).then(() => {
                    this.setPerson(this.person.id);
                });
            }
        },

        deleteActivity: function(){
            if(confirm('Êtes-vous sûr de vouloir supprimer définitivement cette activitée ?')){
                this.axios.delete("activities/"+this.activity.id, { headers : { Authorization:"Bearer " + this.token }}).then(() => {
                    this.setCV(this.cv.id);
                });
            }
        },

        computeShowModifyButton: function(){
            if ( !(this.person && this.token)) {
                this.show_modify_button = false;
                this.setPageType("showing");
            }
            else {
                this.show_modify_button = false;
                this.axiosLogin.get("me/role/ADMIN", { headers : { Authorization:"Bearer " + this.token }}).then(b => {
                    if(b.data)
                        this.show_modify_button = true;

                    else
                        this.axiosLogin.get("me", { headers : { Authorization:"Bearer " + this.token }}).then(u => {
                            let user = u.data;
                        
                            if(user.self)
                                this.show_modify_button = this.person.id == user.self.id;

                            if(!this.show_modify_button)
                                this.setPageType("showing");
                            });
                });
            }
        },

        updateUri() {
            let s = "";
            if(this.person)
                s += "/person/"+this.person.id;
            if(this.cv)
                s += "/cv/"+this.cv.id;
            if(this.activity)
                s += "/activity/"+this.activity.id;

            window.location.href = "http://localhost:8081/app" + s;
        },
    }
}

const app = Vue.createApp(myApp);
app.mount('#myApp');