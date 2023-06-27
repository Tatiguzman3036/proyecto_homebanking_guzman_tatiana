/* const $btnSignIn= document.querySelector('.sign-in-btn'),
      $btnSignUp = document.querySelector('.sign-up-btn'),  
      $signUp = document.querySelector('.sign-up'),
      $signIn  = document.querySelector('.sign-in');

document.addEventListener('click', e => {
    if (e.target === $btnSignIn || e.target === $btnSignUp) {
        $signIn.classList.toggle('active');
        $signUp.classList.toggle('active')
    }
});  */

const {createApp} = Vue;

const app = createApp({
    data(){
        return{
        signInActive: false,
        signUpActive: true,
        previousState: false,
        showSecondForm: false,
        email: "",
        password: "",
        firstName:"",
        lastName:"",
        emailRegister: "",
        passwordRegister: "",
        }
    },
    created(){
        
    },
    methods:{
        toggleForm() {
            this.previousState = this.signInActive;
            this.signInActive = !this.signInActive;
            this.signUpActive = !this.signUpActive;
            this.showSecondForm = true;
          },
          toggleBack() {
            this.signInActive = this.previousState;
            this.signUpActive = !this.previousState;
            this.showSecondForm = false
          },
          login(){
            if (this.email && this.password) {
                axios.post('/api/login',`email=${this.email}&password=${this.password}`,
                {headers:{'content-type':'application/x-www-form-urlencoded'}})
                .then((res) => {
                    console.log('signed in!!!')
                    window.location.href = "/web/pages/accounts.html"
                }).catch(error => console.log(error))
            } else {
                alert("opps..")
            }
          },
          register(){
            console.log(this.lastName,this.firstName,this.passwordRegister,this.emailRegister);
            if (this.firstName && this.lastName && this.emailRegister && this.passwordRegister) {
                axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.emailRegister}&password=${this.passwordRegister}`)
                .then(res =>{
                    axios.post('/api/login',`email=${this.emailRegister}&password=${this.passwordRegister}`)
                    .then(res=>{
                        window.location.href = "/web/pages/accounts.html"
                        console.log('Inicio de sesión exitoso:', res.data);
                    }).catch(error =>console.log(error))
                }).catch(error =>console.log(error))
            }else{
                alert(`An error occurred, please re-enter your information.`)
            }
          }
        },
} )
app.mount('#app')
/* axios.post('/api/login',"email=melba@mindhub.com&password=melba",{headers:{'content-type':'application/x-www-form-urlencoded'}}).then(response => console.log('signed in!!!')) */
