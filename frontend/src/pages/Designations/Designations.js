import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import {
  Plus,
  Search,
  Briefcase,
  Users,
  Edit,
  Trash2,
  Eye,
  TrendingUp
} from 'lucide-react';

const Designations = () => {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState('');

  // Mock data - replace with actual API calls
  const designations = [
    {
      id: '1',
      title: 'Senior Software Engineer',
      department: 'Engineering',
      level: 'Senior',
      minSalary: 80000,
      maxSalary: 120000,
      employeeCount: 15,
      description: 'Lead software development projects and mentor junior developers',
      requirements: ['5+ years experience', 'Bachelor\'s degree', 'Leadership skills'],
      createdDate: '2023-01-15'
    },
    {
      id: '2',
      title: 'HR Manager',
      department: 'Human Resources',
      level: 'Manager',
      minSalary: 60000,
      maxSalary: 90000,
      employeeCount: 3,
      description: 'Manage HR operations and employee relations',
      requirements: ['3+ years HR experience', 'MBA preferred', 'Communication skills'],
      createdDate: '2023-01-15'
    },
    {
      id: '3',
      title: 'Sales Executive',
      department: 'Sales',
      level: 'Executive',
      minSalary: 40000,
      maxSalary: 70000,
      employeeCount: 12,
      description: 'Drive sales growth and maintain client relationships',
      requirements: ['2+ years sales experience', 'Bachelor\'s degree', 'Negotiation skills'],
      createdDate: '2023-02-01'
    },
    {
      id: '4',
      title: 'Marketing Specialist',
      department: 'Marketing',
      level: 'Specialist',
      minSalary: 45000,
      maxSalary: 65000,
      employeeCount: 8,
      description: 'Develop and execute marketing campaigns',
      requirements: ['Digital marketing experience', 'Creative skills', 'Analytics knowledge'],
      createdDate: '2023-02-15'
    }
  ];

  const filteredDesignations = designations.filter(designation =>
    designation.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
    designation.department.toLowerCase().includes(searchTerm.toLowerCase()) ||
    designation.level.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const totalDesignations = designations.length;
  const totalEmployees = designations.reduce((sum, des) => sum + des.employeeCount, 0);
  const avgSalary = designations.reduce((sum, des) => sum + (des.minSalary + des.maxSalary) / 2, 0) / designations.length;

  const getLevelColor = (level) => {
    const colors = {
      'Senior': 'bg-purple-100 text-purple-800',
      'Manager': 'bg-blue-100 text-blue-800',
      'Executive': 'bg-green-100 text-green-800',
      'Specialist': 'bg-orange-100 text-orange-800',
      'Junior': 'bg-gray-100 text-gray-800'
    };
    return colors[level] || colors['Junior'];
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
          <div className="w-10 h-10 bg-orange-100 rounded-lg flex items-center justify-center">
            <Briefcase className="w-6 h-6 text-orange-600" />
          </div>
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Designations</h1>
            <p className="text-gray-600">Manage job roles and positions</p>
          </div>
        </div>
        <div className="mt-4 sm:mt-0">
          <motion.button
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            onClick={() => navigate('/designations/new')}
            className="btn-primary flex items-center space-x-2"
          >
            <Plus className="w-4 h-4" />
            <span>Add Designation</span>
          </motion.button>
        </div>
      </motion.div>

      {/* Stats Cards */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.1 }}
        className="grid grid-cols-1 md:grid-cols-3 gap-6"
      >
        <div className="bg-white rounded-xl shadow-soft p-6 border border-gray-100">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">Total Designations</p>
              <p className="text-2xl font-bold text-gray-900">{totalDesignations}</p>
            </div>
            <div className="w-12 h-12 bg-orange-100 rounded-lg flex items-center justify-center">
              <Briefcase className="w-6 h-6 text-orange-600" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-soft p-6 border border-gray-100">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">Total Employees</p>
              <p className="text-2xl font-bold text-gray-900">{totalEmployees}</p>
            </div>
            <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
              <Users className="w-6 h-6 text-blue-600" />
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-soft p-6 border border-gray-100">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">Avg Salary Range</p>
              <p className="text-2xl font-bold text-gray-900">₹{Math.round(avgSalary / 1000)}K</p>
            </div>
            <div className="w-12 h-12 bg-green-100 rounded-lg flex items-center justify-center">
              <TrendingUp className="w-6 h-6 text-green-600" />
            </div>
          </div>
        </div>
      </motion.div>

      {/* Search */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.2 }}
        className="bg-white rounded-xl shadow-soft p-6 border border-gray-100"
      >
        <div className="relative max-w-md">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
          <input
            type="text"
            placeholder="Search designations..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="input-field pl-10"
          />
        </div>
      </motion.div>

      {/* Designations Grid */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.3 }}
        className="grid grid-cols-1 lg:grid-cols-2 gap-6"
      >
        {filteredDesignations.map((designation, index) => (
          <motion.div
            key={designation.id}
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ delay: index * 0.1 }}
            whileHover={{ y: -5, scale: 1.02 }}
            className="bg-white rounded-xl shadow-soft hover:shadow-medium p-6 border border-gray-100 transition-all duration-300"
          >
            <div className="flex items-start justify-between mb-4">
              <div className="flex items-center space-x-3">
                <div className="w-12 h-12 bg-gradient-to-r from-orange-500 to-red-600 rounded-lg flex items-center justify-center">
                  <Briefcase className="w-6 h-6 text-white" />
                </div>
                <div>
                  <h3 className="text-lg font-semibold text-gray-900">{designation.title}</h3>
                  <p className="text-sm text-gray-600">{designation.department}</p>
                </div>
              </div>
              <div className="flex items-center space-x-2">
                <motion.button
                  whileHover={{ scale: 1.1 }}
                  whileTap={{ scale: 0.9 }}
                  onClick={() => navigate(`/designations/${designation.id}`)}
                  className="p-2 text-gray-400 hover:text-blue-600 transition-colors duration-200"
                >
                  <Eye className="w-4 h-4" />
                </motion.button>
                <motion.button
                  whileHover={{ scale: 1.1 }}
                  whileTap={{ scale: 0.9 }}
                  onClick={() => navigate(`/designations/${designation.id}/edit`)}
                  className="p-2 text-gray-400 hover:text-green-600 transition-colors duration-200"
                >
                  <Edit className="w-4 h-4" />
                </motion.button>
                <motion.button
                  whileHover={{ scale: 1.1 }}
                  whileTap={{ scale: 0.9 }}
                  className="p-2 text-gray-400 hover:text-red-600 transition-colors duration-200"
                >
                  <Trash2 className="w-4 h-4" />
                </motion.button>
              </div>
            </div>

            <div className="mb-4">
              <div className="flex items-center justify-between mb-3">
                <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${getLevelColor(designation.level)}`}>
                  {designation.level}
                </span>
                <div className="flex items-center space-x-1 text-sm text-gray-600">
                  <Users className="w-4 h-4" />
                  <span>{designation.employeeCount} employees</span>
                </div>
              </div>
              
              <p className="text-sm text-gray-600 mb-3">{designation.description}</p>
              
              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-500">Salary Range</span>
                  <span className="text-sm font-medium text-gray-900">
                    ₹{designation.minSalary.toLocaleString()} - ₹{designation.maxSalary.toLocaleString()}
                  </span>
                </div>
              </div>
            </div>

            <div className="mb-4">
              <h4 className="text-sm font-medium text-gray-900 mb-2">Requirements</h4>
              <div className="flex flex-wrap gap-2">
                {designation.requirements.slice(0, 3).map((req, idx) => (
                  <span
                    key={idx}
                    className="inline-flex px-2 py-1 text-xs bg-gray-100 text-gray-700 rounded-md"
                  >
                    {req}
                  </span>
                ))}
                {designation.requirements.length > 3 && (
                  <span className="inline-flex px-2 py-1 text-xs bg-gray-100 text-gray-700 rounded-md">
                    +{designation.requirements.length - 3} more
                  </span>
                )}
              </div>
            </div>

            <div className="pt-4 border-t border-gray-200">
              <div className="flex items-center justify-between">
                <span className="text-xs text-gray-500">
                  Created: {new Date(designation.createdDate).toLocaleDateString()}
                </span>
                <motion.button
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                  onClick={() => navigate(`/designations/${designation.id}`)}
                  className="text-sm text-blue-600 hover:text-blue-700 font-medium"
                >
                  View Details
                </motion.button>
              </div>
            </div>
          </motion.div>
        ))}
      </motion.div>

      {filteredDesignations.length === 0 && (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          className="text-center py-12"
        >
          <Briefcase className="mx-auto h-12 w-12 text-gray-400" />
          <h3 className="mt-2 text-sm font-medium text-gray-900">No designations found</h3>
          <p className="mt-1 text-sm text-gray-500">
            Try adjusting your search criteria or create a new designation.
          </p>
        </motion.div>
      )}
    </div>
  );
};

export default Designations;
