const myApp = {
    
    // Préparation des données
    data() {
        console.log("Data");
        return {
            axios:      null,
            person:     null,
            cv:         null,
            activity:   null,

            list:       null,
            show:       null,
            backList:   null,

            login:      null,
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

        this.closeLogin();
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
            this.persons = null;
            this.resetCV();
        },

        resetCV: function(){
            this.cvs = null;
            this.resetActivity();
        },

        resetActivity: function(){
            this.activities = null;
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
                this.setListAndShow([], this.getActivityShow);
            });
        },

        openLogin: function() {
            this.login = true;
        },

        closeLogin: function() {
            this.login = false;
        },

    }
}

const app = Vue.createApp(myApp);
app.mount('#myApp');