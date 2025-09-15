import React from 'react';
import { motion } from 'framer-motion';

const RecentActivity = ({ activities }) => {
  const getTimeAgo = (time) => {
    return time;
  };

  return (
    <motion.div
      initial={{ opacity: 0, scale: 0.95 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{ duration: 0.5 }}
      className="bg-white rounded-xl shadow-soft p-6 border border-gray-100"
    >
      <div className="mb-6">
        <h3 className="text-lg font-semibold text-gray-900">Recent Activity</h3>
        <p className="text-sm text-gray-600">Latest system activities and updates</p>
      </div>

      <div className="space-y-4">
        {activities.map((activity, index) => {
          const Icon = activity.icon;
          const colorClasses = {
            green: 'bg-green-100 text-green-600',
            blue: 'bg-blue-100 text-blue-600',
            orange: 'bg-orange-100 text-orange-600',
            purple: 'bg-purple-100 text-purple-600'
          };

          return (
            <motion.div
              key={activity.id}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: index * 0.1 }}
              className="flex items-start space-x-4 p-4 rounded-lg hover:bg-gray-50 transition-colors duration-200"
            >
              <div className={`w-10 h-10 ${colorClasses[activity.color]} rounded-lg flex items-center justify-center flex-shrink-0`}>
                <Icon className="w-5 h-5" />
              </div>
              <div className="flex-1 min-w-0">
                <p className="text-sm font-medium text-gray-900 mb-1">
                  {activity.message}
                </p>
                <p className="text-xs text-gray-500">{getTimeAgo(activity.time)}</p>
              </div>
            </motion.div>
          );
        })}
      </div>

      <div className="mt-6 pt-4 border-t border-gray-200">
        <button className="w-full text-center text-sm text-blue-600 hover:text-blue-700 font-medium transition-colors duration-200">
          View All Activities
        </button>
      </div>
    </motion.div>
  );
};

export default RecentActivity;
