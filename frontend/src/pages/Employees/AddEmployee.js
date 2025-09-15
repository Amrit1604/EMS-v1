import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { useNavigate } from 'react-router-dom';
import {
  ArrowLeft,
  Save,
  User,
  Mail,
  Phone,
  Calendar,
  Building,
  Briefcase,
  DollarSign,
  MapPin,
  CreditCard
} from 'lucide-react';

const AddEmployee = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    // Personal Information
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    dateOfBirth: '',
    joiningDate: '',
    
    // Employment Details
    employeeCode: '',
    department: '',
    designation: '',
    baseSalary: '',
    employmentType: 'FULL_TIME',
    status: 'ACTIVE',
    
    // Address
    street: '',
    city: '',
    state: '',
    zipCode: '',
    country: 'India',
    
    // Bank Details
    bankName: '',
    accountNumber: '',
    ifscCode: '',
    accountHolderName: ''
  });

  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  const departments = ['Engineering', 'HR', 'Sales', 'Marketing', 'Finance', 'Operations'];
  const designations = ['Manager', 'Senior Developer', 'Developer', 'Executive', 'Analyst', 'Specialist'];
  const employmentTypes = ['FULL_TIME', 'PART_TIME', 'CONTRACT', 'INTERN'];

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    // Clear error when user starts typing
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const validateForm = () => {
    const newErrors = {};
    
    // Required fields validation
    if (!formData.firstName.trim()) newErrors.firstName = 'First name is required';
    if (!formData.lastName.trim()) newErrors.lastName = 'Last name is required';
    if (!formData.email.trim()) newErrors.email = 'Email is required';
    if (!formData.phoneNumber.trim()) newErrors.phoneNumber = 'Phone number is required';
    if (!formData.employeeCode.trim()) newErrors.employeeCode = 'Employee code is required';
    if (!formData.department) newErrors.department = 'Department is required';
    if (!formData.designation) newErrors.designation = 'Designation is required';
    if (!formData.baseSalary) newErrors.baseSalary = 'Base salary is required';
    if (!formData.joiningDate) newErrors.joiningDate = 'Joining date is required';
    
    // Email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (formData.email && !emailRegex.test(formData.email)) {
      newErrors.email = 'Please enter a valid email address';
    }
    
    // Phone validation
    const phoneRegex = /^[0-9]{10}$/;
    if (formData.phoneNumber && !phoneRegex.test(formData.phoneNumber.replace(/\D/g, ''))) {
      newErrors.phoneNumber = 'Please enter a valid 10-digit phone number';
    }
    
    // Salary validation
    if (formData.baseSalary && (isNaN(formData.baseSalary) || parseFloat(formData.baseSalary) <= 0)) {
      newErrors.baseSalary = 'Please enter a valid salary amount';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }
    
    setIsSubmitting(true);
    
    try {
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 2000));
      
      // Navigate back to employees list
      navigate('/employees', { 
        state: { message: 'Employee added successfully!' }
      });
    } catch (error) {
      console.error('Error adding employee:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
        className="flex items-center justify-between"
      >
        <div className="flex items-center space-x-4">
          <motion.button
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            onClick={() => navigate('/employees')}
            className="p-2 rounded-lg border border-gray-300 hover:bg-gray-50 transition-colors duration-200"
          >
            <ArrowLeft className="w-5 h-5 text-gray-600" />
          </motion.button>
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Add New Employee</h1>
            <p className="text-gray-600">Create a new employee record</p>
          </div>
        </div>
      </motion.div>

      <form onSubmit={handleSubmit} className="space-y-8">
        {/* Personal Information */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.1 }}
          className="bg-white rounded-xl shadow-soft p-6 border border-gray-100"
        >
          <div className="flex items-center space-x-3 mb-6">
            <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center">
              <User className="w-6 h-6 text-blue-600" />
            </div>
            <div>
              <h2 className="text-xl font-semibold text-gray-900">Personal Information</h2>
              <p className="text-sm text-gray-600">Basic employee details</p>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                First Name *
              </label>
              <input
                type="text"
                name="firstName"
                value={formData.firstName}
                onChange={handleInputChange}
                className={`input-field ${errors.firstName ? 'border-red-300' : ''}`}
                placeholder="Enter first name"
              />
              {errors.firstName && (
                <p className="mt-1 text-sm text-red-600">{errors.firstName}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Last Name *
              </label>
              <input
                type="text"
                name="lastName"
                value={formData.lastName}
                onChange={handleInputChange}
                className={`input-field ${errors.lastName ? 'border-red-300' : ''}`}
                placeholder="Enter last name"
              />
              {errors.lastName && (
                <p className="mt-1 text-sm text-red-600">{errors.lastName}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Email Address *
              </label>
              <div className="relative">
                <Mail className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleInputChange}
                  className={`input-field pl-10 ${errors.email ? 'border-red-300' : ''}`}
                  placeholder="Enter email address"
                />
              </div>
              {errors.email && (
                <p className="mt-1 text-sm text-red-600">{errors.email}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Phone Number *
              </label>
              <div className="relative">
                <Phone className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type="tel"
                  name="phoneNumber"
                  value={formData.phoneNumber}
                  onChange={handleInputChange}
                  className={`input-field pl-10 ${errors.phoneNumber ? 'border-red-300' : ''}`}
                  placeholder="Enter phone number"
                />
              </div>
              {errors.phoneNumber && (
                <p className="mt-1 text-sm text-red-600">{errors.phoneNumber}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Date of Birth
              </label>
              <div className="relative">
                <Calendar className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type="date"
                  name="dateOfBirth"
                  value={formData.dateOfBirth}
                  onChange={handleInputChange}
                  className="input-field pl-10"
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Joining Date *
              </label>
              <div className="relative">
                <Calendar className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type="date"
                  name="joiningDate"
                  value={formData.joiningDate}
                  onChange={handleInputChange}
                  className={`input-field pl-10 ${errors.joiningDate ? 'border-red-300' : ''}`}
                />
              </div>
              {errors.joiningDate && (
                <p className="mt-1 text-sm text-red-600">{errors.joiningDate}</p>
              )}
            </div>
          </div>
        </motion.div>

        {/* Employment Details */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.2 }}
          className="bg-white rounded-xl shadow-soft p-6 border border-gray-100"
        >
          <div className="flex items-center space-x-3 mb-6">
            <div className="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
              <Briefcase className="w-6 h-6 text-green-600" />
            </div>
            <div>
              <h2 className="text-xl font-semibold text-gray-900">Employment Details</h2>
              <p className="text-sm text-gray-600">Job-related information</p>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Employee Code *
              </label>
              <input
                type="text"
                name="employeeCode"
                value={formData.employeeCode}
                onChange={handleInputChange}
                className={`input-field ${errors.employeeCode ? 'border-red-300' : ''}`}
                placeholder="Enter employee code"
              />
              {errors.employeeCode && (
                <p className="mt-1 text-sm text-red-600">{errors.employeeCode}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Department *
              </label>
              <div className="relative">
                <Building className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
                <select
                  name="department"
                  value={formData.department}
                  onChange={handleInputChange}
                  className={`input-field pl-10 ${errors.department ? 'border-red-300' : ''}`}
                >
                  <option value="">Select Department</option>
                  {departments.map(dept => (
                    <option key={dept} value={dept}>{dept}</option>
                  ))}
                </select>
              </div>
              {errors.department && (
                <p className="mt-1 text-sm text-red-600">{errors.department}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Designation *
              </label>
              <select
                name="designation"
                value={formData.designation}
                onChange={handleInputChange}
                className={`input-field ${errors.designation ? 'border-red-300' : ''}`}
              >
                <option value="">Select Designation</option>
                {designations.map(designation => (
                  <option key={designation} value={designation}>{designation}</option>
                ))}
              </select>
              {errors.designation && (
                <p className="mt-1 text-sm text-red-600">{errors.designation}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Base Salary *
              </label>
              <div className="relative">
                <DollarSign className="absolute left-3 top-1/2 transform -translate-y-1/2 w-5 h-5 text-gray-400" />
                <input
                  type="number"
                  name="baseSalary"
                  value={formData.baseSalary}
                  onChange={handleInputChange}
                  className={`input-field pl-10 ${errors.baseSalary ? 'border-red-300' : ''}`}
                  placeholder="Enter base salary"
                />
              </div>
              {errors.baseSalary && (
                <p className="mt-1 text-sm text-red-600">{errors.baseSalary}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Employment Type
              </label>
              <select
                name="employmentType"
                value={formData.employmentType}
                onChange={handleInputChange}
                className="input-field"
              >
                {employmentTypes.map(type => (
                  <option key={type} value={type}>{type.replace('_', ' ')}</option>
                ))}
              </select>
            </div>
          </div>
        </motion.div>

        {/* Address */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.3 }}
          className="bg-white rounded-xl shadow-soft p-6 border border-gray-100"
        >
          <div className="flex items-center space-x-3 mb-6">
            <div className="w-10 h-10 bg-orange-100 rounded-lg flex items-center justify-center">
              <MapPin className="w-6 h-6 text-orange-600" />
            </div>
            <div>
              <h2 className="text-xl font-semibold text-gray-900">Address Information</h2>
              <p className="text-sm text-gray-600">Residential address details</p>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="md:col-span-2">
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Street Address
              </label>
              <input
                type="text"
                name="street"
                value={formData.street}
                onChange={handleInputChange}
                className="input-field"
                placeholder="Enter street address"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                City
              </label>
              <input
                type="text"
                name="city"
                value={formData.city}
                onChange={handleInputChange}
                className="input-field"
                placeholder="Enter city"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                State
              </label>
              <input
                type="text"
                name="state"
                value={formData.state}
                onChange={handleInputChange}
                className="input-field"
                placeholder="Enter state"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                ZIP Code
              </label>
              <input
                type="text"
                name="zipCode"
                value={formData.zipCode}
                onChange={handleInputChange}
                className="input-field"
                placeholder="Enter ZIP code"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Country
              </label>
              <input
                type="text"
                name="country"
                value={formData.country}
                onChange={handleInputChange}
                className="input-field"
                placeholder="Enter country"
              />
            </div>
          </div>
        </motion.div>

        {/* Bank Details */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.4 }}
          className="bg-white rounded-xl shadow-soft p-6 border border-gray-100"
        >
          <div className="flex items-center space-x-3 mb-6">
            <div className="w-10 h-10 bg-purple-100 rounded-lg flex items-center justify-center">
              <CreditCard className="w-6 h-6 text-purple-600" />
            </div>
            <div>
              <h2 className="text-xl font-semibold text-gray-900">Bank Details</h2>
              <p className="text-sm text-gray-600">Banking information for salary payments</p>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Bank Name
              </label>
              <input
                type="text"
                name="bankName"
                value={formData.bankName}
                onChange={handleInputChange}
                className="input-field"
                placeholder="Enter bank name"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Account Number
              </label>
              <input
                type="text"
                name="accountNumber"
                value={formData.accountNumber}
                onChange={handleInputChange}
                className="input-field"
                placeholder="Enter account number"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                IFSC Code
              </label>
              <input
                type="text"
                name="ifscCode"
                value={formData.ifscCode}
                onChange={handleInputChange}
                className="input-field"
                placeholder="Enter IFSC code"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Account Holder Name
              </label>
              <input
                type="text"
                name="accountHolderName"
                value={formData.accountHolderName}
                onChange={handleInputChange}
                className="input-field"
                placeholder="Enter account holder name"
              />
            </div>
          </div>
        </motion.div>

        {/* Submit Button */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.5 }}
          className="flex justify-end space-x-4"
        >
          <motion.button
            type="button"
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            onClick={() => navigate('/employees')}
            className="btn-secondary"
          >
            Cancel
          </motion.button>
          <motion.button
            type="submit"
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
            disabled={isSubmitting}
            className="btn-primary flex items-center space-x-2"
          >
            <Save className="w-4 h-4" />
            <span>{isSubmitting ? 'Saving...' : 'Save Employee'}</span>
          </motion.button>
        </motion.div>
      </form>
    </div>
  );
};

export default AddEmployee;
