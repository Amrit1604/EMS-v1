import React from 'react';
import { motion } from 'framer-motion';
import { TrendingUp, TrendingDown } from 'lucide-react';

const StatsCard = ({ title, value, change, changeType, icon: Icon, color }) => {
  const colorClasses = {
    blue: {
      bg: 'bg-blue-500',
      light: 'bg-blue-50',
      text: 'text-blue-600'
    },
    green: {
      bg: 'bg-green-500',
      light: 'bg-green-50',
      text: 'text-green-600'
    },
    orange: {
      bg: 'bg-orange-500',
      light: 'bg-orange-50',
      text: 'text-orange-600'
    },
    purple: {
      bg: 'bg-purple-500',
      light: 'bg-purple-50',
      text: 'text-purple-600'
    }
  };

  const colors = colorClasses[color] || colorClasses.blue;

  return (
    <motion.div
      whileHover={{ y: -5, scale: 1.02 }}
      transition={{ duration: 0.2 }}
      className="bg-white rounded-xl shadow-soft hover:shadow-medium p-6 border border-gray-100"
    >
      <div className="flex items-center justify-between">
        <div className="flex-1">
          <p className="text-sm font-medium text-gray-600 mb-1">{title}</p>
          <p className="text-2xl font-bold text-gray-900 mb-2">{value}</p>
          <div className="flex items-center space-x-1">
            {changeType === 'positive' ? (
              <TrendingUp className="w-4 h-4 text-green-500" />
            ) : (
              <TrendingDown className="w-4 h-4 text-red-500" />
            )}
            <span className={`text-sm font-medium ${
              changeType === 'positive' ? 'text-green-600' : 'text-red-600'
            }`}>
              {change}
            </span>
            <span className="text-sm text-gray-500">vs last month</span>
          </div>
        </div>
        <div className={`w-12 h-12 ${colors.light} rounded-lg flex items-center justify-center`}>
          <Icon className={`w-6 h-6 ${colors.text}`} />
        </div>
      </div>
    </motion.div>
  );
};

export default StatsCard;
