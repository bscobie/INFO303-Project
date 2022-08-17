var accountsApi = '//localhost:8080/api/account';

const app = Vue.createApp({

    data() {
        return {
            account: new Object()
        };
    },

    mounted() {
            
    },

    methods: {
        addAccount() {
            axios.post(accountsApi, this.account)
            .catch(error => {
                console.error(error);
                alert("An error occurred - check the console for details.");
            });
        }
    }

});

// mount the page at the <main> tag - this needs to be the last line in the file
app.mount("main");