import React from 'react';
import { motion } from 'framer-motion';
import {
  Menu,
  Bell,
  Search,
  User,
  LogOut,
  Settings,
  ChevronDown
} from 'lucide-react';
import { useAuthStore } from '../../stores/authStore';

const Header = ({ onMenuClick, sidebarOpen }) => {
  const { user, logout } = useAuthStore();

  const handleLogout = () => {
    logout();
  };

  return (
    <header className="bg-white shadow-sm border-b border-gray-200 z-10">
      <div className="flex items-center justify-between px-6 py-4">
        {/* Left Section */}
        <div className="flex items-center space-x-4">
          <button
            onClick={onMenuClick}
            className="p-2 rounded-lg hover:bg-gray-100 transition-colors lg:hidden"
          >
            <Menu className="w-6 h-6 text-gray-600" />
          </button>
          
          <div className="hidden md:flex items-center space-x-4">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
              <input
                type="text"
                placeholder="Search employees, payroll..."
                className="pl-10 pr-4 py-2 w-80 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-200"
              />
            </div>
          </div>
        </div>

        {/* Right Section */}
        <div className="flex items-center space-x-4">
          {/* Notifications */}
          <motion.button
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            className="relative p-2 rounded-lg hover:bg-gray-100 transition-colors"
          >
            <Bell className="w-6 h-6 text-gray-600" />
            <span className="absolute -top-1 -right-1 w-5 h-5 bg-red-500 text-white text-xs rounded-full flex items-center justify-center">
              3
            </span>
          </motion.button>

          {/* User Menu */}
          <div className="relative group">
            <motion.button
              whileHover={{ scale: 1.02 }}
              className="flex items-center space-x-3 px-3 py-2 rounded-lg hover:bg-gray-100 transition-colors"
            >
              <div className="w-8 h-8 bg-gradient-to-r from-blue-500 to-purple-600 rounded-full flex items-center justify-center">
                <span className="text-white font-semibold text-sm">
                  {user?.firstName?.charAt(0) || 'U'}
                </span>
              </div>
              <div className="hidden md:block text-left">
                <p className="text-sm font-semibold text-gray-900">
                  {user?.firstName} {user?.lastName}
                </p>
                <p className="text-xs text-gray-500">{user?.role || 'Employee'}</p>
              </div>
              <ChevronDown className="w-4 h-4 text-gray-500" />
            </motion.button>

            {/* Dropdown Menu */}
            <motion.div
              initial={{ opacity: 0, y: -10 }}
              animate={{ opacity: 0, y: -10 }}
              whileHover={{ opacity: 1, y: 0 }}
              className="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg border border-gray-200 py-2 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200"
            >
              <a
                href="/profile"
                className="flex items-center space-x-3 px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition-colors"
              >
                <User className="w-4 h-4" />
                <span>Profile</span>
              </a>
              <a
                href="/settings"
                className="flex items-center space-x-3 px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 transition-colors"
              >
                <Settings className="w-4 h-4" />
                <span>Settings</span>
              </a>
              <hr className="my-2 border-gray-200" />
              <button
                onClick={handleLogout}
                className="flex items-center space-x-3 px-4 py-2 text-sm text-red-600 hover:bg-red-50 transition-colors w-full text-left"
              >
                <LogOut className="w-4 h-4" />
                <span>Sign Out</span>
              </button>
            </motion.div>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
