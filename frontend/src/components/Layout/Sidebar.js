import React from 'react';
import { NavLink, useLocation } from 'react-router-dom';
import { motion } from 'framer-motion';
import {
  LayoutDashboard,
  Users,
  DollarSign,
  Building2,
  Briefcase,
  FileText,
  Settings,
  X,
  ChevronRight
} from 'lucide-react';
import { useAuthStore } from '../../stores/authStore';

const Sidebar = ({ onClose }) => {
  const location = useLocation();
  const { user, hasRole } = useAuthStore();

  const menuItems = [
    {
      name: 'Dashboard',
      path: '/dashboard',
      icon: LayoutDashboard,
      roles: ['HR', 'ADMIN', 'EMPLOYEE']
    },
    {
      name: 'Employees',
      path: '/employees',
      icon: Users,
      roles: ['HR', 'ADMIN']
    },
    {
      name: 'Payroll',
      path: '/payroll',
      icon: DollarSign,
      roles: ['HR', 'ADMIN']
    },
    {
      name: 'Departments',
      path: '/departments',
      icon: Building2,
      roles: ['HR', 'ADMIN']
    },
    {
      name: 'Designations',
      path: '/designations',
      icon: Briefcase,
      roles: ['HR', 'ADMIN']
    },
    {
      name: 'Reports',
      path: '/reports',
      icon: FileText,
      roles: ['HR', 'ADMIN']
    },
    {
      name: 'Settings',
      path: '/settings',
      icon: Settings,
      roles: ['HR', 'ADMIN', 'EMPLOYEE']
    }
  ];

  const filteredMenuItems = menuItems.filter(item => 
    item.roles.some(role => hasRole(role))
  );

  return (
    <div className="flex flex-col h-full bg-white shadow-xl">
      {/* Header */}
      <div className="flex items-center justify-between p-6 border-b border-gray-200">
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 bg-gradient-to-r from-blue-500 to-purple-600 rounded-lg flex items-center justify-center">
            <DollarSign className="w-6 h-6 text-white" />
          </div>
          <div>
            <h1 className="text-xl font-bold text-gray-900">PayrollPro</h1>
            <p className="text-sm text-gray-500">Employee Management</p>
          </div>
        </div>
        <button
          onClick={onClose}
          className="lg:hidden p-2 rounded-lg hover:bg-gray-100 transition-colors"
        >
          <X className="w-5 h-5 text-gray-500" />
        </button>
      </div>

      {/* User Info */}
      <div className="p-6 border-b border-gray-200">
        <div className="flex items-center space-x-3">
          <div className="w-12 h-12 bg-gradient-to-r from-green-400 to-blue-500 rounded-full flex items-center justify-center">
            <span className="text-white font-semibold text-lg">
              {user?.firstName?.charAt(0) || 'U'}
            </span>
          </div>
          <div>
            <p className="font-semibold text-gray-900">
              {user?.firstName} {user?.lastName}
            </p>
            <p className="text-sm text-gray-500">{user?.role || 'Employee'}</p>
          </div>
        </div>
      </div>

      {/* Navigation */}
      <nav className="flex-1 p-4 space-y-2">
        {filteredMenuItems.map((item, index) => {
          const Icon = item.icon;
          const isActive = location.pathname.startsWith(item.path);
          
          return (
            <motion.div
              key={item.name}
              initial={{ opacity: 0, x: -20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: index * 0.1 }}
            >
              <NavLink
                to={item.path}
                className={`flex items-center justify-between px-4 py-3 rounded-lg transition-all duration-200 group ${
                  isActive
                    ? 'bg-gradient-to-r from-blue-500 to-purple-600 text-white shadow-lg'
                    : 'text-gray-700 hover:bg-gray-100 hover:text-gray-900'
                }`}
              >
                <div className="flex items-center space-x-3">
                  <Icon className={`w-5 h-5 ${isActive ? 'text-white' : 'text-gray-500'}`} />
                  <span className="font-medium">{item.name}</span>
                </div>
                <ChevronRight 
                  className={`w-4 h-4 transition-transform duration-200 ${
                    isActive ? 'text-white rotate-90' : 'text-gray-400 group-hover:translate-x-1'
                  }`} 
                />
              </NavLink>
            </motion.div>
          );
        })}
      </nav>

      {/* Footer */}
      <div className="p-6 border-t border-gray-200">
        <div className="text-center">
          <p className="text-xs text-gray-500">
            © 2024 PayrollPro. All rights reserved.
          </p>
          <p className="text-xs text-gray-400 mt-1">
            Version 1.0.0
          </p>
        </div>
      </div>
    </div>
  );
};

export default Sidebar;
