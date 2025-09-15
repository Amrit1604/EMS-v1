import React, { useState } from 'react';
import { motion } from 'framer-motion';
import {
  FileText,
  Download,
  Calendar,
  Filter,
  BarChart3,
  PieChart,
  TrendingUp,
  Users,
  DollarSign,
  Building
} from 'lucide-react';

const Reports = () => {
  const [selectedReport, setSelectedReport] = useState('payroll');
  const [dateRange, setDateRange] = useState('last-month');
  const [department, setDepartment] = useState('all');

  const reportTypes = [
    {
      id: 'payroll',
      name: 'Payroll Report',
      description: 'Detailed payroll information and salary breakdowns',
      icon: DollarSign,
      color: 'green'
    },
    {
      id: 'employee',
      name: 'Employee Report',
      description: 'Employee demographics and organizational data',
      icon: Users,
      color: 'blue'
    },
    {
      id: 'department',
      name: 'Department Report',
      description: 'Department-wise analytics and performance metrics',
      icon: Building,
      color: 'purple'
    },
    {
      id: 'attendance',
      name: 'Attendance Report',
      description: 'Employee attendance and leave patterns',
      icon: Calendar,
      color: 'orange'
    }
  ];

  const dateRanges = [
    { value: 'last-week', label: 'Last Week' },
    { value: 'last-month', label: 'Last Month' },
    { value: 'last-quarter', label: 'Last Quarter' },
    { value: 'last-year', label: 'Last Year' },
    { value: 'custom', label: 'Custom Range' }
  ];

  const departments = ['Engineering', 'HR', 'Sales', 'Marketing', 'Finance'];

  const mockReportData = {
    payroll: {
      totalAmount: 4567890,
      employeeCount: 1234,
      avgSalary: 3700,
      breakdown: [
        { category: 'Basic Salary', amount: 3200000, percentage: 70 },
        { category: 'Allowances', amount: 912000, percentage: 20 },
        { category: 'Overtime', amount: 228000, percentage: 5 },
        { category: 'Bonuses', amount: 227890, percentage: 5 }
      ]
    },
    employee: {
      totalEmployees: 1234,
      newHires: 45,
      departures: 12,
      departments: [
        { name: 'Engineering', count: 450, percentage: 36.5 },
        { name: 'Sales', count: 280, percentage: 22.7 },
        { name: 'Marketing', count: 180, percentage: 14.6 },
        { name: 'HR', count: 120, percentage: 9.7 },
        { name: 'Finance', count: 95, percentage: 7.7 },
        { name: 'Operations', count: 109, percentage: 8.8 }
      ]
    }
  };

  const getColorClasses = (color) => {
    const colors = {
      green: 'bg-green-100 text-green-600 border-green-200',
      blue: 'bg-blue-100 text-blue-600 border-blue-200',
      purple: 'bg-purple-100 text-purple-600 border-purple-200',
      orange: 'bg-orange-100 text-orange-600 border-orange-200'
    };
    return colors[color] || colors.blue;
  };

  const generateReport = () => {
    console.log('Generating report:', { selectedReport, dateRange, department });
    // Simulate report generation
  };

  const exportReport = (format) => {
    console.log('Exporting report as:', format);
    // Simulate export functionality
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="flex flex-col sm:flex-row sm:items-center sm:justify-between"
      >
        <div className="flex items-center space-x-3">
          <div className="w-10 h-10 bg-indigo-100 rounded-lg flex items-center justify-center">
            <FileText className="w-6 h-6 text-indigo-600" />
          </div>
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Reports & Analytics</h1>
            <p className="text-gray-600">Generate comprehensive business reports</p>
          </div>
        </div>
      </motion.div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Report Configuration */}
        <motion.div
          initial={{ opacity: 0, x: -20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.5, delay: 0.1 }}
          className="lg:col-span-1 space-y-6"
        >
          {/* Report Type Selection */}
          <div className="bg-white rounded-xl shadow-soft p-6 border border-gray-100">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Report Type</h3>
            <div className="space-y-3">
              {reportTypes.map((report) => {
                const Icon = report.icon;
                return (
                  <motion.button
                    key={report.id}
                    whileHover={{ scale: 1.02 }}
                    whileTap={{ scale: 0.98 }}
                    onClick={() => setSelectedReport(report.id)}
                    className={`w-full p-4 rounded-lg border-2 transition-all duration-200 ${
                      selectedReport === report.id
                        ? getColorClasses(report.color)
                        : 'border-gray-200 hover:border-gray-300'
                    }`}
                  >
                    <div className="flex items-center space-x-3">
                      <Icon className="w-5 h-5" />
                      <div className="text-left">
                        <div className="font-medium">{report.name}</div>
                        <div className="text-sm opacity-75">{report.description}</div>
                      </div>
                    </div>
                  </motion.button>
                );
              })}
            </div>
          </div>

          {/* Filters */}
          <div className="bg-white rounded-xl shadow-soft p-6 border border-gray-100">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Filters</h3>
            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Date Range
                </label>
                <select
                  value={dateRange}
                  onChange={(e) => setDateRange(e.target.value)}
                  className="input-field"
                >
                  {dateRanges.map(range => (
                    <option key={range.value} value={range.value}>{range.label}</option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Department
                </label>
                <select
                  value={department}
                  onChange={(e) => setDepartment(e.target.value)}
                  className="input-field"
                >
                  <option value="all">All Departments</option>
                  {departments.map(dept => (
                    <option key={dept} value={dept}>{dept}</option>
                  ))}
                </select>
              </div>
            </div>

            <div className="mt-6 space-y-3">
              <motion.button
                whileHover={{ scale: 1.05 }}
                whileTap={{ scale: 0.95 }}
                onClick={generateReport}
                className="w-full btn-primary flex items-center justify-center space-x-2"
              >
                <BarChart3 className="w-4 h-4" />
                <span>Generate Report</span>
              </motion.button>

              <div className="flex space-x-2">
                <motion.button
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                  onClick={() => exportReport('pdf')}
                  className="flex-1 btn-secondary flex items-center justify-center space-x-2"
                >
                  <Download className="w-4 h-4" />
                  <span>PDF</span>
                </motion.button>
                <motion.button
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                  onClick={() => exportReport('excel')}
                  className="flex-1 btn-secondary flex items-center justify-center space-x-2"
                >
                  <Download className="w-4 h-4" />
                  <span>Excel</span>
                </motion.button>
              </div>
            </div>
          </div>
        </motion.div>

        {/* Report Preview */}
        <motion.div
          initial={{ opacity: 0, x: 20 }}
          animate={{ opacity: 1, x: 0 }}
          transition={{ duration: 0.5, delay: 0.2 }}
          className="lg:col-span-2"
        >
          <div className="bg-white rounded-xl shadow-soft p-6 border border-gray-100">
            <div className="flex items-center justify-between mb-6">
              <h3 className="text-lg font-semibold text-gray-900">
                {reportTypes.find(r => r.id === selectedReport)?.name} Preview
              </h3>
              <div className="flex items-center space-x-2 text-sm text-gray-500">
                <Calendar className="w-4 h-4" />
                <span>Last updated: {new Date().toLocaleDateString()}</span>
              </div>
            </div>

            {selectedReport === 'payroll' && (
              <div className="space-y-6">
                {/* Summary Cards */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div className="bg-gradient-to-r from-green-50 to-green-100 p-4 rounded-lg">
                    <div className="flex items-center justify-between">
                      <div>
                        <p className="text-sm text-green-600 font-medium">Total Payroll</p>
                        <p className="text-2xl font-bold text-green-900">
                          ₹{mockReportData.payroll.totalAmount.toLocaleString()}
                        </p>
                      </div>
                      <DollarSign className="w-8 h-8 text-green-600" />
                    </div>
                  </div>
                  <div className="bg-gradient-to-r from-blue-50 to-blue-100 p-4 rounded-lg">
                    <div className="flex items-center justify-between">
                      <div>
                        <p className="text-sm text-blue-600 font-medium">Employees</p>
                        <p className="text-2xl font-bold text-blue-900">
                          {mockReportData.payroll.employeeCount}
                        </p>
                      </div>
                      <Users className="w-8 h-8 text-blue-600" />
                    </div>
                  </div>
                  <div className="bg-gradient-to-r from-purple-50 to-purple-100 p-4 rounded-lg">
                    <div className="flex items-center justify-between">
                      <div>
                        <p className="text-sm text-purple-600 font-medium">Avg Salary</p>
                        <p className="text-2xl font-bold text-purple-900">
                          ₹{mockReportData.payroll.avgSalary.toLocaleString()}
                        </p>
                      </div>
                      <TrendingUp className="w-8 h-8 text-purple-600" />
                    </div>
                  </div>
                </div>

                {/* Breakdown Chart */}
                <div>
                  <h4 className="text-md font-semibold text-gray-900 mb-4">Salary Breakdown</h4>
                  <div className="space-y-3">
                    {mockReportData.payroll.breakdown.map((item, index) => (
                      <div key={index} className="flex items-center justify-between">
                        <div className="flex items-center space-x-3">
                          <div className="w-4 h-4 bg-blue-500 rounded" style={{
                            backgroundColor: `hsl(${index * 60}, 70%, 50%)`
                          }}></div>
                          <span className="text-sm font-medium text-gray-700">{item.category}</span>
                        </div>
                        <div className="text-right">
                          <div className="text-sm font-semibold text-gray-900">
                            ₹{item.amount.toLocaleString()}
                          </div>
                          <div className="text-xs text-gray-500">{item.percentage}%</div>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            )}

            {selectedReport === 'employee' && (
              <div className="space-y-6">
                {/* Summary Cards */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div className="bg-gradient-to-r from-blue-50 to-blue-100 p-4 rounded-lg">
                    <div className="flex items-center justify-between">
                      <div>
                        <p className="text-sm text-blue-600 font-medium">Total Employees</p>
                        <p className="text-2xl font-bold text-blue-900">
                          {mockReportData.employee.totalEmployees}
                        </p>
                      </div>
                      <Users className="w-8 h-8 text-blue-600" />
                    </div>
                  </div>
                  <div className="bg-gradient-to-r from-green-50 to-green-100 p-4 rounded-lg">
                    <div className="flex items-center justify-between">
                      <div>
                        <p className="text-sm text-green-600 font-medium">New Hires</p>
                        <p className="text-2xl font-bold text-green-900">
                          {mockReportData.employee.newHires}
                        </p>
                      </div>
                      <TrendingUp className="w-8 h-8 text-green-600" />
                    </div>
                  </div>
                  <div className="bg-gradient-to-r from-red-50 to-red-100 p-4 rounded-lg">
                    <div className="flex items-center justify-between">
                      <div>
                        <p className="text-sm text-red-600 font-medium">Departures</p>
                        <p className="text-2xl font-bold text-red-900">
                          {mockReportData.employee.departures}
                        </p>
                      </div>
                      <Users className="w-8 h-8 text-red-600" />
                    </div>
                  </div>
                </div>

                {/* Department Distribution */}
                <div>
                  <h4 className="text-md font-semibold text-gray-900 mb-4">Department Distribution</h4>
                  <div className="space-y-3">
                    {mockReportData.employee.departments.map((dept, index) => (
                      <div key={index} className="flex items-center justify-between">
                        <div className="flex items-center space-x-3">
                          <div className="w-4 h-4 bg-blue-500 rounded" style={{
                            backgroundColor: `hsl(${index * 50}, 70%, 50%)`
                          }}></div>
                          <span className="text-sm font-medium text-gray-700">{dept.name}</span>
                        </div>
                        <div className="text-right">
                          <div className="text-sm font-semibold text-gray-900">{dept.count}</div>
                          <div className="text-xs text-gray-500">{dept.percentage}%</div>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            )}

            {(selectedReport === 'department' || selectedReport === 'attendance') && (
              <div className="text-center py-12">
                <FileText className="mx-auto h-12 w-12 text-gray-400" />
                <h3 className="mt-2 text-sm font-medium text-gray-900">Report Preview</h3>
                <p className="mt-1 text-sm text-gray-500">
                  Click "Generate Report" to view detailed {reportTypes.find(r => r.id === selectedReport)?.name.toLowerCase()} data.
                </p>
              </div>
            )}
          </div>
        </motion.div>
      </div>
    </div>
  );
};

export default Reports;
