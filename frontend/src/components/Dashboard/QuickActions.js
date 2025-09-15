import React from 'react';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import {
  UserPlus,
  DollarSign,
  FileText,
  Settings,
  Download,
  Upload
} from 'lucide-react';

const QuickActions = () => {
  const navigate = useNavigate();

  const actions = [
    {
      title: 'Add Employee',
      description: 'Register a new employee',
      icon: UserPlus,
      color: 'blue',
      onClick: () => navigate('/employees/new')
    },
    {
      title: 'Generate Payroll',
      description: 'Create monthly payroll',
      icon: DollarSign,
      color: 'green',
      onClick: () => navigate('/payroll/new')
    },
    {
      title: 'View Reports',
      description: 'Access payroll reports',
      icon: FileText,
      color: 'purple',
      onClick: () => navigate('/reports')
    },
    {
      title: 'System Settings',
      description: 'Configure system',
      icon: Settings,
      color: 'gray',
      onClick: () => navigate('/settings')
    },
    {
      title: 'Export Data',
      description: 'Download reports',
      icon: Download,
      color: 'orange',
      onClick: () => console.log('Export data')
    },
    {
      title: 'Import Data',
      description: 'Upload employee data',
      icon: Upload,
      color: 'indigo',
      onClick: () => console.log('Import data')
    }
  ];

  const colorClasses = {
    blue: 'bg-blue-500 hover:bg-blue-600',
    green: 'bg-green-500 hover:bg-green-600',
    purple: 'bg-purple-500 hover:bg-purple-600',
    gray: 'bg-gray-500 hover:bg-gray-600',
    orange: 'bg-orange-500 hover:bg-orange-600',
    indigo: 'bg-indigo-500 hover:bg-indigo-600'
  };

  return (
    <motion.div
      initial={{ opacity: 0, scale: 0.95 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{ duration: 0.5 }}
      className="bg-white rounded-xl shadow-soft p-6 border border-gray-100"
    >
      <div className="mb-6">
        <h3 className="text-lg font-semibold text-gray-900">Quick Actions</h3>
        <p className="text-sm text-gray-600">Frequently used operations</p>
      </div>

      <div className="space-y-3">
        {actions.map((action, index) => {
          const Icon = action.icon;
          return (
            <motion.button
              key={action.title}
              initial={{ opacity: 0, x: 20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: index * 0.1 }}
              whileHover={{ scale: 1.02, x: 5 }}
              whileTap={{ scale: 0.98 }}
              onClick={action.onClick}
              className="w-full flex items-center space-x-4 p-4 rounded-lg border border-gray-200 hover:border-gray-300 hover:shadow-md transition-all duration-200 group"
            >
              <div className={`w-10 h-10 ${colorClasses[action.color]} rounded-lg flex items-center justify-center transition-colors duration-200`}>
                <Icon className="w-5 h-5 text-white" />
              </div>
              <div className="flex-1 text-left">
                <h4 className="font-medium text-gray-900 group-hover:text-gray-700">
                  {action.title}
                </h4>
                <p className="text-sm text-gray-500">{action.description}</p>
              </div>
            </motion.button>
          );
        })}
      </div>
    </motion.div>
  );
};

export default QuickActions;
