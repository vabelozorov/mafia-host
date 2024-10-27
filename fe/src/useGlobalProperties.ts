import { getCurrentInstance } from 'vue';

export function useGameApi() {
  const instance = getCurrentInstance();
  if (!instance) {
    throw new Error('useGlobalProperties must be called within a setup function');
  }
  
  return instance.appContext.config.globalProperties.$gameApi;
}