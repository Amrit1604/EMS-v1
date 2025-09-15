import React from 'react';
import { motion } from 'framer-motion';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  AreaChart,
  Area
} from 'recharts';

const PayrollChart = () => {
  const data = [
    { month: 'Jan', amount: 4200000, employees: 1180 },
    { month: 'Feb', amount: 4350000, employees: 1195 },
    { month: 'Mar', amount: 4180000, employees: 1205 },
    { month: 'Apr', amount: 4420000, employees: 1220 },
    { month: 'May', amount: 4380000, employees: 1215 },
    { month: 'Jun', amount: 4567890, employees: 1234 }
  ];

  const formatCurrency = (value) => {
    return `₹${(value / 100000).toFixed(1)}L`;
  };

  return (
    <motion.div
      initial={{ opacity: 0, scale: 0.95 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{ duration: 0.5 }}
      className="bg-white rounded-xl shadow-soft p-6 border border-gray-100"
    >
      <div className="flex items-center justify-between mb-6">
        <div>
          <h3 className="text-lg font-semibold text-gray-900">Payroll Trends</h3>
          <p className="text-sm text-gray-600">Monthly payroll expenses over time</p>
        </div>
        <div className="flex items-center space-x-4">
          <div className="flex items-center space-x-2">
            <div className="w-3 h-3 bg-blue-500 rounded-full"></div>
            <span className="text-sm text-gray-600">Payroll Amount</span>
          </div>
          <div className="flex items-center space-x-2">
            <div className="w-3 h-3 bg-green-500 rounded-full"></div>
            <span className="text-sm text-gray-600">Employee Count</span>
          </div>
        </div>
      </div>

      <div className="h-80">
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart data={data}>
            <defs>
              <linearGradient id="payrollGradient" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="#3B82F6" stopOpacity={0.3}/>
                <stop offset="95%" stopColor="#3B82F6" stopOpacity={0}/>
              </linearGradient>
            </defs>
            <CartesianGrid strokeDasharray="3 3" stroke="#f0f0f0" />
            <XAxis 
              dataKey="month" 
              axisLine={false}
              tickLine={false}
              tick={{ fontSize: 12, fill: '#6B7280' }}
            />
            <YAxis 
              yAxisId="amount"
              orientation="left"
              axisLine={false}
              tickLine={false}
              tick={{ fontSize: 12, fill: '#6B7280' }}
              tickFormatter={formatCurrency}
            />
            <YAxis 
              yAxisId="employees"
              orientation="right"
              axisLine={false}
              tickLine={false}
              tick={{ fontSize: 12, fill: '#6B7280' }}
            />
            <Tooltip
              contentStyle={{
                backgroundColor: 'white',
                border: '1px solid #e5e7eb',
                borderRadius: '8px',
                boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
              }}
              formatter={(value, name) => [
                name === 'amount' ? formatCurrency(value) : value,
                name === 'amount' ? 'Payroll Amount' : 'Employees'
              ]}
            />
            <Area
              yAxisId="amount"
              type="monotone"
              dataKey="amount"
              stroke="#3B82F6"
              strokeWidth={3}
              fill="url(#payrollGradient)"
            />
            <Line
              yAxisId="employees"
              type="monotone"
              dataKey="employees"
              stroke="#10B981"
              strokeWidth={2}
              dot={{ fill: '#10B981', strokeWidth: 2, r: 4 }}
              activeDot={{ r: 6, stroke: '#10B981', strokeWidth: 2 }}
            />
          </AreaChart>
        </ResponsiveContainer>
      </div>
    </motion.div>
  );
};

export default PayrollChart;
