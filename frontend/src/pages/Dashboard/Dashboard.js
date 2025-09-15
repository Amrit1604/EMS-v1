import React from 'react';
import { motion } from 'framer-motion';
import {
  Users,
  DollarSign,
  TrendingUp,
  Calendar,
  Clock,
  CheckCircle,
  AlertCircle,
  BarChart3
} from 'lucide-react';
import StatsCard from '../../components/Dashboard/StatsCard';
import RecentActivity from '../../components/Dashboard/RecentActivity';
import PayrollChart from '../../components/Dashboard/PayrollChart';
import EmployeeChart from '../../components/Dashboard/EmployeeChart';
import QuickActions from '../../components/Dashboard/QuickActions';

const Dashboard = () => {
  // Mock data - replace with actual API calls
  const stats = [
    {
      title: 'Total Employees',
      value: '1,234',
      change: '+12%',
      changeType: 'positive',
      icon: Users,
      color: 'blue'
    },
    {
      title: 'Monthly Payroll',
      value: '₹45,67,890',
      change: '+8%',
      changeType: 'positive',
      icon: DollarSign,
      color: 'green'
    },
    {
      title: 'Pending Approvals',
      value: '23',
      change: '-15%',
      changeType: 'negative',
      icon: Clock,
      color: 'orange'
    },
    {
      title: 'Processed This Month',
      value: '1,156',
      change: '+5%',
      changeType: 'positive',
      icon: CheckCircle,
      color: 'purple'
    }
  ];

  const recentActivities = [
    {
      id: 1,
      type: 'payroll_processed',
      message: 'Payroll processed for Engineering Department',
      time: '2 hours ago',
      icon: CheckCircle,
      color: 'green'
    },
    {
      id: 2,
      type: 'employee_added',
      message: 'New employee John Smith added to HR Department',
      time: '4 hours ago',
      icon: Users,
      color: 'blue'
    },
    {
      id: 3,
      type: 'approval_pending',
      message: '15 payroll entries pending approval',
      time: '6 hours ago',
      icon: AlertCircle,
      color: 'orange'
    },
    {
      id: 4,
      type: 'report_generated',
      message: 'Monthly payroll report generated',
      time: '1 day ago',
      icon: BarChart3,
      color: 'purple'
    }
  ];

  return (
    <div className="space-y-6">
      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="flex flex-col sm:flex-row sm:items-center sm:justify-between"
      >
        <div>
          <h1 className="text-3xl font-bold text-gray-900 mb-2">Dashboard</h1>
          <p className="text-gray-600">Welcome back! Here's what's happening with your payroll system.</p>
        </div>
        <div className="mt-4 sm:mt-0 flex items-center space-x-3">
          <div className="flex items-center space-x-2 text-sm text-gray-500">
            <Calendar className="w-4 h-4" />
            <span>{new Date().toLocaleDateString('en-US', { 
              weekday: 'long', 
              year: 'numeric', 
              month: 'long', 
              day: 'numeric' 
            })}</span>
          </div>
        </div>
      </motion.div>

      {/* Stats Cards */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.1 }}
        className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6"
      >
        {stats.map((stat, index) => (
          <motion.div
            key={stat.title}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 0.1 + index * 0.1 }}
          >
            <StatsCard {...stat} />
          </motion.div>
        ))}
      </motion.div>

      {/* Charts and Quick Actions */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Payroll Chart */}
        <motion.div
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.5, delay: 0.3 }}
          className="lg:col-span-2"
        >
          <PayrollChart />
        </motion.div>

        {/* Quick Actions */}
        <motion.div
          initial={{ opacity: 0, x: 20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.5, delay: 0.4 }}
        >
          <QuickActions />
        </motion.div>
      </div>

      {/* Employee Chart and Recent Activity */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Employee Chart */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.5 }}
        >
          <EmployeeChart />
        </motion.div>

        {/* Recent Activity */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.6 }}
        >
          <RecentActivity activities={recentActivities} />
        </motion.div>
      </div>
    </div>
  );
};

export default Dashboard;
