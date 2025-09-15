import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import {
  Plus,
  Search,
  Filter,
  Download,
  Eye,
  DollarSign,
  Calendar,
  CheckCircle,
  Clock,
  XCircle,
  FileText
} from 'lucide-react';

const Payroll = () => {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedStatus, setSelectedStatus] = useState('all');
  const [selectedMonth, setSelectedMonth] = useState('');

  // Mock data - replace with actual API calls
  const payrollRecords = [
    {
      id: '1',
      employeeId: 'EMP001',
      employeeName: 'John Doe',
      department: 'Engineering',
      payPeriod: '2024-01',
      basicSalary: 75000,
      allowances: 15000,
      deductions: 8500,
      netSalary: 81500,
      status: 'PAID',
      payDate: '2024-01-31'
    },
    {
      id: '2',
      employeeId: 'EMP002',
      employeeName: 'Jane Smith',
      department: 'HR',
      payPeriod: '2024-01',
      basicSalary: 65000,
      allowances: 12000,
      deductions: 7200,
      netSalary: 69800,
      status: 'APPROVED',
      payDate: null
    },
    {
      id: '3',
      employeeId: 'EMP003',
      employeeName: 'Mike Johnson',
      department: 'Sales',
      payPeriod: '2024-01',
      basicSalary: 55000,
      allowances: 18000,
      deductions: 6500,
      netSalary: 66500,
      status: 'PENDING',
      payDate: null
    }
  ];

  const statuses = ['PENDING', 'APPROVED', 'PAID', 'REJECTED'];
  const months = [
    { value: '2024-01', label: 'January 2024' },
    { value: '2023-12', label: 'December 2023' },
    { value: '2023-11', label: 'November 2023' }
  ];

  const filteredPayroll = payrollRecords.filter(record => {
    const matchesSearch = 
      record.employeeName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      record.employeeId.toLowerCase().includes(searchTerm.toLowerCase()) ||
      record.department.toLowerCase().includes(searchTerm.toLowerCase());
    
    const matchesStatus = selectedStatus === 'all' || record.status === selectedStatus;
    const matchesMonth = !selectedMonth || record.payPeriod === selectedMonth;
    
    return matchesSearch && matchesStatus && matchesMonth;
  });

  const getStatusIcon = (status) => {
    const icons = {
      PENDING: Clock,
      APPROVED: CheckCircle,
      PAID: CheckCircle,
      REJECTED: XCircle
    };
    return icons[status] || Clock;
  };

  const getStatusColor = (status) => {
    const colors = {
      PENDING: 'bg-yellow-100 text-yellow-800',
      APPROVED: 'bg-blue-100 text-blue-800',
      PAID: 'bg-green-100 text-green-800',
      REJECTED: 'bg-red-100 text-red-800'
    };
    return colors[status] || colors.PENDING;
  };

  const totalPayroll = filteredPayroll.reduce((sum, record) => sum + record.netSalary, 0);
  const paidAmount = filteredPayroll
    .filter(record => record.status === 'PAID')
    .reduce((sum, record) => sum + record.netSalary, 0);

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
          <div className="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
            <DollarSign className="w-6 h-6 text-green-600" />
          </div>
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Payroll Management</h1>
            <p className="text-gray-600">Manage employee payroll and salary processing</p>
          </div>
        </div>
        <div className="mt-4 sm:mt-0 flex items-center space-x-3">
          <motion.button
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            className="btn-secondary flex items-center space-x-2"
          >
            <Download className="w-4 h-4" />
            <span>Export</span>
          </motion.button>
          <motion.button
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            onClick={() => navigate('/payroll/generate')}
            className="btn-primary flex items-center space-x-2"
          >
            <Plus className="w-4 h-4" />
            <span>Generate Payroll</span>
          </motion.button>
        </div>
      </motion.div>

      {/* Stats Cards */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.1 }}
        className="grid grid-cols-1 md:grid-cols-4 gap-6"
      >
        <div className="bg-white rounded-xl shadow-soft p-6 border border-gray-100">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">Total Payroll</p>
              <p className="text-2xl font-bold text-gray-900">₹{totalPayroll.toLocaleString()}</p>
            </div>
            <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
              <DollarSign className="w-6 h-6 text-blue-600" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-soft p-6 border border-gray-100">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">Paid Amount</p>
              <p className="text-2xl font-bold text-gray-900">₹{paidAmount.toLocaleString()}</p>
            </div>
            <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
              <CheckCircle className="w-6 h-6 text-green-600" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-soft p-6 border border-gray-100">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">Pending</p>
              <p className="text-2xl font-bold text-gray-900">
                {filteredPayroll.filter(r => r.status === 'PENDING').length}
              </p>
            </div>
            <div className="w-12 h-12 bg-yellow-100 rounded-lg flex items-center justify-center">
              <Clock className="w-6 h-6 text-yellow-600" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-soft p-6 border border-gray-100">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">Processed</p>
              <p className="text-2xl font-bold text-gray-900">
                {filteredPayroll.filter(r => r.status === 'PAID').length}
              </p>
            </div>
            <div className="w-12 h-12 bg-purple-100 rounded-lg flex items-center justify-center">
              <FileText className="w-6 h-6 text-purple-600" />
            </div>
          </div>
        </div>
      </motion.div>

      {/* Filters */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.2 }}
        className="bg-white rounded-xl shadow-soft p-6 border border-gray-100"
      >
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          {/* Search */}
          <div className="relative">
            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
            <input
              type="text"
              placeholder="Search payroll..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="input-field pl-10"
            />
          </div>

          {/* Status Filter */}
          <div>
            <select
              value={selectedStatus}
              onChange={(e) => setSelectedStatus(e.target.value)}
              className="input-field"
            >
              <option value="all">All Statuses</option>
              {statuses.map(status => (
                <option key={status} value={status}>{status}</option>
              ))}
            </select>
          </div>

          {/* Month Filter */}
          <div>
            <select
              value={selectedMonth}
              onChange={(e) => setSelectedMonth(e.target.value)}
              className="input-field"
            >
              <option value="">All Months</option>
              {months.map(month => (
                <option key={month.value} value={month.value}>{month.label}</option>
              ))}
            </select>
          </div>

          {/* Clear Filters */}
          <div>
            <button
              onClick={() => {
                setSearchTerm('');
                setSelectedStatus('all');
                setSelectedMonth('');
              }}
              className="w-full btn-secondary flex items-center justify-center space-x-2"
            >
              <Filter className="w-4 h-4" />
              <span>Clear Filters</span>
            </button>
          </div>
        </div>
      </motion.div>

      {/* Payroll List */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.3 }}
        className="bg-white rounded-xl shadow-soft border border-gray-100 overflow-hidden"
      >
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-gray-200">
              <tr>
                <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Employee
                </th>
                <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Pay Period
                </th>
                <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Basic Salary
                </th>
                <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Allowances
                </th>
                <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Deductions
                </th>
                <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Net Salary
                </th>
                <th className="px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Status
                </th>
                <th className="px-6 py-4 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {filteredPayroll.map((record, index) => {
                const StatusIcon = getStatusIcon(record.status);
                return (
                  <motion.tr
                    key={record.id}
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ delay: index * 0.1 }}
                    className="hover:bg-gray-50 transition-colors duration-200"
                  >
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center">
                        <div className="w-10 h-10 bg-gradient-to-r from-green-500 to-blue-600 rounded-full flex items-center justify-center">
                          <span className="text-white font-semibold text-sm">
                            {record.employeeName.split(' ').map(n => n.charAt(0)).join('')}
                          </span>
                        </div>
                        <div className="ml-4">
                          <div className="text-sm font-medium text-gray-900">
                            {record.employeeName}
                          </div>
                          <div className="text-sm text-gray-500">{record.employeeId}</div>
                          <div className="text-sm text-gray-500">{record.department}</div>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center">
                        <Calendar className="w-4 h-4 text-gray-400 mr-2" />
                        <span className="text-sm text-gray-900">
                          {new Date(record.payPeriod + '-01').toLocaleDateString('en-US', { 
                            year: 'numeric', 
                            month: 'long' 
                          })}
                        </span>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      ₹{record.basicSalary.toLocaleString()}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-green-600">
                      +₹{record.allowances.toLocaleString()}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-red-600">
                      -₹{record.deductions.toLocaleString()}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-semibold text-gray-900">
                      ₹{record.netSalary.toLocaleString()}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center">
                        <span className={`inline-flex items-center px-2 py-1 text-xs font-semibold rounded-full ${getStatusColor(record.status)}`}>
                          <StatusIcon className="w-3 h-3 mr-1" />
                          {record.status}
                        </span>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                      <div className="flex items-center justify-end space-x-2">
                        <motion.button
                          whileHover={{ scale: 1.1 }}
                          whileTap={{ scale: 0.9 }}
                          onClick={() => navigate(`/payroll/${record.id}`)}
                          className="p-2 text-gray-400 hover:text-blue-600 transition-colors duration-200"
                        >
                          <Eye className="w-4 h-4" />
                        </motion.button>
                        <motion.button
                          whileHover={{ scale: 1.1 }}
                          whileTap={{ scale: 0.9 }}
                          className="p-2 text-gray-400 hover:text-green-600 transition-colors duration-200"
                        >
                          <Download className="w-4 h-4" />
                        </motion.button>
                      </div>
                    </td>
                  </motion.tr>
                );
              })}
            </tbody>
          </table>
        </div>

        {filteredPayroll.length === 0 && (
          <div className="text-center py-12">
            <DollarSign className="mx-auto h-12 w-12 text-gray-400" />
            <h3 className="mt-2 text-sm font-medium text-gray-900">No payroll records found</h3>
            <p className="mt-1 text-sm text-gray-500">
              Try adjusting your search or filter criteria.
            </p>
          </div>
        )}
      </motion.div>
    </div>
  );
};

export default Payroll;
