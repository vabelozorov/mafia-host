import {createApiClient} from '@/api'

const http = createApiClient('http://localhost:8080/');

declare module '@vue/runtime-core' {
  interface ComponentCustomProperties {
    $http: typeof http; 
  }
}

export default http;
