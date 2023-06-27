const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            account: [],
            id: "",
            transactions:[],

        }
    },
    created(){
        axios.get(`http://localhost:8080/api/clients/current`)
        .then(res =>{
            this.account = res.data.accounts

            console.log(this.account);
        }).catch(error => console.log(error))
    },
    methods:{
        signOut(){
            axios.post('/api/logout')
            .then(response => {
                console.log('signed out!!!')
                window.location.href = "/web/pages/login.html"
            }).catch(error => console.log(error))
        }

    }
})
app.mount('#app')