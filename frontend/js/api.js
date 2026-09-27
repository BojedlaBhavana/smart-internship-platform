// API & Authentication Utility for Smart Internship Platform

const API_BASE = ''; // Relative path works seamlessly with Spring Boot

function getToken() {
  return localStorage.getItem('sip_token');
}

function getUser() {
  const user = localStorage.getItem('sip_user');
  return user ? JSON.parse(user) : null;
}

function setAuth(token, user) {
  localStorage.setItem('sip_token', token);
  localStorage.setItem('sip_user', JSON.stringify(user));
}

function clearAuth() {
  localStorage.removeItem('sip_token');
  localStorage.removeItem('sip_user');
  window.location.href = 'login.html';
}

function isAuthenticated() {
  return !!getToken();
}

function isAdmin() {
  const user = getUser();
  return user && user.role === 'ROLE_ADMIN';
}

function isStudent() {
  const user = getUser();
  return user && user.role === 'ROLE_STUDENT';
}

async function apiRequest(path, options = {}) {
  const headers = options.headers || {};
  const token = getToken();

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  if (!(options.body instanceof FormData)) {
    headers['Content-Type'] = 'application/json';
  }

  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers
  });

  if (response.status === 401) {
    clearAuth();
    throw new Error('Session expired. Please log in again.');
  }

  const data = await response.json();
  if (!response.ok || (data && data.success === false)) {
    throw new Error(data.message || 'An error occurred');
  }

  return data;
}

function showAlert(elementId, message, isError = true) {
  const el = document.getElementById(elementId);
  if (!el) return;
  el.textContent = message;
  el.className = `alert ${isError ? 'alert-error' : 'alert-success'}`;
  el.style.display = 'block';
  setTimeout(() => {
    el.style.display = 'none';
  }, 5000);
}

// Render dynamic navbar across all pages
function renderNavbar() {
  const navContainer = document.getElementById('navbar');
  if (!navContainer) return;

  const user = getUser();
  let linksHtml = '';

  if (user) {
    if (user.role === 'ROLE_ADMIN') {
      linksHtml = `
        <a href="admin.html" class="nav-link">Admin Portal</a>
        <a href="index.html" class="nav-link">Browse All</a>
        <div class="nav-user">
          <span class="role-badge role-admin">Admin</span>
          <span style="font-weight: 600; font-size: 0.9rem;">${user.fullName || user.email}</span>
          <button onclick="clearAuth()" class="btn btn-secondary btn-sm">Logout</button>
        </div>
      `;
    } else {
      linksHtml = `
        <a href="dashboard.html" class="nav-link">My Dashboard</a>
        <a href="index.html" class="nav-link">All Internships</a>
        <div class="nav-user">
          <span class="role-badge role-student">Student</span>
          <span style="font-weight: 600; font-size: 0.9rem;">${user.fullName || user.email}</span>
          <button onclick="clearAuth()" class="btn btn-secondary btn-sm">Logout</button>
        </div>
      `;
    }
  } else {
    linksHtml = `
      <a href="index.html" class="nav-link">Home</a>
      <a href="login.html" class="btn btn-secondary btn-sm">Login</a>
      <a href="register.html" class="btn btn-primary btn-sm">Register</a>
    `;
  }

  navContainer.innerHTML = `
    <a href="index.html" class="brand">
      <span class="brand-icon">🎓</span>
      <span>Smart Internship</span>
    </a>
    <div class="nav-links">
      ${linksHtml}
    </div>
  `;
}

document.addEventListener('DOMContentLoaded', renderNavbar);
