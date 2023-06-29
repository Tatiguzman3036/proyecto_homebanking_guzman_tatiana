const {createApp} = Vue;
const app = createApp({
    data(){
        return{
            type:"",
            color:"",
        }
    },
    created(){

    },
    methods:{
        createdCards(){
            console.log(this.type , this.color);
            axios.post('/api/clients/current/cards',`type=${this.type}&color=${this.color}`)
            .then(res =>{
                console.log("hola");
                console.log(res);
                window.location.href= '/web/pages/card.html'
            }).catch(err=> console.log(err))
        }
    }
})
app.mount('#app')