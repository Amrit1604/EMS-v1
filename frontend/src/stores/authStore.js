import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export const useAuthStore = create(
  persist(
    (set, get) => ({
      // State
      user: null,
      token: null,
      isAuthenticated: false,
      isLoading: false,
      
      // Actions
      login: (userData, token) => {
        set({
          user: userData,
          token,
          isAuthenticated: true,
          isLoading: false,
        });
      },
      
      logout: () => {
        set({
          user: null,
          token: null,
          isAuthenticated: false,
          isLoading: false,
        });
      },
      
      setLoading: (loading) => {
        set({ isLoading: loading });
      },
      
      updateUser: (userData) => {
        set({ user: { ...get().user, ...userData } });
      },
      
      // Getters
      getToken: () => get().token,
      getUser: () => get().user,
      hasRole: (role) => {
        const user = get().user;
        return user?.roles?.includes(role) || false;
      },
      
      hasAnyRole: (roles) => {
        const user = get().user;
        return roles.some(role => user?.roles?.includes(role)) || false;
      },
    }),
    {
      name: 'auth-storage',
      partialize: (state) => ({
        user: state.user,
        token: state.token,
        isAuthenticated: state.isAuthenticated,
      }),
    }
  )
);
