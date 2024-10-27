import './assets/main.css'

import vuetify from '@/vuetify'
import router from '@/router'
import { createApp } from 'vue'
import App from '@/App.vue'
import http from '@/api/http'

const app = createApp(App)

app.use(vuetify)
  .use(router)
  .mount('#app')

app.config.globalProperties.$gameApi = http;
