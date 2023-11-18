const myApp = {
    
    // Préparation des données
    data() {
        console.log("Data");
        return {
            axios:      null,
            axiosLogin: null,
            
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
            timeout: 5000,
            headers: { 'Authorization': 'Bearer ' },
        });

        this.closeLogin();
        this.closeSignup();
        this.setNothing();
    },

    methods: {
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


        }
    }
}

const app = Vue.createApp(myApp);
app.mount('#myApp');