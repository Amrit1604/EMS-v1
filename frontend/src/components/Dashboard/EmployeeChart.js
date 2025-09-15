import React from 'react';
import { motion } from 'framer-motion';
import {
  PieChart,
  Pie,
  Cell,
  ResponsiveContainer,
  Tooltip,
  Legend
} from 'recharts';

const EmployeeChart = () => {
  const data = [
    { name: 'Engineering', value: 450, color: '#3B82F6' },
    { name: 'Sales', value: 280, color: '#10B981' },
    { name: 'Marketing', value: 180, color: '#F59E0B' },
    { name: 'HR', value: 120, color: '#EF4444' },
    { name: 'Finance', value: 95, color: '#8B5CF6' },
    { name: 'Operations', value: 109, color: '#06B6D4' }
  ];

  const RADIAN = Math.PI / 180;
  const renderCustomizedLabel = ({
    cx, cy, midAngle, innerRadius, outerRadius, percent
  }) => {
    const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
    const x = cx + radius * Math.cos(-midAngle * RADIAN);
    const y = cy + radius * Math.sin(-midAngle * RADIAN);

    return (
      <text 
        x={x} 
        y={y} 
        fill="white" 
        textAnchor={x > cx ? 'start' : 'end'} 
        dominantBaseline="central"
        fontSize={12}
        fontWeight="600"
      >
        {`${(percent * 100).toFixed(0)}%`}
      </text>
    );
  };

  return (
    <motion.div
      initial={{ opacity: 0, scale: 0.95 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{ duration: 0.5 }}
      className="bg-white rounded-xl shadow-soft p-6 border border-gray-100"
    >
      <div className="mb-6">
        <h3 className="text-lg font-semibold text-gray-900">Employee Distribution</h3>
        <p className="text-sm text-gray-600">Employees by department</p>
      </div>

      <div className="h-80">
        <ResponsiveContainer width="100%" height="100%">
          <PieChart>
            <Pie
              data={data}
              cx="50%"
              cy="50%"
              labelLine={false}
              label={renderCustomizedLabel}
              outerRadius={100}
              fill="#8884d8"
              dataKey="value"
            >
              {data.map((entry, index) => (
                <Cell key={`cell-${index}`} fill={entry.color} />
              ))}
            </Pie>
            <Tooltip
              contentStyle={{
                backgroundColor: 'white',
                border: '1px solid #e5e7eb',
                borderRadius: '8px',
                boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
              }}
              formatter={(value, name) => [value, 'Employees']}
            />
            <Legend
              verticalAlign="bottom"
              height={36}
              iconType="circle"
              wrapperStyle={{
                paddingTop: '20px',
                fontSize: '14px'
              }}
            />
          </PieChart>
        </ResponsiveContainer>
      </div>

      <div className="mt-4 grid grid-cols-2 gap-4">
        {data.map((item, index) => (
          <motion.div
            key={item.name}
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: index * 0.1 }}
            className="flex items-center justify-between p-3 bg-gray-50 rounded-lg"
          >
            <div className="flex items-center space-x-3">
              <div 
                className="w-3 h-3 rounded-full"
                style={{ backgroundColor: item.color }}
              ></div>
              <span className="text-sm font-medium text-gray-700">{item.name}</span>
            </div>
            <span className="text-sm font-semibold text-gray-900">{item.value}</span>
          </motion.div>
        ))}
      </div>
    </motion.div>
  );
};

export default EmployeeChart;
