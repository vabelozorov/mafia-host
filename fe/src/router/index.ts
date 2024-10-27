import { createRouter, createWebHistory } from 'vue-router'
import StartGame from '@/views/StartGame.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: StartGame
    },
    {
      path: '/games/:id/assign',
      name: 'assignRoles',
      component: () => import('@/views/AssignRoles.vue'),
      props: route => ({id: Number(route.params.id)})
    },    
    {
      path: '/games/:id/firstNight',
      name: 'firstNight',
      component: () => import('@/views/FirstNight.vue'),
      props: route => ({id: Number(route.params.id)})
    },
  ]
})

export default router
