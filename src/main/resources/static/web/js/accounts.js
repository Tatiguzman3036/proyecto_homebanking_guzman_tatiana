const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            client1:[],
            mensaje: "hola",
        }
    },
    created(){
        this.loadData()
    },
    methods:{
        loadData(){
            axios.get("http://localhost:8080/api/clients/2")
            .then(res => {
                this.client1 = res.data
            
                console.log(this.client1);
            })
            .catch(error => console.log(error))
        },
    }
});
app.mount('#app')